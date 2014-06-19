package com.mollases.zombies.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * Created by mollases on 2/20/14.
 */
public class SetPhoneIdToGCM extends AsyncTask<Void, Void, Integer> {
    private final static String TAG = SetPhoneIdToGCM.class.getName();
    private final Context context;
    private final String phoneId;
    private final String registrationId;


    public SetPhoneIdToGCM(Context context, String phoneId, String registrationId) {
        this.context = context;
        this.phoneId = phoneId;
        this.registrationId = registrationId;
    }


    @Override
    protected Integer doInBackground(Void... params) {      // Making HTTP request
//        try {
//            // defaultHttpClient
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(MessageWatcherActivity.mollasesURL);
//
//            ArrayList<NameValuePair> postParameters;
//            postParameters = new ArrayList<NameValuePair>();
//            postParameters.add(new BasicNameValuePair("m", "gcmid"));
//            postParameters.add(new BasicNameValuePair("gcm_id", registrationId));
//            postParameters.add(new BasicNameValuePair("id", phoneId));
//            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
//
//            httpClient.execute(httpPost);
//            return 1;
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        return 0;
    }


    @Override
    protected void onPostExecute(final Integer status) {
        if (status.equals(0)) {
            Toast.makeText(context, "There was a problem sending the message to the server", Toast.LENGTH_SHORT).show();
        }
    }
}
