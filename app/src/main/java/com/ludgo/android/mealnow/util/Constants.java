package com.ludgo.android.mealnow.util;


import com.ludgo.android.mealnow.BuildConfig;

public final class Constants {

    public static final String OKHTTP_TAG = "OkHttp";
    public static final String OKHTTP_REQUEST_TAG = "---> ";
    public static final String OKHTTP_RESPONSE_TAG = "<--- ";

    public static final String API_URL_BASE = BuildConfig.SERVER_BASE_URL;
    public static final String API_V1_URL = API_URL_BASE + "/api/v1";
    public static final String API_V1_LOGIN_GOOGLE = API_V1_URL + "/google/login";
    public static final String API_V1_LOGOUT_GOOGLE = API_V1_URL + "/google/logout";

}
