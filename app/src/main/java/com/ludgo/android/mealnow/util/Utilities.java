package com.ludgo.android.mealnow.util;

import android.content.Context;
import android.content.Intent;

import com.ludgo.android.mealnow.model.MyInfo;
import com.ludgo.android.mealnow.ui.activity.MainActivity;

import java.util.List;

public class Utilities {

    public static String buildLoginUrl(String provider) {
        return Constants.API_V1_URL + "/" + provider + "/login";
    }

    /**
     * Check if user's credentials {@link MyInfo} are stored in the database
     * @return true in case of signed in user
     */
    public static boolean isUser() {
        List<MyInfo> myInfo = MyInfo.getAll();
        return myInfo.size() > 0;
    }

    /**
     * Make app as if it was freshly opened
     */
    public static void logoutUser(Context context) {
        // Remove all credentials of previously signed in user
        MyInfo.deleteAll();
        toMainActivity(context);
    }

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
