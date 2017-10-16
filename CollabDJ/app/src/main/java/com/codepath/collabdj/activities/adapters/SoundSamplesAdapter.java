package com.codepath.collabdj.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.models.SoundSample;
import com.codepath.collabdj.activities.utils.PlayPauseButton;

import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

public class SoundSamplesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Tag for logging.
    private final String TAG = SoundSamplesAdapter.class.getName();

    private List<Object> mSamples;
    private Context mContext;

    public SoundSamplesAdapter(Context context, List<Object> samples) {
        this.mContext = context;
        this.mSamples = samples;
    }

    private Context getContext() {
        return this.mContext;
    }

    // Inflates a layout from XML and returns it in the ViewHolder.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        /*
        // Inflate the custom layout.
        View soundSampleView = inflater.inflate(R.layout.item_sound_sample, parent, false);

        // Return a new holder instance.
        ViewHolderSample viewHolder = new ViewHolderSample(soundSampleView);
        return viewHolder;
        */

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == 0) {
            View emptySampleView = inflater.inflate(R.layout.item_add_sound_sample, parent, false);
            viewHolder = new ViewHolderEmpty(emptySampleView);
        } else if (viewType == 1) {
            View soundSampleView = inflater.inflate(R.layout.item_sound_sample, parent, false);
            viewHolder = new ViewHolderSample(soundSampleView);
        }

        return viewHolder;
    }

    // Populates data into the viewHolder.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == 0) {
            ViewHolderEmpty vhEmpty = (ViewHolderEmpty) viewHolder;
            vhEmpty.bind();

        } else if (viewHolder.getItemViewType() == 1) {
            ViewHolderSample vhSample = (ViewHolderSample) viewHolder;
            SoundSample soundSample = (SoundSample) mSamples.get(position);
            vhSample.bind(soundSample);
        }
        /*
        // Get the data model based on position.
        SoundSample soundSample = mSamples.get(position);
        // Bind the data to the viewHolder.
        viewHolder.bind(soundSample);
        */
    }

    // Returns the total count of items in the list.
    @Override
    public int getItemCount() {
        return mSamples.size();
    }

    // Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mSamples.get(position) instanceof String) {
            return 0; // Zero means empty
        } else if (mSamples.get(position) instanceof SoundSample) {
            return 1; // One means sample.
        }
        // Error, log a message.
        Log.d(TAG, "getItemViewType is not choosing any valid type and returns -1.");
        return -1;
    }

    // Used to cache the views within the item layout for fast access.
    public class ViewHolderSample extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public PlayPauseButton ibPlayPause;
        public ImageView ivStatus;

        // Constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolderSample(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ibPlayPause = (PlayPauseButton) itemView.findViewById(R.id.ibPlayPause);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);

            // Set listener on the `play/pause` button.
            ibPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toggle the button image.
                    ibPlayPause.toggleIsPlaying();
                    int position = getAdapterPosition();
                    ((SoundSample) mSamples.get(position)).toggleIsPlaying();
                }
            });
        }

        // Sets the sound sample information into their respective views.
        public void bind(SoundSample soundSample) {
            tvTitle.setText(soundSample.getName());
            ibPlayPause.setIsPlaying(soundSample.getIsPlaying());
        }
    }

    // Used to cache the views within the item layout for fast access.
    public class ViewHolderEmpty extends RecyclerView.ViewHolder {

        public ImageButton ibAddSample;

        // Constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolderEmpty(View itemView) {
            super(itemView);

            ibAddSample = (ImageButton) itemView.findViewById(R.id.ibAddSample);

            // Set listener on the `add` button.
            ibAddSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    SoundSample soundSample = new SoundSample("Fresh Sample", 0, "", 0.0, "");
                    mSamples.set(position, soundSample);
                    notifyDataSetChanged();
                }
            });
        }

        // Sets the sound sample information into their respective views.
        public void bind() {

        }
    }
}
