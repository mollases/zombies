package com.mollases.zombies.async;

/**
 * List of possible parameters allowed on the servers end
 * This is used to keep track of the keywords already in play and allow for maximum reuse
 */
public enum ZParam {
    /**
     * Method Identifier
     */
    SERVER_QUERY("m"),

    /**
     * Device Identifier
     */
    DEVICE_ID("id"),

    /**
     * Current match identifier
     */
    GAME_ID("game_id"),

    /**
     * Current latitude of player
     */
    LATITUDE("lat"),

    /**
     * Current longitude of player
     */
    LONGITUDE("long"),

    /**
     * Current state of player [1,0]
     */
    STATE("state"),

    /**
     * Current status of player [0...n]
     */
    STATUS("status"),

    /**
     * Current time zone of player [-/+ D]
     */
    TIMEZONE("tz"),

    /**
     * Current time
     */
    CURRENT_TIME("time"),

    /**
     * Title of object
     */
    TITLE("title"),

    @Deprecated
    LOCATION("loc"),

    /**
     * From time
     */
    FROM_TIME("from"),

    /**
     * Until time
     */
    UNTIL_TIME("until"),

    /**
     * Maximum players being used
     */
    MAX_PLAYERS("max");

    /**
     * What the ZParam translates to on the server
     */
    private String serverName;

    ZParam(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Gets the server translated name of the ZParam
     *
     * @return ZParam's server side name
     */
    public String getServerName() {
        return serverName;
    }

}
