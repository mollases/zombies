package com.mollases.zombies.pregame.joinablegames;

import android.content.Context;
import android.os.AsyncTask;

import com.mollases.zombies.async.ZParam;
import com.mollases.zombies.async.ZService;
import com.mollases.zombies.async.ZombClient;
import com.mollases.zombies.util.DeviceInformation;

import java.io.IOException;

/**
 * Created by mollases on 4/22/14.
 */
public class UpdateJoinableGame extends AsyncTask<Integer, Void, Void> {
    private final static String TAG = UpdateJoinableGame.class.getName();
    private final boolean isJoining;
    private final Context context;


    public UpdateJoinableGame(Context context, boolean isJoining) {
        this.context = context;
        this.isJoining = isJoining;
    }


    @Override
    protected Void doInBackground(Integer... params) {      // Making HTTP request
        try {
            ZombClient client = new ZombClient(ZService.UPDATE_JOINABLE_GAME);

            client.add(ZParam.DEVICE_ID, DeviceInformation.getRegistrationIdAsString(context));
            client.add(ZParam.GAME_ID, String.valueOf(params[0]));
            client.add(ZParam.STATE, String.valueOf(isJoining));

            client.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
