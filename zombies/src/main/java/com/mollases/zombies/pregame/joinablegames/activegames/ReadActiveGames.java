package com.mollases.zombies.pregame.joinablegames.activegames;

import android.app.ListFragment;
import android.os.AsyncTask;

import com.mollases.zombies.R;
import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.pregame.joinablegames.JoinableGame;
import com.mollases.zombies.pregame.joinablegames.JoinableGamesArrayAdapter;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mollases on 4/22/14.
 */
class ReadActiveGames extends AsyncTask<Void, Void, JSONArray> {
    private static final String TAG = ReadActiveGames.class.getName();
    private final ListFragment listFragment;

    public ReadActiveGames(ListFragment listFragment) {
        this.listFragment = listFragment;
    }


    protected JSONArray doInBackground(final Void... voids) {

        try {

            ZombClient client = new ZombClient(ZService.ACTIVE_GAMES);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(listFragment.getActivity()));
            client.add(ZParam.TIMEZONE, String.valueOf(DeviceInformation.getTimeZoneByHourlyOffset()));

            return AsyncHelper.pullJSONArray(client.execute());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
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
                        obj.optString("place"),
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