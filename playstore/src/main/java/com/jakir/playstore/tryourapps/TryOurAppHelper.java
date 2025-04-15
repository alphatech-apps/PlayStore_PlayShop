package com.jakir.playstore.tryourapps;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//
// Created by JAKIR HOSSAIN on 3/12/2025.
//
public class TryOurAppHelper {
    public void tryOurOtherAppsLoad(Context context, String developerName, int openCountWant) {
        int openCount = AppOpenUtil.getCountAppOpen(context);
        if (openCount % openCountWant == 0 && new CheckInternet().isConnected(context)) {
            loadDataFromHtml(context, developerName);
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

    private void loadDataFromHtml(Context context, String developerName) {
        new Thread(() -> {
            try {
                String url = "https://play.google.com/store/apps/developer?id=" + developerName;
                Document doc = Jsoup.connect(url).get();
                Elements apps = doc.select("a.Si6A0c.Gy4nib");

                for (Element app : apps) {
                    String link = "https://play.google.com" + app.attr("href");
                    String name = app.select("span.DdYX5").text();
                    String imageUrl = app.select("div.j2FCNc img").attr("src");

                    new TryOurAppListDatabase(context).insertData(link, name, imageUrl);
                }

            } catch (IOException e) {
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
