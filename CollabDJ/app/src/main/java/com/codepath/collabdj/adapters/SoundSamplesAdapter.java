package com.codepath.collabdj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.utils.PlayPauseButton;
import com.codepath.collabdj.views.SoundSampleView;

import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

public class SoundSamplesAdapter extends RecyclerView.Adapter<SoundSamplesAdapter.ViewHolder> {

    public interface SoundSamplePlayListener {
        /**
         * Notifies the listener that the play button on a SoundSampleInstance was pressed
         * @param soundSampleInstance
         */
        void playButtonPressed(SoundSampleInstance soundSampleInstance);
    }

    // Tag for logging.
    private final String TAG = SoundSamplesAdapter.class.getName();

    private List<SoundSampleInstance> mSamples;
    private Context mContext;
    private SoundSamplePlayListener soundSamplePlayListener;

    public SoundSamplesAdapter(Context context,
                               List<SoundSampleInstance> samples,
                               SoundSamplePlayListener soundSamplePlayListener) {
        this.mContext = context;
        this.mSamples = samples;
        this.soundSamplePlayListener = soundSamplePlayListener;
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
        SoundSampleInstance soundSample = mSamples.get(position);
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
        public PlayPauseButton ibPlayPause;
        public ImageView ivStatus;

        // Constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolder(View itemView) {
            super(itemView);

            ((SoundSampleView)itemView).viewHolder = this;

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ibPlayPause = (PlayPauseButton) itemView.findViewById(R.id.ibPlayPause);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);

            // Set listener on the `play/pause` button.
            ibPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (soundSamplePlayListener != null) {
                        soundSamplePlayListener.playButtonPressed(getSoundSampleInstance());
                    }
                }
            });
        }

        public SoundSampleInstance getSoundSampleInstance() {
            return mSamples.get(getAdapterPosition());
        }

        // Sets the sound sample information into their respective views.
        public void bind(SoundSampleInstance soundSample) {
            tvTitle.setText(soundSample.getSoundSample().getName());
        }
    }
}
