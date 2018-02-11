package com.masmoudi.vache_taureau;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud on 10/02/2018.
 */

public class CustomAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;

    public CustomAdapter(@NonNull Activity context, ArrayList<String> titles, ArrayList<String> subtitles) {
        super(context, R.layout.history_layout, titles);
        this.context = context;
        this.titles = titles;
        this.subtitles = subtitles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.history_layout, null, true);

        TextView titleTextView = (TextView)rowView.findViewById(R.id.titleTextView);
        TextView subtitleTextView = (TextView)rowView.findViewById(R.id.subtitleTextView);

        titleTextView.setText(titles.get(position));
        subtitleTextView.setText(subtitles.get(position));

        return rowView;
    }
}
