package com.mollases.zombies.pregame.locationanalyzer;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mollases on 4/19/14.
 */
class ReadPreviousMaps extends AsyncTask<Void, Void, JSONArray> {
    private final static String TAG = ReadPreviousMaps.class.getName();
    private final ZombMapLocationAnalyzer mapLocationAnalyzer;


    public ReadPreviousMaps(ZombMapLocationAnalyzer mapLocationAnalyzer) {
        this.mapLocationAnalyzer = mapLocationAnalyzer;
    }


    @Override
    protected JSONArray doInBackground(Void... params) {      // Making HTTP request
        try {

            ZombClient client = new ZombClient(ZService.MAPPER_LIST);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(mapLocationAnalyzer));

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
        Log.d(TAG, array.toString());

        Set<Pair<String, Integer>> pairings = new HashSet<Pair<String, Integer>>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Pair<String, Integer> pair = new Pair<String, Integer>(obj.getString("name"), obj.getInt("id"));
                pairings.add(pair);
            }
        } catch (JSONException e) {
            Log.e(TAG, "couldn't get an id from the server");
        }
        mapLocationAnalyzer.setMapperList(pairings);
    }
}