package com.mollases.zombies.async;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Zomb Client provides a clean extracted interface to communicate with the Mollases.com webserver
 * The Service that the Zomb Client is constructed with has some implementation details hidden.
 * The way to use the client is to specify a service, and add key value pairs to the client. and then execute.
 * Upon execution the client connects, creates is http request and fires away.
 */
public class ZombClient {

    /**
     * Tag to log messages with
     */
    private final static String TAG = ZombClient.class.getName();

    /**
     * convience instead of magic words
     */
    private final static String INITIAL_QUERY = "?";

    /**
     * convience instead of magic words
     */
    private final static String EQUALS = "=";

    /**
     * convience instead of magic words
     */
    private final static String APPEND = "&";

    /**
     * The url to the current server api
     */
    private final static String ZOMB_URL = "http://www.mollases.com/son/zomb.php";

    /**
     * The service to execute
     */
    private final ZService service;

    /**
     * The map before alterations
     */
    private final Map<ZParam, String> data;

    /**
     * Initializes a map and sets it up for validations and executions against a given service
     *
     * @param service The Services created from its list of services
     */
    public ZombClient(ZService service) {
        this.service = service;
        data = new HashMap<ZParam, String>();
    }

    /**
     * Adds a key value pair to the map
     */
    public void add(ZParam key, String value) {

        data.put(key, value);
    }

    /**
     * Performs some validation and executes the request after validation.
     *
     * @return the HTTPResponse of the underlying HTTPRequestBase
     * @throws IOException thrown from the underlying execute method
     */
    public HttpResponse execute() throws IOException {
        Log.i(TAG, "Executing " + service.name());
        service.addMethodCall(data);
        service.assertValidity(data);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpRequestBase base;
        if (service.isPost()) {
            base = new HttpPost(ZOMB_URL);
            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            for (Map.Entry<ZParam, String> entry : data.entrySet()) {
                postParameters.add(new BasicNameValuePair(entry.getKey().getServerName(), entry.getValue()));
            }
            ((HttpPost) base).setEntity(new UrlEncodedFormEntity(postParameters));

        } else {
            String completeUrl = ZOMB_URL + INITIAL_QUERY;

            for (Map.Entry<ZParam, String> entry : data.entrySet()) {
                completeUrl += entry.getKey().getServerName() + EQUALS + entry.getValue() + APPEND;
            }
            completeUrl = completeUrl.substring(0, completeUrl.length() - 1);

            base = new HttpGet(completeUrl);
            Log.i(TAG, completeUrl);
        }

        return httpClient.execute(base);
    }
}
