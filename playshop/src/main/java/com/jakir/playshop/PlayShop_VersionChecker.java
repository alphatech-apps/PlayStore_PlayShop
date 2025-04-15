package com.jakir.playshop;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayShop_VersionChecker {
    public PlayShop_VersionChecker(Context context, String url, OnVersionCheckListener listener) {
        getDataFromLink(context, url, listener);
    }

    private void getDataFromLink(Context context, String url, OnVersionCheckListener listener) {
        new Thread(() -> {
            try {
                URL link = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) link.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                inputStream.close();
                connection.disconnect();

                int latestVersion = Integer.parseInt(result.toString().trim());

                new Handler(Looper.getMainLooper()).post(() -> listener.onVersionChecked(latestVersion));

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> listener.onVersionChecked(0));
            }
        }).start();
    }

    public interface OnVersionCheckListener {
        void onVersionChecked(int versionCode);
    }

}

