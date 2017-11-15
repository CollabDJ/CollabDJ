package com.codepath.collabdj.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.collabdj.R;
import com.codepath.collabdj.activities.CreateSongActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by tiago on 11/12/17.
 */

public class NearbyConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // This is how this file is organized:
    // Members of the class, there are a lot of them.
    // After that, the constructor.
    // Interface to communicate with CreateSongActivity and JoinSessionActivity.
    // Methods used to check the permissions.
    // Methods used to advertise/discover.
    // Methods used to send/receive data.

    private static final String TAG = NearbyConnection.class.getSimpleName();

    private Context mContext;
    private AppCompatActivity mActivity;

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    /**
     * The connection strategy we'll use for Nearby Connections. In this case, we've decided on
     * P2P_STAR, which is a combination of Bluetooth Classic and WiFi Hotspots.
     */
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    /** We'll talk to Nearby Connections through the GoogleApiClient. */
    private GoogleApiClient mGoogleApiClient;

    /*
     * This variable indicates if the connection is in a state where it's able to send/receive data.
     * This assumes that there is going to be only a 1-1 connection. Probably enough for demo purposes.
     */
    private boolean mConnectionEstablished;

    /*
     * This variable indicates if the current song was sent. It's used to filter the type of message.
     * Fix in the future.
     */
    private boolean mWasCurrentSongSent;

    /*
     * Stores the Id of the device I'm connected to. Again, done this way for demo purposes.
     */
    private String mEndpointId;
    private String mEndpointName;

    /*
     * This variable represents the listener passed in by the owning object.
     * Passes messages up to the parent.
     */
    private NearbyConnectionListener listener;

    private static final String SERVICE_ID = "com.codepath.tiago.nearbytest";

    /** Callbacks for connections to other devices. Used by the Advertiser. */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallbackAdvertiser =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.v(TAG,
                            String.format(
                                    "onConnectionInitiated(endpointId=%s, endpointName=%s) executed.",
                                    endpointId, connectionInfo.getEndpointName()));
                    Log.v(TAG, "We are going to accept the incoming connection.");
                    //Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                    //mPendingConnections.put(endpointId, endpoint);
                    //StartSongActivity.this.onConnectionInitiated(endpoint, connectionInfo);
                    mEndpointName = connectionInfo.getEndpointName();
                    advertiserAcceptsIncomingConnection(endpointId);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    Log.v(TAG, String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result));

                    // We're no longer connecting
                    //mIsConnecting = false;

                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG,
                                String.format(
                                        "onConnectionResult() - Connection failed."));
                        //onConnectionFailed(mPendingConnections.remove(endpointId));
                        return;
                    }
                    //connectedToEndpoint(mPendingConnections.remove(endpointId));


                    if (result.getStatus().isSuccess()) {
                        Log.v(TAG, "onConnectionResult() - Connection succeded. We can now send/receive data.");

                        // Set up flags to indicate the state of the connection.
                        mConnectionEstablished = true;
                        mEndpointId = endpointId;

                        // Send data to the discoverer.
                        // TODO We should send data to every discoverer connected to us. For demo we are gonna have 1 I guess, so it's fine.
                        // Send all the song info.
                        /*if (listener != null) {
                            listener.sendCurrentSong();
                        }
                        */

                        //For demo purposes just assume they'll be in the same state on startup

                        //sendDataToDiscoverer(endpointId);
                        //Toast.makeText(mContext, mEndpointName + " just joined the session!", Toast.LENGTH_SHORT).show();
                        ((CreateSongActivity)mActivity).showFabEndpoint();
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    //if (!mEstablishedConnections.containsKey(endpointId)) {
                    //    Log.v(TAG, "Unexpected disconnection from endpoint " + endpointId);
                    //    return;
                    //}
                    //disconnectedFromEndpoint(mEstablishedConnections.get(endpointId));
                }
            };


    /** Callbacks for payloads (bytes of data) sent from another device to us. Used by the advertiser. */
    private final PayloadCallback mPayloadCallbackAdvertiser =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    Log.d(TAG, String.format("onPayloadReceived(endpointId=%s, payload=%s)", endpointId, payload));
                    //onReceive(mEstablishedConnections.get(endpointId), payload);

                    receiveDataFromDiscoverer(endpointId, payload);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    Log.d(TAG,
                            String.format(
                                    "onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId, update));
                }
            };



    /** Callbacks for connections to other devices. Use by the discoverer. */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallbackDiscoverer =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.v(TAG,
                            String.format(
                                    "onConnectionInitiated(endpointId=%s, endpointName=%s)",
                                    endpointId, connectionInfo.getEndpointName()));
                    //Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                    //mPendingConnections.put(endpointId, endpoint);
                    //StartSongActivity.this.onConnectionInitiated(endpoint, connectionInfo);
                    discovererAcceptsIncomingConnection(endpointId);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    Log.v(TAG, String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result));

                    // We're no longer connecting
                    //mIsConnecting = false;

                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG,
                                String.format(
                                        "Connection failed. Received status."));
                        //onConnectionFailed(mPendingConnections.remove(endpointId));
                        return;
                    }
                    //connectedToEndpoint(mPendingConnections.remove(endpointId));

                    if (result.getStatus().isSuccess()) {
                        //Toast.makeText(mContext, "Finallly connected!", Toast.LENGTH_SHORT).show();

                        // Set up flags to indicate the state of the connection.
                        mConnectionEstablished = true;
                        mEndpointId = endpointId;

                        // Dismiss dialogFragment.
                        ((CreateSongActivity)mActivity).connectionEstablished();

                        // Show FAB.
                        ((CreateSongActivity)mActivity).showFabEndpoint();

                        // Send data to the advertiser.
                        sendDataToAdvertiser(endpointId);
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    //if (!mEstablishedConnections.containsKey(endpointId)) {
                    //    Log.v(TAG, "Unexpected disconnection from endpoint " + endpointId);
                    //    return;
                    //}
                    //disconnectedFromEndpoint(mEstablishedConnections.get(endpointId));
                }
            };


    /** Callbacks for payloads (bytes of data) sent from another device to us. Used by the discoverer. */
    private final PayloadCallback mPayloadCallbackDiscoverer =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    Log.d(TAG, String.format("onPayloadReceived(endpointId=%s, payload=%s)", endpointId, payload));
                    //onReceive(mEstablishedConnections.get(endpointId), payload);

                    receiveDataFromAdvertiser(endpointId, payload);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    Log.d(TAG,
                            String.format(
                                    "onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId, update));
                }
            };




    // Constructor.
    public NearbyConnection(AppCompatActivity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;
        this.mConnectionEstablished = false;
        this.mWasCurrentSongSent = false;
        this.listener = null;
    }


    // Interface that defines methods to communicate with parent classes.
    public interface NearbyConnectionListener {
        void receiveAddSample(int sampleIndex, String sampleName);

        void receivePlaySample(int sampleIndex, long sectionIndex);

        void receiveStopSample(int sampleIndex);
    }

    private static final String MESSAGE_TYPE = "message_type";

    private static final String ADD_SAMPLE_MESSAGE = "add_sample";
    private static final String PLAY_SAMPLE_MESSAGE = "play_sample";
    private static final String STOP_SAMPLE_MESSAGE = "stop_sample";

    private static final String SAMPLE_INDEX = "sample_index";
    private static final String SAMPLE_NAME = "sample_name";
    private static final String SECTION_INDEX = "section_index";

    public void sendAddSample(int sampleIndex, String sampleName) {
        JSONObject data = new JSONObject();

        try {
            data.put(MESSAGE_TYPE, ADD_SAMPLE_MESSAGE);
            data.put(SAMPLE_INDEX, sampleIndex);
            data.put(SAMPLE_NAME, sampleName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendData(data.toString());
    }

    public void sendPlaySample(int sampleIndex, long sectionIndex) {
        JSONObject data = new JSONObject();

        try {
            data.put(MESSAGE_TYPE, PLAY_SAMPLE_MESSAGE);
            data.put(SAMPLE_INDEX, sampleIndex);
            data.put(SECTION_INDEX, sectionIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendData(data.toString());
    }

    public void sendStopSample(int sampleIndex) {
        JSONObject data = new JSONObject();

        try {
            data.put(MESSAGE_TYPE, STOP_SAMPLE_MESSAGE);
            data.put(SAMPLE_INDEX, sampleIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendData(data.toString());
    }

    // Sets the listener.
    public void setNearbyConnectionListener(NearbyConnectionListener listener) {
        this.listener = listener;
    }



    /**
     * An optional hook to pool any permissions the app needs with the permissions ConnectionsActivity
     * will request.
     *
     * @return All permissions required for the app to properly function.
     */
    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }



    public boolean hasPermissions() {
        return hasPermissions(mContext, getRequiredPermissions());
    }




    /** @return True if the app was granted all the permissions. False otherwise. */
    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }




    public void requestPermissions() {
        ActivityCompat.requestPermissions(mActivity, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSIONS);
    }




    public int getRequestCodeRequiredPermissions() {
        return REQUEST_CODE_REQUIRED_PERMISSIONS;
    }



    // Setup the GoogleApiClient.
    public void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient =
                    new GoogleApiClient.Builder(mContext)
                            .addApi(Nearby.CONNECTIONS_API)
                            .addConnectionCallbacks(this)
                            .enableAutoManage(mActivity, this)
                            .build();
            Log.v(TAG, "GoogleApiClient built.");
        }
    }


    /** We've connected to Nearby Connections' GoogleApiClient. */
    // Callback indicates that the client is ready to be used. Now we can perform the operations that need a working GoogleApiClient.
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "GoogleApiClient onConnected() executed. We are gonna start advertising/discovering.");

        if (mActivity instanceof CreateSongActivity) {
            CreateSongActivity createSongActivity = (CreateSongActivity) mActivity;

            if (createSongActivity.isHost) {
                startAdvertising();
            } else {
                startDiscovering();
            }
        } else {
            Log.v(TAG, "ERROR: in onConnected() - instanceof couldn't identify the activity.");
        }
    }

    // Starts advertising, discoverers are going to be able to connect to us.
    private void startAdvertising() {
        Nearby.Connections.startAdvertising(
                mGoogleApiClient,
                "Tiago",
                SERVICE_ID,
                mConnectionLifecycleCallbackAdvertiser,
                new AdvertisingOptions(STRATEGY))
                .setResultCallback(
                        new ResultCallback<Connections.StartAdvertisingResult>() {
                            @Override
                            public void onResult(@NonNull Connections.StartAdvertisingResult result) {
                                if (result.getStatus().isSuccess()) {
                                    Log.v(TAG, "Advertising succeded.");
                                    Log.v(TAG, "Now advertising endpoint " + result.getLocalEndpointName());
                                    //onAdvertisingStarted();
                                } else {
                                    //mIsAdvertising = false;
                                    Log.v(TAG,
                                            String.format(
                                                    "Advertising failed."
                                            ));
                                    //onAdvertisingFailed();
                                }
                            }
                        });
    }


    // Starts discovering, we can find advertisers nearby. After we find one we can connect to them.
    private void startDiscovering() {
        Nearby.Connections.startDiscovery(
                mGoogleApiClient,
                SERVICE_ID,
                new EndpointDiscoveryCallback() {
                    @Override
                    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                        Log.d(TAG,
                                String.format(
                                        "onEndpointFound(endpointId=%s, serviceId=%s, endpointName=%s)",
                                        endpointId, info.getServiceId(), info.getEndpointName()));
                        //Toast.makeText(mContext, "Endpoint Found!", Toast.LENGTH_LONG).show();
                        // Found an endpoint (Device).
                        ((CreateSongActivity) mActivity).updateConnectionStatus(mContext.getString(R.string.session_found));

                        if (SERVICE_ID.equals(info.getServiceId())) {
                            //Endpoint endpoint = Endpoint(endpointId, info.getEndpointName());
                            //mDiscoveredEndpoints.put(endpointId, endpoint);
                            //onEndpointDiscovered(endpoint);

                            // Ask to connect
                            Nearby.Connections.requestConnection(
                                    mGoogleApiClient, "Codepath", endpointId, mConnectionLifecycleCallbackDiscoverer)
                                    .setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(@NonNull Status status) {
                                                    if (!status.isSuccess()) {
                                                        Log.v(TAG,
                                                                String.format(
                                                                        "requestConnection failed."));
                                                        //mIsConnecting = false;
                                                        //onConnectionFailed(endpoint);
                                                    }
                                                }
                                            });
                        }
                    }

                    @Override
                    public void onEndpointLost(String endpointId) {
                        Log.d(TAG, String.format("onEndpointLost(endpointId=%s)", endpointId));
                    }
                },
                new DiscoveryOptions(STRATEGY))
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    //onDiscoveryStarted();
                                    //Toast.makeText(mContext, "We are discovering!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //mIsDiscovering = false;
                                    Log.w(TAG,
                                            String.format(
                                                    "Discovering failed. Received status."));
                                    //onDiscoveryFailed();
                                }
                            }
                        });
    }


    // I'm an advertiser and I'm receiving a connection, I accept it.
    private void advertiserAcceptsIncomingConnection(String endpointId) {
        Nearby.Connections.acceptConnection(mGoogleApiClient, endpointId, mPayloadCallbackAdvertiser)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (!status.isSuccess()) {
                                    Log.v(TAG,
                                            String.format(
                                                    "acceptConnection failed."));
                                } else {
                                    Log.v(TAG,
                                            String.format(
                                                    "acceptConnection succeded!."));
                                }
                            }
                        });
    }



    // I'm a discoverer and I'm receiving a connection, I accept it.
    private void discovererAcceptsIncomingConnection(String endpointId) {
        Nearby.Connections.acceptConnection(mGoogleApiClient, endpointId, mPayloadCallbackDiscoverer)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (!status.isSuccess()) {
                                    Log.v(TAG,
                                            String.format(
                                                    "acceptConnection failed."));
                                } else {
                                    Log.v(TAG,
                                            String.format(
                                                    "acceptConnection succeded!."));
                                    //Toast.makeText(mContext, "Conectadooooo!!!", Toast.LENGTH_SHORT).show();
                                    // We are connected.
                                    ((CreateSongActivity) mActivity).updateConnectionStatus(mContext.getString(R.string.connection_established));
                                }
                            }
                        });
    }



    /** We've been temporarily disconnected from Nearby Connections' GoogleApiClient. */
    // Nothing important for now.
    @CallSuper
    @Override
    public void onConnectionSuspended(int reason) {
        Log.w(TAG, String.format("onConnectionSuspended(reason=%s)", reason));
        //resetState();
    }

    /** We are unable to connect to Nearby Connections' GoogleApiClient. Oh uh. */
    // Nothing important for now.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG,
                String.format(
                        "onConnectionFailed()"));
    }


    // Used by a discoverer to send data to an advertiser.
    private void sendDataToAdvertiser(String endpointId) {
        /*
        String payloadMessage = "String mandado por Nearby!";
        try {
            // Send this message as a bytes payload.
            Nearby.Connections.sendPayload(mGoogleApiClient,
                    endpointId,
                    Payload.fromBytes(payloadMessage.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */
    }



    // Used by an advertiser to send data to a discoverer.
    private void sendDataToDiscoverer(String endpointId) {

        /*
        try {
            Payload payload = Payload.fromBytes(data.toString().getBytes("UTF-8"));
            Nearby.Connections.sendPayload(mGoogleApiClient, endpointId, payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */
    }


    // Used by an advertiser to receive data from a discoverer.
    private void receiveDataFromDiscoverer(String endpointId, Payload payload) {
        receiveData(payload);
    }


    // Used by a discoverer to receive data from an advertiser.
    private void receiveDataFromAdvertiser(String endpointId, Payload payload) {
        receiveData(payload);
    }

    void receiveData(Payload payload) {
        String payloadMessage = "";
        try {
            payloadMessage = new String(payload.asBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(payloadMessage);

            String messageType = jsonObject.getString(MESSAGE_TYPE);

            if (messageType.equals(ADD_SAMPLE_MESSAGE)) {
                int sampleIndex = jsonObject.getInt(SAMPLE_INDEX);
                String sampleName = jsonObject.getString(SAMPLE_NAME);

                listener.receiveAddSample(sampleIndex, sampleName);
            }
            else if (messageType.equals(PLAY_SAMPLE_MESSAGE)) {
                int sampleIndex = jsonObject.getInt(SAMPLE_INDEX);
                long sectionIndex = jsonObject.getLong(SECTION_INDEX);

                listener.receivePlaySample(sampleIndex, sectionIndex);
            }
            else if (messageType.equals(STOP_SAMPLE_MESSAGE)) {
                int sampleIndex = jsonObject.getInt(SAMPLE_INDEX);

                listener.receiveStopSample(sampleIndex);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void sendData(String data) {
        // Check if the connection is in the proper state to send/receive data.
        if (mConnectionEstablished) {

            Payload payload;
            try {
                payload = Payload.fromBytes(data.getBytes("UTF-8"));
                Nearby.Connections.sendPayload(mGoogleApiClient, mEndpointId, payload);
            } catch (UnsupportedEncodingException e) {
                Log.v(TAG, "public sendData - Couldn't send data. Exception raised");
                e.printStackTrace();
            }

        } else {
            Log.v(TAG, "public sendData - connection in wrong state, can't send data.");
        }

    }


    public void sendDataToAdvertiser() {
        // TODO implement
    }

    public String getEndpointName() {
        return mEndpointName;
    }

}
