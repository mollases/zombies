package com.mollases.zombies.pregame.joinablegames;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mollases.zombies.R;

import java.util.List;

/**
 * Created by mollases on 4/22/14.
 */
public class JoinableGamesArrayAdapter extends ArrayAdapter<JoinableGame> {


    public JoinableGamesArrayAdapter(final Context context, final int textViewResourceId, final List<JoinableGame> objects) {
        super(context, textViewResourceId, objects);
    }


    public View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        final JoinableGame joinableGame = getItem(position);
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.view_joinable_games_open_item, null);
            holder = new ViewHolder();
            holder.item = (LinearLayout) view.findViewById(R.id.view_joinable_games_item);
            holder.gameTitle = (TextView) view.findViewById(R.id.view_joinable_games_open_title_label);
            holder.gamePlace = (TextView) view.findViewById(R.id.view_joinable_games_open_where_label);
            holder.gameCurrentPlayers = (TextView) view.findViewById(R.id.view_joinable_games_open_current_players_label);
            holder.gameMaxPlayers = (TextView) view.findViewById(R.id.view_joinable_games_open_max_players_label);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (joinableGame != null) {
            holder.item.setBackgroundResource(
                    joinableGame.isSelected() ?
                            R.color.joinable_game_selected :
                            R.color.joinable_game_not_selected
            );

            holder.gameTitle.setText(joinableGame.getGameTitle());
            holder.gamePlace.setText(joinableGame.getGamePlace());
            holder.gameCurrentPlayers.setText(String.valueOf(joinableGame.getGameCurrentPlayers()));
            holder.gameMaxPlayers.setText(String.valueOf(joinableGame.getGameMaxPlayers()));
        }

        return view;
    }


    static class ViewHolder {
        LinearLayout item;
        TextView gameTitle;
        TextView gamePlace;
        TextView gameCurrentPlayers;
        TextView gameMaxPlayers;
    }
}