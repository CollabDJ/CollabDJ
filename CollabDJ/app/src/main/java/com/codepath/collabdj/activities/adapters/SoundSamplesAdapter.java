package com.codepath.collabdj.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.models.SoundSample;

import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

public class SoundSamplesAdapter extends RecyclerView.Adapter<SoundSamplesAdapter.ViewHolder> {

    // Tag for logging.
    private final String TAG = SoundSamplesAdapter.class.getName();

    private List<SoundSample> mSamples;
    private Context mContext;

    public SoundSamplesAdapter(Context context, List<SoundSample> samples) {
        this.mContext = context;
        this.mSamples = samples;
    }

    private Context getContext() {
        return this.mContext;
    }

    // Inflates a layout from XML and returns it in the ViewHolder.
    @Override
    public SoundSamplesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout.
        View soundSampleView = inflater.inflate(R.layout.item_sound_sample, parent, false);

        // Return a new holder instance.
        ViewHolder viewHolder = new ViewHolder(soundSampleView);
        return viewHolder;
    }

    // Populates data into the viewHolder.
    @Override
    public void onBindViewHolder(SoundSamplesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position.
        SoundSample soundSample = mSamples.get(position);
        // Bind the data to the viewHolder.
        viewHolder.bind(soundSample);
    }

    // Returns the total count of items in the list.
    @Override
    public int getItemCount() {
        return mSamples.size();
    }

    // Used to cache the views within the item layout for fast access.
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageButton ibPlay;
        public ImageView ivStatus;

        // Constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ibPlay = (ImageButton) itemView.findViewById(R.id.ibPlay);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
        }

        // Sets the sound sample information into their respective views.
        public void bind(SoundSample soundSample) {
            tvTitle.setText(soundSample.getName());
        }
    }
}
