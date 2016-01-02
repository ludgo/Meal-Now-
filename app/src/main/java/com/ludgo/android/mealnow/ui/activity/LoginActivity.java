package com.ludgo.android.mealnow.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.ludgo.android.mealnow.R;
import com.ludgo.android.mealnow.model.MyInfo;
import com.ludgo.android.mealnow.ui.fragment.LoginChoiceFragment;
import com.ludgo.android.mealnow.util.Constants;
import com.ludgo.android.mealnow.util.Utilities;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class LoginActivity extends ProviderLoginActivity {

    private static final String LOG_TAG = SignInActivity.class.getSimpleName();

    private static final String LOGIN_CHOICE_FRAGMENT_TAG = "LCF_TAG";

    private String mUserName;
    private String mUserEmail;
    private String mUserPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            replaceFragment(LOGIN_CHOICE_FRAGMENT_TAG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // This ID represents the Home or Up button.
                // Ensure that 'toolbar back' behaves just like 'bottom back'
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                mGoogleId = acct.getId();
                mUserName = acct.getDisplayName();
                mUserEmail = acct.getEmail();
                Uri userPhotoUrl = acct.getPhotoUrl();
                mUserPicture = (userPhotoUrl == null) ? Constants.USER_PICTURE_DEFAULT : userPhotoUrl.toString();
                mGoogleToken = acct.getIdToken();

                serverLogin(Constants.AUTH_PROVIDER_GOOGLE);
            }
        }
    }

    /**
     * Choose a fragment class to display
     *
     * @param tag is one from above tags
     */
    private void replaceFragment(String tag) {

        if (tag.equals(LOGIN_CHOICE_FRAGMENT_TAG)) {
            LoginChoiceFragment fragment = new LoginChoiceFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, fragment, LOGIN_CHOICE_FRAGMENT_TAG)
                    .commit();
        }
    }

    /**
     * Initialize authentication
     */
    public void signIn(@NonNull String provider) {

        if (provider.equals(Constants.AUTH_PROVIDER_GOOGLE)) {
            googleSignIn();;
        }
    }

    /**
     * Authorize user at server
     *
     * @param provider is the segment of url specified by server api
     */
    private void serverLogin(@NonNull String provider) {

        try {
            URL url = new URL(Utilities.buildLoginUrl(provider));

            JSONObject object = new JSONObject();
            object.put("client_type", "android");
            object.put("user_name", mUserName);
            object.put("user_email", mUserEmail);
            object.put("user_picture", mUserPicture);
            if (provider.equals(Constants.AUTH_PROVIDER_GOOGLE)) {
                object.put("token_id", mGoogleToken);
            }

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(Constants.OKHTTP_TYPE_JSON, object.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Log.d(Constants.OKHTTP_TAG, Constants.OKHTTP_REQUEST_TAG + request.toString());
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(Constants.OKHTTP_TAG, "Server not responding.");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        Log.d(Constants.OKHTTP_TAG, Constants.OKHTTP_RESPONSE_TAG + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            storeMyInfo(response.body().string());
                        }
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error encoding server's response.");
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(Constants.OKHTTP_TAG, "Error encoding server's response.");
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error sending request to server.");
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error sending request to server.");
        }
    }

    /**
     * Encode and store retrieved {@link MyInfo} in the database
     */
    private void storeMyInfo(String jsonString)
            throws JSONException {

        try {
            JSONObject object = new JSONObject(jsonString);

            final String NAME_SERVER_TOKEN = "token";
            final String NAME_USER = "user";
            final String NAME_USER_ID = "id";
            final String NAME_USER_NAME = "name";
            final String NAME_USER_EMAIL = "email";
            final String NAME_USER_PICTURE = "picture";

            String serverToken;
            int userId = -1;
            String userName;
            String userEmail;
            String userPicture;

            serverToken = object.getString(NAME_SERVER_TOKEN);
            JSONObject userObject = object.getJSONObject(NAME_USER);
            userId = userObject.getInt(NAME_USER_ID);
            userName = userObject.getString(NAME_USER_NAME);
            userEmail = userObject.getString(NAME_USER_EMAIL);
            userPicture = userObject.getString(NAME_USER_PICTURE);

            if (userId != -1
                    && userName != null
                    && userEmail != null
                    && userPicture != null
                    && serverToken != null
                    && mGoogleId != null) {

                MyInfo myInfo = new MyInfo(
                        userId,
                        serverToken,
                        userName,
                        userEmail,
                        userPicture,
                        mGoogleId
                );
                myInfo.save();

                // At this point, user login has entirely & successfully ended
                Utilities.toMainActivity(this);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error encoding server's response.");
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
