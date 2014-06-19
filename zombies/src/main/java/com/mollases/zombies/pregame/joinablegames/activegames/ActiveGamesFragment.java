package com.mollases.zombies.pregame.joinablegames.activegames;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mollases.zombies.R;
import com.mollases.zombies.ingame.ZombMapFragment;
import com.mollases.zombies.pregame.joinablegames.JoinableGame;
import com.mollases.zombies.util.DeviceInformation;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ActiveGamesFragment extends ListFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActiveGamesFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        new ReadActiveGames(this).execute();
    }


    @Override
    public void onListItemClick(ListView l, final View view, int position, long id) {
        final JoinableGame game = (JoinableGame) getListAdapter().getItem(position);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(game.getGameTitle());

        View v = View.inflate(getActivity(), R.layout.view_joinable_game_detail, null);
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_place)).setText(game.getGamePlace());
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_date)).setText(game.getGameDate());
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_from)).setText(game.getGameStartTime());
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_until)).setText(game.getGameEndTime());
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_current)).setText(String.valueOf(game.getGameCurrentPlayers()));
        ((TextView) v.findViewById(R.id.view_joinable_game_detail_max)).setText(String.valueOf(game.getGameMaxPlayers()));

        alert.setView(v);

        alert.setPositiveButton(R.string.view_active_games_detail_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DeviceInformation.storeCurrentlyActiveGame(getActivity(), game.getId());
                startActivity(new Intent(getActivity(), ZombMapFragment.class));
            }
        });

        alert.show();
    }

}
