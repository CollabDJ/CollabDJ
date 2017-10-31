package com.codepath.collabdj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.models.SoundSample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by tiago on 10/23/17.
 */

public class AddSoundSampleDialogFragment extends DialogFragment {

    private ListView lvAddSamples;
    private List<String> mItems;

    public interface AddSoundSampleDialogListener {
        void onSoundSampleAdded(String title);
    }


    public AddSoundSampleDialogFragment() {
        // Empty constructor required by DialogFragment.
    }

    public static AddSoundSampleDialogFragment newInstance() {
        AddSoundSampleDialogFragment frag = new AddSoundSampleDialogFragment();
        Bundle args = new Bundle();
        //args.putSerializable("soundSampleMap", soundSampleMap);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_sound_sample, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Map<String, SoundSample> map = SoundSample.SOUND_SAMPLES;
        mItems = new ArrayList<>();
        for (Map.Entry<String, SoundSample> entry : map.entrySet()) {
            mItems.add(entry.getKey());
        }

        //alphabetically sort the samples
        mItems.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        lvAddSamples = (ListView) view.findViewById(R.id.lvAddSample);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mItems);
        lvAddSamples.setAdapter(itemsAdapter);

        lvAddSamples.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);

                AddSoundSampleDialogListener listener = (AddSoundSampleDialogListener) getActivity();
                listener.onSoundSampleAdded(item);

                dismiss();
            }
        });
    }

}
