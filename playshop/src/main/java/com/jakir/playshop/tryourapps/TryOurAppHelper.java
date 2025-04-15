package com.jakir.playshop.tryourapps;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//
// Created by JAKIR HOSSAIN on 3/12/2025.
//
public class TryOurAppHelper {
    public void tryOurOtherAppsLoad(Context context, String developerLink, int openCountWant) {
        int openCount = AppOpenUtil.getCountAppOpen(context);
        if (openCount % openCountWant == 0 && new CheckInternet().isConnected(context)) {
            loadDataFromHtml(context, developerLink);
        }
    }

    public void showTryOurAppsDialog(AppCompatActivity activity) {
        List<TryOurAppsBottomSheet.Appinfo> appList = getAllApps(activity);

        if (!appList.isEmpty()) {
            TryOurAppsBottomSheet bottomSheet = new TryOurAppsBottomSheet(appList);
            bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
        } else {
            Log.d("showTryOurAppsDialog", "showTryOurAppsDialog is faild: appList.isEmpty");
//            Toast.makeText(activity, "appList.isEmpty", Toast.LENGTH_SHORT).show();
        }

        AppOpenUtil.countAppOpen(activity); // increese 1 for avoid again show dialog

    }

    private List<TryOurAppsBottomSheet.Appinfo> getAllApps(AppCompatActivity activity) {
        List<TryOurAppsBottomSheet.Appinfo> appList = new ArrayList<>();
        Cursor cursor = new TryOurAppListDatabase(activity).readData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String packageName = cursor.getString(cursor.getColumnIndexOrThrow(TryOurAppListDatabase.PACKAGE_NAME));
                String appName = cursor.getString(cursor.getColumnIndexOrThrow(TryOurAppListDatabase.APP_NAME));
                String appIcon = cursor.getString(cursor.getColumnIndexOrThrow(TryOurAppListDatabase.APP_ICON));

                appList.add(new TryOurAppsBottomSheet.Appinfo(appName, appIcon, packageName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return appList;
    }

    private void loadDataFromHtml(Context context, String developerLink) {
        new Thread(() -> {
            try {
                String urlStr = developerLink;  // Replace with your JSON URL
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                StringBuilder result = new StringBuilder();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray jsonArray = new JSONArray(result.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String name = jsonObject.getString("AppName");
                    String link = jsonObject.getString("AppLink");
                    String imageUrl = jsonObject.getString("AppImageUrl");

                    // Insert data into your database or perform other operations
                    new TryOurAppListDatabase(context).insertData(link, name, imageUrl);
                }

                inputStream.close();
                urlConnection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public boolean shouldShowTryAppDialog(Activity activity, int openCountWant) {
 /*   int openCount = Util.getCountAppOpen(activity);
      if (openCount % 5 == 0 && new CheckInternet().isConnected(activity) {
            return true;
        } else return false;
        */
        return AppOpenUtil.getCountAppOpen(activity) % openCountWant == 0 && new CheckInternet().isConnected(activity);
    }
}
