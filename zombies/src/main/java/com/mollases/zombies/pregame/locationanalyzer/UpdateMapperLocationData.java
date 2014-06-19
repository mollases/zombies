package com.mollases.zombies.pregame.locationanalyzer;

import android.content.Context;
import android.os.AsyncTask;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.DeviceInformation;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

/**
 * Created by mollases on 2/20/14.
 */
class UpdateMapperLocationData extends AsyncTask<String, Void, Void> {
    private final static String TAG = UpdateMapperLocationData.class.getName();
    private final Context context;

    public UpdateMapperLocationData(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(String... params) {      // Making HTTP request
        try {
            ZombClient client = new ZombClient(ZService.MAPPER_DATA);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(context));
            client.add(ZParam.MAP_DATA_ID, params[0]);
            client.add(ZParam.LATITUDE, params[1]);
            client.add(ZParam.LONGITUDE, params[2]);

            client.execute();
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
