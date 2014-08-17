package com.mollases.zombies.ingame;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mollases.zombies.R;
import com.mollases.zombies.gmap.Pin;

/**
 * Created by mollases on 5/28/14.
 */
public class UpdateInGameUI extends AsyncTask<Void, Void, Void> {
    private final static String TAG = UpdateInGameUI.class.getName();
    private final Activity activity;
    private final Pin.Type type;
    private final int warningRate;

    public UpdateInGameUI(Activity activity, Pin.Type type, int warningRate) {
        this.activity = activity;
        this.type = type;
        this.warningRate = warningRate;
    }


    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }


    @Override
    protected void onPostExecute(final Void array) {
        TextView textView = (TextView) activity.findViewById(R.id.in_game_current_pin_type);
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.in_game_conversion_progress_bar);
        textView.setText(type.name());
        progressBar.setProgress(warningRate);
    }
}