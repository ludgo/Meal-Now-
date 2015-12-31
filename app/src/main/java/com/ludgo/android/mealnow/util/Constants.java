package com.ludgo.android.mealnow.util;


import com.ludgo.android.mealnow.BuildConfig;
import com.squareup.okhttp.MediaType;

public final class Constants {

    public static final String OKHTTP_TAG = "OkHttp";
    public static final String OKHTTP_REQUEST_TAG = "---> ";
    public static final String OKHTTP_RESPONSE_TAG = "<--- ";
    public static final String OKHTTP_HEADER_JSON = "application/json; charset=utf-8";
    public static final MediaType OKHTTP_TYPE_JSON = MediaType.parse(OKHTTP_HEADER_JSON);

    public static final String API_URL_BASE = BuildConfig.SERVER_BASE_URL;
    public static final String API_V1_URL = API_URL_BASE + "/api/v1";
    public static final String API_V1_LOGIN_GOOGLE = API_V1_URL + "/google/login";
    public static final String API_V1_LOGOUT_GOOGLE = API_V1_URL + "/google/logout";
    public static final String API_V1_OFFERS = API_V1_URL + "/offers";

}
