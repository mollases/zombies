package com.mollases.zombies.pregame.joinablegames;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mollases.zombies.R;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class JoinableGamesFragment extends ListFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JoinableGamesFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        new ReadAvailableGames(this).execute();
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

        alert.setPositiveButton(R.string.view_joinable_games_detail_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!game.isSelected()) {
                    view.findViewById(R.id.view_joinable_games_item).setBackgroundResource(
                            R.color.joinable_game_selected);
                    game.setSelected(true);
                    new UpdateJoinableGame(getActivity(), true).execute(game.getId());
                }
            }
        });

        alert.setNegativeButton(R.string.view_joinable_games_detail_decline, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                if (game.isSelected()) {
                    view.findViewById(R.id.view_joinable_games_item).setBackgroundResource(
                            R.color.joinable_game_not_selected);
                    game.setSelected(false);
                    new UpdateJoinableGame(getActivity(), false).execute(game.getId());
                }
            }
        });

        alert.show();

    }

}
