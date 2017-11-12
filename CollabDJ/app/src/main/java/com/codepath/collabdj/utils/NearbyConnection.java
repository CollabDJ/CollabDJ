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
import android.widget.Toast;

import com.codepath.collabdj.activities.CreateSongActivity;
import com.codepath.collabdj.activities.OpenSongsActivity;
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

import java.io.UnsupportedEncodingException;

/**
 * Created by tiago on 11/12/17.
 */

public class NearbyConnection implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // This is how this file is organized:
    // First I declare all the members, there are a lot of them.
    // After that, the constructor.
    // Following, the methods used to advertise/discover.
    // Finally, the methods used to send/receive data.

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

                        // Send data to the discoverer.
                        // TODO We should send data to every discoverer connected to us. For demo we are gonna have 1 I guess, so it's fine.
                        sendDataToDiscoverer(endpointId);
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
                        Toast.makeText(mContext, "Finallly connected!", Toast.LENGTH_SHORT).show();

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
            startAdvertising();
        } else if (mActivity instanceof OpenSongsActivity) {
            startDiscovering();
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
                        Toast.makeText(mContext, "Endpoint Found!", Toast.LENGTH_LONG).show();

                        if (SERVICE_ID.equals(info.getServiceId())) {
                            //Endpoint endpoint = Endpoint(endpointId, info.getEndpointName());
                            //mDiscoveredEndpoints.put(endpointId, endpoint);
                            //onEndpointDiscovered(endpoint);

                            // Ask to connect
                            Nearby.Connections.requestConnection(
                                    mGoogleApiClient, "Isabelle", endpointId, mConnectionLifecycleCallbackDiscoverer)
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
                                    Toast.makeText(mContext, "Estamos descubriendo!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(mContext, "Conectadooooo!!!", Toast.LENGTH_SHORT).show();
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
        String payloadMessage = "String mandado por Nearby!";
        try {
            // Send this message as a bytes payload.
            Nearby.Connections.sendPayload(mGoogleApiClient,
                    endpointId,
                    Payload.fromBytes(payloadMessage.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    // Used by an advertiser to send data to a discoverer.
    private void sendDataToDiscoverer(String endpointId) {
        // Do nothing, for now.
    }


    // Used by an advertiser to receive data from a discoverer.
    private void receiveDataFromDiscoverer(String endpointId, Payload payload) {

        String payloadFilenameMessage = "";
        try {
            payloadFilenameMessage = new String(payload.asBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // SHOW THE RECEIVED INFO!!!
        Toast.makeText(mContext, payloadFilenameMessage, Toast.LENGTH_SHORT).show();
    }


    // Used by a discoverer to receive data from an advertiser.
    private void receiveDataFromAdvertiser(String endpointId, Payload payload) {
        // Do nothing, for now.
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

}
