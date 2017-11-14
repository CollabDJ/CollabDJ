package com.codepath.collabdj.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codepath.collabdj.R;
import com.codepath.collabdj.fragments.DiscoveringConnectionDialogFragment;
import com.codepath.collabdj.utils.NearbyConnection;

public class JoinSessionActivity extends AppCompatActivity {

    private static final String TAG = JoinSessionActivity.class.getSimpleName();

    private NearbyConnection mNearbyConnection;

    private OnConnectionEstablishedListener listener;

    public interface OnConnectionEstablishedListener {
        public void onConnectionEstablished();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_session);

        setupNearbyConnection();

        showDiscoveringConnectionDialog();
        listener = null;

    }


    @Override
    protected void onStart() {
        if (mNearbyConnection.hasPermissions()) {
            Log.v(TAG, "The app has the required permissions.");
            mNearbyConnection.createGoogleApiClient();
        } else {
            Log.v(TAG, "Requesting the permissions to use NearbyApi.");
            mNearbyConnection.requestPermissions();
        }
        super.onStart();
    }



    /** The user has accepted (or denied) our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mNearbyConnection.getRequestCodeRequiredPermissions()) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Log.v(TAG, "Couldn't get the permissions!");
                    finish();
                    return;
                }
            }
            recreate();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupNearbyConnection() {
        mNearbyConnection = new NearbyConnection(this, this);
        mNearbyConnection.setNearbyConnectionListener(new NearbyConnection.NearbyConnectionListener() {
            @Override
            public void sendCurrentSong() {
                // Do nothing!
            }

            @Override
            public void receiveCurrentSong(String song) {

                // Show the received info.
                TextView tvTest = (TextView) findViewById(R.id.tvTest);
                tvTest.setText(song);
                //Toast.makeText(JoinSessionActivity.this, song, Toast.LENGTH_LONG).show();
            }

            @Override
            public void receiveNewSample(String sample) {

                // Show the received info.
                TextView tvTest = (TextView) findViewById(R.id.tvTest);
                tvTest.setText(sample);
                //Toast.makeText(JoinSessionActivity.this, sample, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showDiscoveringConnectionDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DiscoveringConnectionDialogFragment discoveringConnectionDialogFragment = DiscoveringConnectionDialogFragment.newInstance(getString(R.string.looking_for_session_nearby));
        discoveringConnectionDialogFragment.show(fm, "fragment_discovering_connection");
    }


    public void setOnConnectionEstablishedListener(OnConnectionEstablishedListener listener) {
        this.listener = listener;
    }

    public void connectionEstablished() {
        if (listener != null) {
            listener.onConnectionEstablished();
        }
    }
}































