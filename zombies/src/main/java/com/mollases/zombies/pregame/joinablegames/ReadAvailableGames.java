package com.mollases.zombies.pregame.joinablegames;

import android.app.ListFragment;
import android.os.AsyncTask;

import com.mollases.zombies.R;
import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;
import com.mollases.zombies.util.ZTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mollases on 4/22/14.
 */
class ReadAvailableGames extends AsyncTask<Void, Void, JSONArray> {
    private static final String TAG = ReadAvailableGames.class.getName();
    private final ListFragment listFragment;

    public ReadAvailableGames(ListFragment listFragment) {
        this.listFragment = listFragment;
    }


    protected JSONArray doInBackground(final Void... voids) {
        JSONArray array = new JSONArray();
        try {
            ZombClient client = new ZombClient(ZService.GET_JOINABLE_GAMES);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(listFragment.getActivity()));
            client.add(ZParam.CURRENT_TIME, new ZTime().toString());

            array = AsyncHelper.pullJSONArray(client.execute());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }


    @Override
    protected void onPostExecute(final JSONArray array) {
        List<JoinableGame> joinableGames = new ArrayList<JoinableGame>();

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                joinableGames.add(new JoinableGame(
                        obj.getInt("id"),
                        obj.optBoolean("selected", false),
                        obj.getString("title"),
                        obj.optString("map_title"),
                        obj.getString("start_time"),
                        obj.getString("end_time"),
                        obj.getInt("current_players"),
                        obj.getInt("max_players")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listFragment.setListAdapter(
                new JoinableGamesArrayAdapter(listFragment.getActivity(),
                        R.layout.view_joinable_games_open_item,
                        joinableGames)
        );
    }
}