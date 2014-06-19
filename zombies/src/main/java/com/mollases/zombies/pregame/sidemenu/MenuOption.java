package com.mollases.zombies.pregame.sidemenu;


/**
 * TODO: move the PreGameActivities#ActiveFragments enum into its own class
 * Created by mollases on 5/12/14.
 */
public enum MenuOption {


    GAME_CREATOR("Set up a Game", "Create a game"),
    GAME_AVAIL_VIEWER("View Available Games", "Available Games"),
    GAME_ACTIVE_VIEWER("View Active Games", "Active Games"),
    MAP_ANALYSIS_TOOL("Map analysis tool", "Map Analyser");

    final String optionLabel;
    final String optionDesc;

    MenuOption(String optionLabel, String optionDesc) {
        this.optionDesc = optionDesc;
        this.optionLabel = optionLabel;
    }

    public String getOptionDesc() {
        return optionDesc;
    }


    public String getOptionLabel() {
        return optionLabel;
    }
}
