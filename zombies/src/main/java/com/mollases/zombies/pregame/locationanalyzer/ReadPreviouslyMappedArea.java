package com.mollases.zombies.pregame.locationanalyzer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.gmap.Zone;
import com.mollases.zombies.util.AsyncHelper;
import com.mollases.zombies.util.DeviceInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mollases on 4/20/14.
 */
class ReadPreviouslyMappedArea extends AsyncTask<Void, Void, JSONArray> {
    private final static String TAG = ReadPreviousMaps.class.getName();
    private final ZombMapLocationAnalyzer mapLocationAnalyzer;
    private final String name;
    private final String id;


    public ReadPreviouslyMappedArea(ZombMapLocationAnalyzer mapLocationAnalyzer, String name, String id) {
        this.mapLocationAnalyzer = mapLocationAnalyzer;
        this.name = name;
        this.id = id;
    }


    @Override
    protected JSONArray doInBackground(Void... params) {      // Making HTTP request
        try {
            ZombClient client = new ZombClient(ZService.MAPPER_DATA_LIST);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(mapLocationAnalyzer));
            client.add(ZParam.MAP_DATA_ID, id);

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

        try {

            List<LatLng> coords = new ArrayList<LatLng>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                coords.add(new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude")));
            }


            if (coords.isEmpty()) {
                Toast.makeText(mapLocationAnalyzer, "No Points were recorded", Toast.LENGTH_SHORT).show();
            } else {
                Zone zone = new Zone(Zone.Type.UNACTIVE, name, Integer.valueOf(id), coords);
                mapLocationAnalyzer.setCurrentZone(zone);
            }
        } catch (JSONException e) {
            Log.e(TAG, "couldn't get an id from the server");
        }
    }
}