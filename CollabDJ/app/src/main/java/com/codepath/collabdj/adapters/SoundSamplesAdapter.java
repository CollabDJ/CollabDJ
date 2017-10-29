package com.codepath.collabdj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SoundSampleInstance;
import com.codepath.collabdj.views.SoundSampleView;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

/**
 * Created by tiago on 10/12/17.
 */

public class SoundSamplesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface SoundSamplePlayListener {
        /**
         * Notifies the listener that the play button on a SoundSampleInstance was pressed
         * @param soundSampleInstance
         * @return returns the milliseconds per section so when a sample is queued to play it knows
         *      how to display the pie chart percentage
         */
        long playButtonPressed(SoundSampleInstance soundSampleInstance);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

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
            SoundSampleInstance soundSampleInstance =  mSamples.get(position);
            vhSample.bind(soundSampleInstance);
        }
    }

    // Returns the total count of items in the list.
    @Override
    public int getItemCount() {
        return mSamples.size();
    }

    // Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mSamples.get(position).getSoundSample() == null) {
            return 0; // Zero means empty
        } else if (mSamples.get(position).getSoundSample() != null) {
            return 1; // One means sample.
        }
        // Error, log a message.
        Log.d(TAG, "getItemViewType is not choosing any valid type and returns -1.");
        return -1;
    }

    // Used to cache the views within the item layout for fast access.
    public class ViewHolderSample extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public PieChart pcPercent;
        public TextView tvPercent;  //TODO: replace with a piechart
        public ImageView ivPlayPause;
        public ImageView ivIcon;
        public ProgressBar pbLoadingIndicator;
        public long millisecondsPerSection;

        // Constructor that accepts the entire item row
        // and does the view lookups to find each subview.
        public ViewHolderSample(View itemView) {
            super(itemView);

            ((SoundSampleView)itemView).viewHolder = this;

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            pcPercent = (PieChart) itemView.findViewById(R.id.pcPercent);
            tvPercent = (TextView) itemView.findViewById(R.id.tvPercent);
            ivPlayPause = (ImageView) itemView.findViewById(R.id.ivPlayPause);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            pbLoadingIndicator = (ProgressBar) itemView.findViewById(R.id.pbLoadingIndicator);

            // Set listener on the `play/pause` button.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (soundSamplePlayListener != null && getSoundSampleInstance().isLoaded()) {
                        millisecondsPerSection = soundSamplePlayListener.playButtonPressed(getSoundSampleInstance());
                    }
                }
            });

            // Set up the dataset on the piechart
        }

        public SoundSampleInstance getSoundSampleInstance() {
            return mSamples.get(getAdapterPosition());
        }

        // Sets the sound sample information into their respective views.
        public void bind(SoundSampleInstance soundSample) {
            tvTitle.setText(soundSample.getSoundSample().getName());
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
                    //TODO: Update this

//                    int position = getAdapterPosition();
//                    SoundSample soundSample = new SoundSample("Fresh Sample", 0, null, 0, 0, null);
//                    SoundSampleInstance soundSampleInstance = new SoundSampleInstance(soundSample, null, getContext(), );
//                    mSamples.set(position, soundSampleInstance);
//                    notifyDataSetChanged();
                }
            });
        }

        // Sets the sound sample information into their respective views.
        public void bind() {

        }
    }
}
