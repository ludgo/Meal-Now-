package com.ludgo.android.mealnow.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ludgo.android.mealnow.BuildConfig;

public abstract class ProviderLoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = ProviderLoginActivity.class.getSimpleName();

    // Google sign in variables
    public static final int REQUEST_CODE_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInOptions mGoogleSignInOptions;
    public GoogleApiClient mGoogleApiClient;
    public String mGoogleId;
    public String mGoogleToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = buildGoogleApiClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // [START Google sign in methods]

    public GoogleSignInOptions getGoogleSignInOptions() {
        return mGoogleSignInOptions;
    }

    /**
     * Build common Google api client,
     * here scopes that will be demanded are defined
     */
    public GoogleApiClient buildGoogleApiClient(Context context) {

        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by GoogleSignInOptions.
        return new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Client is prepared to call available Google apis
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
        Log.d(LOG_TAG, "Connection to Google Play Services Suspended: Reconnecting..");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e(LOG_TAG, "Connection to Google Play Services Failed with ConnectionResult: " + connectionResult);
    }

    /**
     * Initialize authentication at Google Auth Api
     */
    public void googleSignIn() {
        if (!mGoogleApiClient.isConnecting()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
            // This will result to onActivityResult
        }
    }
    // [END Google sign in methods]
}
