package com.mollases.zombies.pregame.newgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mollases.zombies.R;
import com.mollases.zombies.pregame.locationanalyzer.ZombMapLocationAnalyzer;
import com.mollases.zombies.util.ZTime;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class GameSelectorFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = GameSelectorFragment.class.getName();
    private final long millsecondsPerSecond = 1000L;
    private final long secondsPerMin = 60L;
    private final long minsPerHour = 60L;
    private final long hoursPerDay = 24L;
    private final long daysPerMonth = 31L;

    private OnLocationSaved mHostInterface;

    private String mapTitle;
    private double delta;
    private double lat;
    private double lng;

    public GameSelectorFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mHostInterface = (OnLocationSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLocationSaved");
        }

        mapTitle = mHostInterface.locationSavedTitle();
        delta = mHostInterface.locationSavedDelta();
        lat = mHostInterface.locationSavedLatitude();
        lng = mHostInterface.locationSavedLongitude();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_selector, container, false);
        view.findViewById(R.id.game_selector_location_row).setOnClickListener(this);
        view.findViewById(R.id.game_selector_max_people_row).setOnClickListener(this);
        view.findViewById(R.id.game_selector_new_game_next).setOnClickListener(this);
        if (mapTitle != null && !mapTitle.isEmpty()) {
            ((TextView) view.findViewById(R.id.game_selector_location)).setText(mapTitle);
        }
        DatePicker dateView = (DatePicker) view.findViewById(R.id.game_selector_date);
        dateView.setMinDate(System.currentTimeMillis() - millsecondsPerSecond);
        dateView.setMaxDate(System.currentTimeMillis() + (
                millsecondsPerSecond * secondsPerMin * minsPerHour * hoursPerDay * daysPerMonth * 3L
        ));

        return view;
    }

    private void onPlaceChanged(View view) {
        Intent i = new Intent(getActivity(), ZombMapLocationAnalyzer.class);
        i.putExtra("set_location", true);
        startActivity(i);
    }

    private void onMaxPlayersChanged(View view) {
        generateAlert("How many people?",
                (TextView) view.findViewById(R.id.game_selector_max_people),
                Arrays.asList("5", "10", "20", "50", "infinite")).show();
    }

    private void onNextButtonPressed() {
        TextView maxPeopleView = (TextView) getActivity().findViewById(R.id.game_selector_max_people);
        TextView locationView = (TextView) getActivity().findViewById(R.id.game_selector_location);
        DatePicker dateView = (DatePicker) getActivity().findViewById(R.id.game_selector_date);
        TimePicker fromView = (TimePicker) getActivity().findViewById(R.id.game_selector_from_time);
        TimePicker untilView = (TimePicker) getActivity().findViewById(R.id.game_selector_until_time);
        TextView titleView = (TextView) getActivity().findViewById(R.id.game_selector_title);

        String maxPeople = String.valueOf(maxPeopleView.getText());
        String location = String.valueOf(locationView.getText());
        String title = String.valueOf(titleView.getText());

        int day = dateView.getDayOfMonth();
        int month = dateView.getMonth() + 1;
        int year = dateView.getYear();

        int fromMinute = fromView.getCurrentMinute();
        int fromHour = fromView.getCurrentHour();

        int untilMinute = untilView.getCurrentMinute();
        int untilHour = untilView.getCurrentHour();

        ZTime fromTime = new ZTime(year, month, day, fromHour, fromMinute);
        ZTime untilTime = new ZTime(year, month, day, untilHour, untilMinute);

        Log.i(TAG, "from " + fromTime.toString() + ", until " + untilTime.toString());

        Pair<Boolean, String> validate = validateInformation(fromTime, untilTime);

        if (validate.first) {
            String deltaString = String.valueOf(delta);
            String mapTitleString = String.valueOf(mapTitle);
            String latString = String.valueOf(lat);
            String lngString = String.valueOf(lng);

            new CreateNewGame(getActivity()).execute(
                    title,
                    location,
                    fromTime.toString(),
                    untilTime.toString(),
                    maxPeople,
                    mapTitleString,
                    deltaString,
                    latString,
                    lngString);

        } else {
            Toast.makeText(getActivity(), validate.second, Toast.LENGTH_SHORT).show();
        }
    }

    private Pair<Boolean, String> validateInformation(ZTime fromTime, ZTime untilTime) {
        String reasonForFailure = null;
        if (mapTitle == null || mapTitle.isEmpty()) {
            reasonForFailure = "You have to set a location for your game.";
        } else if (fromTime.compare(untilTime) >= 0) {
            reasonForFailure = "Invalid time slots.";
        }
        if (reasonForFailure != null) {
            return new Pair<Boolean, String>(Boolean.FALSE, reasonForFailure);
        }
        return new Pair<Boolean, String>(Boolean.TRUE, "");
    }

    private AlertDialog.Builder generateAlert(String title, final TextView inputView, List<String> range) {
        final ListView listView = new ListView(getActivity());
        final String originalText = inputView.getText().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(title);

        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, range));
        alert.setView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputView.setText(((TextView) view).getText().toString());
                view.setBackgroundColor(getResources().getColor(R.color.black_overlay));
            }
        });

        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.setNegativeButton("Nevermind...", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                inputView.setText(originalText);
            }
        });

        return alert;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_selector_location_row: {
                onPlaceChanged(v);
                break;
            }
            case R.id.game_selector_max_people_row: {
                onMaxPlayersChanged(v);
                break;
            }
            case R.id.game_selector_new_game_next: {
                onNextButtonPressed();
                break;
            }
        }
    }

    public interface OnLocationSaved {
        public String locationSavedTitle();

        public double locationSavedLatitude();

        public double locationSavedLongitude();

        public double locationSavedDelta();
    }
}
