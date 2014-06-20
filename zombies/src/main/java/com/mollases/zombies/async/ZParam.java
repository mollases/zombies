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
     * Current map identifier
     */
    MAP_DATA_ID("map_data_id"),

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
    private String name;

    ZParam(String name) {
        this.name = name;
    }

    /**
     * Gets the server translated name of the ZParam
     *
     * @return ZParam's server side name
     */
    public String getName() {
        return name;
    }

}
