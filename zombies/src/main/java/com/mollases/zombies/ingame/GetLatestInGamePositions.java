package com.mollases.zombies.ingame;

import android.os.AsyncTask;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.gmap.Pin;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mollases on 5/3/14.
 */
class GetLatestInGamePositions extends AsyncTask<Void, Void, JSONArray> {
    private final static String TAG = GetLatestInGamePositions.class.getName();
    private final ZombMapFragment zombMapFragment;

    public GetLatestInGamePositions(ZombMapFragment zombMapFragment) {
        this.zombMapFragment = zombMapFragment;
    }


    @Override
    protected JSONArray doInBackground(Void... params) {
        try {
            int gameId = DeviceInformation.getInGameId(zombMapFragment);
            if (gameId != -1) {
                ZombClient client = new ZombClient(ZService.IN_GAME_UPDATE);
                client.add(ZParam.GAME_ID, String.valueOf(gameId));
                client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(zombMapFragment));
                return AsyncHelper.pullJSONArray(client.execute());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    @Override
    protected void onPostExecute(final JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int id = obj.getInt("player_id");


                Pin.Type type = Pin.Type.getTypeByAssociatedNumber(obj.getInt("status"));
                double lat = obj.getDouble("latitude");
                double lng = obj.getDouble("longitude");

                zombMapFragment.updatePin(id, type, lat, lng);

            }
            zombMapFragment.scanMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
