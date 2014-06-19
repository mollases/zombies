package com.mollases.zombies.pregame;

import android.content.Context;
import android.os.AsyncTask;

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
public class StoreNewPhoneId extends AsyncTask<Void, Void, JSONObject> {
    private final static String TAG = StoreNewPhoneId.class.getName();
    private final Context context;


    public StoreNewPhoneId(Context context) {
        this.context = context;
    }


    @Override
    protected JSONObject doInBackground(Void... params) {      // Making HTTP request
        try {
            ZombClient client = new ZombClient(ZService.REGISTER);
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
        try {
            DeviceInformation.storeRegistrationId(context, status.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
