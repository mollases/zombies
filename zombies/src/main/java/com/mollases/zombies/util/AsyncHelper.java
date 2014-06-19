package com.mollases.zombies.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mollases on 1/29/14.
 */
public class AsyncHelper {
    private final static String TAG = AsyncHelper.class.getName();


    public static JSONObject pullJSONObject(HttpResponse httpResponse) throws IOException, JSONException {
        String response = read(httpResponse);
        if (isEmpty(response))
            return new JSONObject();
        return new JSONObject(response);
    }


    public static JSONArray pullJSONArray(HttpResponse httpResponse) throws IOException, JSONException {
        String response = read(httpResponse);
        if (isEmpty(response))
            return new JSONArray();
        return new JSONArray(response);
    }

    private static boolean isEmpty(String readResponse) {
        if (readResponse.isEmpty() || readResponse.equals("\"\"\n")) {
            Log.i(TAG, "empty response...");
            return true;
        }
        return false;
    }

    private static String read(HttpResponse httpResponse) throws IOException {

        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        is.close();
        String jsonString = sb.toString();
        Log.d(TAG, jsonString);
        return jsonString;
    }
}
