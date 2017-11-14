package com.codepath.collabdj.fragments;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.JoinSessionActivity;

/**
 * Created by tiago on 11/12/17.
 */

public class DiscoveringConnectionDialogFragment extends DialogFragment {

    private ProgressBar pbConnecting;
    private TextSwitcher tsConnectionStatus;


    public DiscoveringConnectionDialogFragment() {
        // Required empty constructor for DialogFragment.
    }

    public static DiscoveringConnectionDialogFragment newInstance(String status) {
        DiscoveringConnectionDialogFragment frag = new DiscoveringConnectionDialogFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovering_connection, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupTextSwitcher(view);

        // Fetch arguments from bundle and set status.
        String status = getArguments().getString("status");
        tsConnectionStatus.setText(status);

        pbConnecting = (ProgressBar) view.findViewById(R.id.pbConnecting);
        pbConnecting.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white), PorterDuff.Mode.SRC_IN );
        pbConnecting.setVisibility(ProgressBar.VISIBLE);

        // Listener to dismiss the dialogFragment when the connection is established.
        ((JoinSessionActivity)getActivity()).setOnConnectionEstablishedListener(new JoinSessionActivity.OnConnectionEstablishedListener() {
            @Override
            public void onConnectionEstablished() {
                dismiss();
            }
        });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Make the dialogFragment fullscreen.
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    private void setupTextSwitcher(View view) {

        // Get field from view.
        tsConnectionStatus = (TextSwitcher) view.findViewById(R.id.tsConnectionStatus);
        tsConnectionStatus.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tvStatusText = new TextView(getContext());
                tvStatusText.setText(getString(R.string.player_status_loading));
                tvStatusText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvStatusText.setTypeface(tvStatusText.getTypeface(), Typeface.BOLD);
                return tvStatusText;
            }
        });
        tsConnectionStatus.setInAnimation(getContext(), android.R.anim.fade_in);
        tsConnectionStatus.setOutAnimation(getContext(), android.R.anim.fade_out);

    }

}







































