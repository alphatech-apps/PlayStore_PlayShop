[![](https://jitpack.io/v/alphatech-apps/PlayStore_PlayShop.svg)](https://jitpack.io/#alphatech-apps/PlayStore_PlayShop)


How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root settings.gradle at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

 
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.alphatech-apps:PlayStore_PlayShop:Tag'
	}


 Step 3. add on Remote class

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jakir.playshop.PlayShop_TryOurApps;
import com.jakir.playshop.PlayShop_Update;
import com.jakir.playshop.PlayShop_VersionChecker;
import com.jakir.playstore.PlayStore_RateUs;
import com.jakir.playstore.PlayStore_TryOurApps;

import java.util.Calendar;

//
// Created by JAKIR HOSSAIN on 3/16/2025.
//
public class DoRemoteJob {
    Context context;
    boolean isPlayStoreVersion = true;

    public DoRemoteJob(Context context) {
        this.context = context;

        if (isPlayStoreVersion) { // PlayStore
            PlayStore_updateFromPlayStore(context);
            PlayStore_tryOurOtherAppsLoad(context);
            PlayStore_RateUs(context);

        } else { // PlayShop
            PlayShop_updateFromWebsite(context);
            PlayShop_tryOurOtherAppsLoad(context);

        }

    }

    private void PlayStore_updateFromPlayStore(Context context) {

    }

    private void PlayStore_tryOurOtherAppsLoad(Context context) {
        String developerName = context.getString(R.string.developerName);
        new PlayStore_TryOurApps(context, developerName, 2);
    }

    private void PlayStore_RateUs(Context context) {
        new PlayStore_RateUs(context, Calendar.SUNDAY);
    }

    private void PlayShop_updateFromWebsite(Context context) {
        new PlayShop_VersionChecker(context, "http://localhost:8080/", versionCode -> {
            int currentVersion = getCurrentVersionCode(context);

            if (versionCode > currentVersion) {
//                Toast.makeText(context, "Server version: " + versionCode, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Update Available!", Toast.LENGTH_LONG).show();
                new PlayShop_Update(context, "apk-debug.apk", "http://localhost:8080/app-debug.apk").updateApp();
            } else {
//                Toast.makeText(context, "App is Up to Date", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void PlayShop_tryOurOtherAppsLoad(Context context) {
        new PlayShop_TryOurApps(context, "http://localhost:8080/");
    }


    private int getCurrentVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
