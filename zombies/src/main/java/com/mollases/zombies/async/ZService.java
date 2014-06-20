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
    REGISTER(true, "register", new ZParam[]{}, true),

    MAPPER(true, "mapper",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.TITLE}, true
    ),

    MAPPER_DATA(true, "mapperData",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.MAP_DATA_ID,
                    ZParam.LATITUDE,
                    ZParam.LONGITUDE}, true
    ),

    IN_GAME_PLAYER_PIN_MAPPER_DATA(true, "inGameMapperData",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID,
                    ZParam.LATITUDE,
                    ZParam.LONGITUDE,
                    ZParam.STATUS}, true
    ),

    CREATE_JOINABLE_GAME(true, "createJoinableGame",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.TIMEZONE,
                    ZParam.TITLE,
                    ZParam.LOCATION,
                    ZParam.FROM_TIME,
                    ZParam.UNTIL_TIME,
                    ZParam.MAX_PLAYERS}, false
    ),

    UPDATE_JOINABLE_GAME(true, "updateJoinableGame",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID,
                    ZParam.STATE}, true
    ),


    MAPPER_LIST(false, "mapperList",
            new ZParam[]{
                    ZParam.DEVICE_ID}, true
    ),

    MAPPER_DATA_LIST(false, "mapperDataList",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.MAP_DATA_ID}, true
    ),

    ACTIVE_GAMES(false, "activeGames",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.TIMEZONE}, false
    ),

    JOINABLE_GAMES(false, "joinableGames",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.TIMEZONE}, false
    ),

    IN_GAME_UPDATE(false, "inGameUpdate",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID}, true
    ),

    IN_GAME_UPDATE_PLAYER_PIN(false, "updatePlayerPin",
            new ZParam[]{
                    ZParam.DEVICE_ID,
                    ZParam.GAME_ID}, true
    );

    /**
     * Tag to log messages with
     */
    private final static String TAG = ZombClient.class.getName();

    final boolean post;
    final boolean fullyImplemented;
    final Set<ZParam> requiredZParams;
    final String method;

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
    boolean isPost() {
        return post;
    }

    /**
     * Adds the method call that this service is linked to
     *
     * @param data the map to add this call to
     */
    void addMethodCall(Map<ZParam, String> data) {
        data.put(ZParam.SERVER_QUERY, method);
    }

    /**
     * Verifies that the expected number of required arguments are present as well
     * as the service call itself (Service#addMethodCall(...))
     *
     * @param data the map to check
     */
    void assertValidity(Map<ZParam, String> data) {
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
