package com.bfirestone.udacity.cookbook.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class NetworkConnectionDetector {

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null &&
                        activeNetwork.isConnected();
            }

        } catch (Exception e) {
            Log.v("[connectivity]", e.getMessage());
        }

        return false;
    }
}
