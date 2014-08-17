package com.mollases.zombies.ingame;

import android.os.AsyncTask;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.gmap.Pin;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mollases on 5/28/14.
 */
public class UpdatePlayerPinLocation extends AsyncTask<Void, Void, JSONObject> {
    private final static String TAG = UpdatePlayerPinLocation.class.getName();
    private final ZombMapActivity zombMapActivity;

    public UpdatePlayerPinLocation(ZombMapActivity zombMapActivity) {
        this.zombMapActivity = zombMapActivity;
    }


    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            int gameId = DeviceInformation.getInGameId(zombMapActivity);
            if (gameId != -1) {
                ZombClient client = new ZombClient(ZService.IN_GAME_RETRIEVE_PLAYER_PIN_LOCATION);
                client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(zombMapActivity));
                client.add(ZParam.GAME_ID, String.valueOf(gameId));
                return AsyncHelper.pullJSONObject(client.execute());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    @Override
    protected void onPostExecute(final JSONObject obj) {
        zombMapActivity.setPlayerPin(
                Pin.Type.getTypeByAssociatedNumber(obj.optInt("status")),
                obj.optDouble("latitude", 0L),
                obj.optDouble("longitude", 0L)
        );
    }
}
