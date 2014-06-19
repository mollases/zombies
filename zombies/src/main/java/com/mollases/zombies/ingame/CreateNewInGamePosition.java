package com.mollases.zombies.ingame;

import android.content.Context;
import android.os.AsyncTask;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.DeviceInformation;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

/**
 * Created by mollases on 5/3/14.
 */
class CreateNewInGamePosition extends AsyncTask<String, Void, Void> {
    private final static String TAG = CreateNewInGamePosition.class.getName();
    private final Context context;

    public CreateNewInGamePosition(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(String... params) {
        try {
            ZombClient client = new ZombClient(ZService.IN_GAME_PLAYER_PIN_MAPPER_DATA);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(context));
            client.add(ZParam.GAME_ID, DeviceInformation.getInGameIdAsString(context));
            client.add(ZParam.LATITUDE, params[0]);
            client.add(ZParam.LONGITUDE, params[1]);
            client.add(ZParam.STATUS, params[2]);

            client.execute();

        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
