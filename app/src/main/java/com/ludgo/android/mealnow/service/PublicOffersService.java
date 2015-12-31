package com.ludgo.android.mealnow.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.ludgo.android.mealnow.model.PublicOffer;
import com.ludgo.android.mealnow.util.Constants;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Fetch {@link PublicOffer}s from server
 * on demand from {@link com.ludgo.android.mealnow.ui.PublicTabFragment}
 */
public class PublicOffersService extends IntentService {

    private final String LOG_TAG = PublicOffersService.class.getSimpleName();

    public PublicOffersService() {
        super("PublicOffersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Uri build = Uri.parse(Constants.API_V1_OFFERS).buildUpon()
                    .appendQueryParameter("page", "1")
                    .build();
            URL url = new URL(build.toString());

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type", Constants.OKHTTP_HEADER_JSON)
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
                        if (response.code() == 200) {
                            storePublicOffers(response.body().string());
                        }
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error encoding server's response.");
                        Log.e(LOG_TAG, e.getMessage(), e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error encoding server's response.");
                    }
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error sending request to server.");
        }
    }

    /**
     * Encode and store retrieved {@link PublicOffer}s in the database
     */
    private void storePublicOffers(String jsonString)
            throws JSONException {

        try {
            JSONObject object = new JSONObject(jsonString);

            final String NAME_OFFERS = "offers";
            JSONArray offersArray = object.getJSONArray(NAME_OFFERS);

            final String NAME_OFFER = "offer";
            final String NAME_OFFER_ID = "id";
            final String NAME_OFFER_TIME_CREATED = "time_created";
            final String NAME_OFFER_MEAL = "meal";
            final String NAME_OFFER_LOCATION = "location";
            final String NAME_OFFER_LATITUDE = "latitude";
            final String NAME_OFFER_LONGITUDE = "longitude";
            final String NAME_OFFER_FILLED = "filled";
            final String NAME_USER = "user";
            final String NAME_USER_ID = "id";
            final String NAME_USER_NAME = "name";
            final String NAME_USER_EMAIL = "email";
            final String NAME_USER_PICTURE = "picture";

            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < offersArray.length(); i++) {

                    JSONObject anOffer = offersArray.getJSONObject(i);
                    JSONObject offerPart = anOffer.getJSONObject(NAME_OFFER);
                    JSONObject userPart = anOffer.getJSONObject(NAME_USER);

                    int offerId = -1;
                    String offerTimeCreated;
                    String offerMeal;
                    String offerLocation;
                    double offerLatitude;
                    double offerLongitude;
                    int offerFilled = -1;
                    int userId = -1;
                    String userName;
                    String userEmail;
                    String userPicture;

                    offerId = offerPart.getInt(NAME_OFFER_ID);
                    offerTimeCreated = offerPart.getString(NAME_OFFER_TIME_CREATED);
                    offerMeal = offerPart.getString(NAME_OFFER_MEAL);
                    offerLocation = offerPart.getString(NAME_OFFER_LOCATION);
                    offerLatitude = offerPart.getDouble(NAME_OFFER_LATITUDE);
                    offerLongitude = offerPart.getDouble(NAME_OFFER_LONGITUDE);
                    offerFilled = offerPart.getInt(NAME_OFFER_FILLED);
                    userId = userPart.getInt(NAME_USER_ID);
                    userName = userPart.getString(NAME_USER_NAME);
                    userEmail = userPart.getString(NAME_USER_EMAIL);
                    userPicture = userPart.getString(NAME_USER_PICTURE);

                    if (offerId != -1
                            && offerTimeCreated != null
                            && offerMeal != null
                            && offerLocation != null
                            && offerFilled != -1
                            && userId != -1
                            && userName != null
                            && userEmail != null
                            && userPicture != null) {

                        PublicOffer publicOffer = new PublicOffer(
                                offerId,
                                offerTimeCreated,
                                offerMeal,
                                offerLocation,
                                offerLatitude,
                                offerLongitude,
                                offerFilled,
                                userId,
                                userName,
                                userEmail,
                                userPicture
                        );
                        publicOffer.save();
                    }
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error encoding server's response.");
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
