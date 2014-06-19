package com.mollases.zombies.pregame.joinablegames;

import com.mollases.zombies.util.ZTime;


/**
 * Created by mollases on 4/22/14.
 */
public class JoinableGame {

    private final String gameTitle;
    private final String gamePlace;
    private final ZTime gameStartTime;
    private final ZTime gameEndTime;
    private final int gameCurrentPlayers;
    private final int gameMaxPlayers;
    private final int id;
    private boolean selected;

    public JoinableGame(int id, boolean selected, String gameTitle, String gamePlace, String gameStartTime, String gameEndTime, int gameCurrentPlayers, int gameMaxPlayers) {
        this.id = id;
        this.selected = selected;
        this.gameTitle = gameTitle;
        this.gamePlace = gamePlace;

        this.gameStartTime = new ZTime(gameStartTime);
        this.gameEndTime = new ZTime(gameEndTime);
        this.gameCurrentPlayers = gameCurrentPlayers;
        this.gameMaxPlayers = gameMaxPlayers;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        selected = isSelected;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getGamePlace() {
        return gamePlace;
    }


    public String getGameStartTime() {
        return gameStartTime.getDisplayTime();
    }


    public String getGameEndTime() {
        return gameEndTime.getDisplayTime();
    }


    public String getGameDate() {
        return gameStartTime.getDisplayDate();
    }

    public int getGameCurrentPlayers() {
        return gameCurrentPlayers;
    }

    public int getId() {
        return id;
    }

    public int getGameMaxPlayers() {
        return gameMaxPlayers;
    }
}
