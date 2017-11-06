package com.codepath.collabdj.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SoundSample;

import java.util.List;

/**
 * Created by tiago on 10/31/17.
 */

public class ChooseSoundSamplesAdapter extends ArrayAdapter<SoundSample> {

    Context mContext;
    List<SoundSample> mSoundSamples;

    public ChooseSoundSamplesAdapter(Context context, List<SoundSample> samples) {
        super(context, 0, samples);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundSample soundSample = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_choose_sound_sample, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSoundSampleTitle);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvSoundSampleDesc);
        TextView tvDuration = (TextView) convertView.findViewById(R.id.tvSoundSampleDuration);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.ivSoundSamplePic);

        tvTitle.setText(soundSample.getName());

        int iconResource = soundSample.getIconDrawableId();

        ivPic.setImageResource(iconResource == 0 ? R.drawable.ic_notes_glowing : iconResource);

        return convertView;
    }
}
