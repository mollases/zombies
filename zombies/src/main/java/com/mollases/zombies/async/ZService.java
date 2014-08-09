package com.mollases.zombies.async;

import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mollases on 5/30/14.
 */
public enum ZService {
    /**
     * Registers a new device with the server
     */
    REGISTER(true, "register", new ZParam[]{}, true),

    /**
     * creates a new game on the server
     */
    CREATE_NEW_GAME(true, "createJoinableGame",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.TIMEZONE,
                    ZParam.TITLE,
                    ZParam.LOCATION,
                    ZParam.FROM_TIME,
                    ZParam.UNTIL_TIME,
                    ZParam.MAX_PLAYERS}, false
    ),

    /**
     * updates game, tells if device has joined game or opted out of joined game
     */
    ADD_REMOVE_PLAYER_FROM_GAME(true, "updateJoinableGame",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID,
                    ZParam.STATE}, true
    ),

    /**
     * finds games that are active and have been joined by device
     */
    GET_ACTIVE_GAMES(true, "activeGames",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.CURRENT_TIME}, false
    ),

    /**
     * finds games that have not started
     */
    GET_JOINABLE_GAMES(true, "joinableGames",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.CURRENT_TIME}, false
    ),

    /**
     * requests new status from server for game
     * device id is needed so no duplicate information is passed
     */
    IN_GAME_UPDATE_MAP(false, "inGameUpdate",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID}, true
    ),

    /**
     * tells playing map where device is while game is active
     */
    IN_GAME_SEND_PLAYER_PIN_LOCATION(true, "inGameMapperData",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID,
                    ZParam.LATITUDE,
                    ZParam.LONGITUDE,
                    ZParam.STATUS}, true
    ),

    /**
     * gets device's location according to the server
     */
    IN_GAME_RETRIEVE_PLAYER_PIN_LOCATION(false, "updatePlayerPin",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID}, true
    );

    /**
     * Tag to log messages with
     */
    private final static String TAG = ZombClient.class.getName();

    final private boolean post;
    final private boolean fullyImplemented;
    final private Set<ZParam> requiredZParams;
    final private String method;

    ZService(boolean post, String method, ZParam[] requiredZParams, boolean fullyImplemented) {
        this.post = post;
        this.method = method;
        this.requiredZParams = new HashSet<ZParam>(Arrays.asList(requiredZParams));
        this.fullyImplemented = fullyImplemented;
    }

    /**
     * Does this service exist as under the http POST?
     *
     * @return true if yes
     */
    public boolean isPost() {
        return post;
    }

    /**
     * Adds the method call that this service is linked to
     *
     * @param data the map to add this call to
     */
    public void addMethodCall(Map<ZParam, String> data) {
        data.put(ZParam.SERVER_QUERY, method);
    }

    /**
     * Verifies that the expected number of required arguments are present as well
     * as the service call itself (Service#addMethodCall(...))
     *
     * @param data the map to check
     */
    public void assertValidity(Map<ZParam, String> data) {
        if (!fullyImplemented) {
            Log.w(TAG, method + " not fully implemented!");
        }

        for (ZParam param : requiredZParams) {
            if (data.get(param) == null) {
                throw new IllegalStateException("Missing " + param.name() + " parameter");
            }
        }
    }
}
