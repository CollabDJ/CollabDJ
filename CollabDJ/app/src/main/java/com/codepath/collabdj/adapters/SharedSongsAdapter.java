package com.codepath.collabdj.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SharedSong;

import java.util.List;

/**
 * Created by chenrangong on 10/21/17.
 */

public class SharedSongsAdapter extends ArrayAdapter<SharedSong> {

    // View Lookup cache
    private static class ViewHolder{
        TextView tvTitle;
    }
    public SharedSongsAdapter(Context context, List<SharedSong> sharedSongs){
        super(context, android.R.layout.simple_list_item_1, sharedSongs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the data item from position
        SharedSong song = getItem(position);

        // check the existing view being resued
        ViewHolder viewHolder; // view lookup cache stored in tag
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_shared_songs, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.sharedSongTV);
            // cache the viewHolder object inseide the fresh view
            convertView.setTag(viewHolder);
        } else{
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate data
        viewHolder.tvTitle.setText(song.getTitle());

        return convertView;
    }
}
