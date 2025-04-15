package com.jakir.playstore;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.jakir.playstore.tryourapps.TryOurAppHelper;

//
// Created by JAKIR HOSSAIN on 4/11/2025.
//
public class PlayStore_TryOurApps {
    public PlayStore_TryOurApps(Context context, String developerName, int openCountWant) {
        new TryOurAppHelper().tryOurOtherAppsLoad(context, developerName, openCountWant); // openCountWant means how much time app open then try to load
    }
}

// call to count app open = splashscreen
//         AppOpenUtil.countAppOpen(this);


// call this for show on any activity  by back pressed
// Back Pressed Callback
//private boolean doubleBackPressed = false;
//getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
//    @Override
//    public void handleOnBackPressed() {
//
//        // for doubleBackPressed function
//        if (doubleBackPressed) {
//            finish(); // Exit the app
//        } else {
//            doubleBackPressed = true;
//            Toast.makeText(HomeActivity.this, "Press once again to exit", Toast.LENGTH_SHORT).show();
//
//            // Check if the rate dialog should be shown
//            if (new TryOurAppHelper().shouldShowTryAppDialog(HomeActivity.this, 3)) {  // openCountWant means how much time app open then try to show
//                new TryOurAppHelper().showTryOurAppsDialog(HomeActivity.this);
//            }
////                  else Toast.makeText(HomeActivity.this, "Exit Dialog Show", Toast.LENGTH_SHORT).show();
//
//            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackPressed = false, 2000);
//        }
//
///*                // for exit dialog function
//
//                // Check if the rate dialog should be shown
//                if (new TryOurAppHelper().shouldShowTryAppDialog(HomeActivity.this, 6)) {  // openCountWant means how much time app open then try to show
//                    new TryOurAppHelper().showTryOurAppsDialog(HomeActivity.this);
//                }else {
//                    new Exit_Dialog(this).show();
//                }*/
//    }
//});
