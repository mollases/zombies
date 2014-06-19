package com.mollases.zombies.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mollases.zombies.R;

//import com.mollases.zombies.MessageWatcherActivity;

/**
 * Created by mollases on 2/20/14.
 */
public class OverCastNotification {
    private static int msgCount = 0;
    private final Context context;
    private int mId;

    public OverCastNotification(Context context) {
        this.context = context;
    }

    public void reset() {
        msgCount = 0;
    }

    public void notifyDevice() {
        msgCount++;
        String contentTitle = "New droplet" + (msgCount == 1 ? "" : "s") + "!";
        String contentText = "View the latest droplet" + (msgCount == 1 ? "" : "s");

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                .setContentTitle(contentTitle)
                .setContentInfo(String.valueOf(msgCount))
                .setContentText(contentText)
                .setTicker(contentTitle)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);


// Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(context, MessageWatcherActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.

// Adds the back stack for the Intent (but not the Intent itself)

// Adds the Intent that starts the Activity to the top of the stack
        Intent intent = new Intent(context, RegistrationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, builder.build());
    }
}
