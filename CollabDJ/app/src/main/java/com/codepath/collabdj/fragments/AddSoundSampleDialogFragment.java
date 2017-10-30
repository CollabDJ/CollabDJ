package com.codepath.collabdj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.collabdj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 10/23/17.
 */

public class AddSoundSampleDialogFragment extends DialogFragment {

    private EditText mEditText;
    private ListView lvAddSamples;
    private List<String> mItems;


    public AddSoundSampleDialogFragment() {
        // Empty constructor required by DialogFragment.
    }

    public static AddSoundSampleDialogFragment newInstance(String title) {
        AddSoundSampleDialogFragment frag = new AddSoundSampleDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
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
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        mEditText.requestFocus();

        mItems = new ArrayList<>();
        mItems.add("Hello!");
        mItems.add("World!");
        mItems.add("Me too!");
        lvAddSamples = (ListView) view.findViewById(R.id.lvAddSample);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mItems);
        lvAddSamples.setAdapter(itemsAdapter);
    }

}
