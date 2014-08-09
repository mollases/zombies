package com.mollases.zombies.pregame.sidemenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mollases.zombies.R;

import java.util.List;

/**
 * Created by mollases on 5/12/14.
 */
public class MenuOptionArrayAdapter extends ArrayAdapter<MenuOption> {

    public MenuOptionArrayAdapter(final Context context, final int textViewResourceId, final List<MenuOption> objects) {
        super(context, textViewResourceId, objects);
    }

    public View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        final MenuOption menuOption = getItem(position);
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.menu_option, null);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.menu_option_label);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (menuOption != null) {
            holder.text.setText(menuOption.getOptionLabel());
        }

        return view;
    }

    static class ViewHolder {
        TextView text;
    }
}
