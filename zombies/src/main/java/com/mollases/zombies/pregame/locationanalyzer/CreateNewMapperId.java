package com.mollases.zombies.pregame.locationanalyzer;

import android.os.AsyncTask;
import android.util.Log;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mollases on 2/20/14.
 */
class CreateNewMapperId extends AsyncTask<String, Void, JSONObject> {
    private final static String TAG = CreateNewMapperId.class.getName();
    private final ZombMapLocationAnalyzer mapLocationAnalyzer;


    public CreateNewMapperId(ZombMapLocationAnalyzer mapLocationAnalyzer) {
        this.mapLocationAnalyzer = mapLocationAnalyzer;
    }


    @Override
    protected JSONObject doInBackground(String... params) {      // Making HTTP request
        try {
            ZombClient client = new ZombClient(ZService.MAPPER);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(mapLocationAnalyzer));
            client.add(ZParam.TITLE, params[0]);

            return AsyncHelper.pullJSONObject(client.execute());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    @Override
    protected void onPostExecute(final JSONObject status) {
        Log.d(TAG, status.toString());
        try {
            mapLocationAnalyzer.setMapperId(status.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
            mapLocationAnalyzer.setMapperId(0);
            Log.e(TAG, "couldn't get an id from the server");
        }
    }
}
