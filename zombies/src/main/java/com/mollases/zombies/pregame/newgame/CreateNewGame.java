package com.mollases.zombies.pregame.newgame;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.pregame.joinablegames.UpdateJoinableGame;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mollases on 4/24/14.
 */
class CreateNewGame extends AsyncTask<String, Void, JSONObject> {
    private final static String TAG = CreateNewGame.class.getName();
    private final Context context;


    public CreateNewGame(Context context) {
        this.context = context;
    }


    @Override
    protected JSONObject doInBackground(String... params) {      // Making HTTP request
        try {

            ZombClient client = new ZombClient(ZService.CREATE_NEW_GAME);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(context));
            client.add(ZParam.TIMEZONE, String.valueOf(DeviceInformation.getTimeZoneByHourlyOffset()));
            client.add(ZParam.TITLE, params[0]);
            client.add(ZParam.LOCATION, params[1]);
            client.add(ZParam.FROM_TIME, params[2]);
            client.add(ZParam.UNTIL_TIME, params[3]);
            client.add(ZParam.MAX_PLAYERS, params[4]);
            client.add(ZParam.MAP_TITLE, params[5]);
            client.add(ZParam.MAP_DELTA, params[6]);
            client.add(ZParam.LATITUDE, params[7]);
            client.add(ZParam.LONGITUDE, params[8]);
            return AsyncHelper.pullJSONObject(client.execute());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final JSONObject result) {
        try {
            new UpdateJoinableGame(context, true).execute(result.getInt("id"));
            Toast.makeText(context, "Game created.", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "Auto-joining this game..", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}