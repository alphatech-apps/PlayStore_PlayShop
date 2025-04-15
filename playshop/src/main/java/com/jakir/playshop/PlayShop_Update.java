package com.jakir.playshop;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//
// Created by JAKIR HOSSAIN on 4/12/2025.
//
// for update call this code from activity
//         new Update(this, "apk-debug.apk","http://localhost:8080/app-debug.apk").updateApp();

public class PlayShop_Update {
    Context context;
    String appFileName;
    String appDownLink;

    public PlayShop_Update(Context context, String apkFileName, String appdownlink) {
        this.context = context;
        this.appFileName = apkFileName;
        this.appDownLink = appdownlink;
    }

    public void updateApp() {
        File apkFile = new File(context.getCacheDir(), appFileName);

        Intent intent = ((Activity) context).getIntent();
        if ("com.jakir.installer.INSTALL".equals(intent.getAction())) {
            if (apkFile.exists()) {
                VerifyInstallDialog(apkFile);
            }
        } else {
            if (apkFile.exists()) {
                VerifyInstallDialog(apkFile);
            } else {
                downloadApkToCacheWithNotification(appDownLink, apkFile);
            }
        }
    }

    public void downloadApkToCacheWithNotification(String urlStr, File apkFile) {
        String CHANNEL_ID = "apk_update_channel";
        int Notification_Id = 123;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setContentTitle("Downloading Update version of " + context.getString(R.string.app_name)).setContentText("Please wait...").setSmallIcon(android.R.drawable.stat_sys_download).setPriority(NotificationCompat.PRIORITY_LOW).setOnlyAlertOnce(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "APK Update", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(Notification_Id, builder.build());

        new Thread(() -> {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Failed to download APK", Toast.LENGTH_SHORT).show());
                    return;
                }

                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(apkFile);

                byte[] data = new byte[4096];
                int count;
                long total = 0;
                int lastProgress = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);

                    if (fileLength > 0) {
                        int progress = (int) (total * 100 / fileLength);
                        if (progress != lastProgress) {
                            lastProgress = progress;
                            builder.setProgress(100, progress, false).setContentText("Downloaded " + progress + "%");
                        }
                    }
                }

                output.flush();
                output.close();
                input.close();

                builder.setContentTitle("Downloaded updated version of " + context.getString(R.string.app_name)).setContentText("Download complete").setProgress(0, 0, false).setSmallIcon(android.R.drawable.stat_sys_download_done).setAutoCancel(true);

//                Intent installIntent = new Intent(context, MainActivity.class);
//                installIntent.setAction("com.jakir.installer.INSTALL");
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, installIntent, PendingIntent.FLAG_IMMUTABLE);
//                builder.setContentIntent(pendingIntent);
//                builder.addAction(android.R.drawable.stat_sys_download_done, "Install", pendingIntent);

                notificationManager.notify(Notification_Id, builder.build());

                ((Activity) context).runOnUiThread(() -> VerifyInstallDialog(apkFile));

            } catch (Exception e) {
                e.printStackTrace();
                notificationManager.cancel(Notification_Id);
//                ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Download error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    public void VerifyInstallDialog(File apkFile) {
        try {
            if (apkFile == null || !apkFile.exists()) {
//                Toast.makeText(context, "Cached APK not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Get current app versionCode
            PackageManager pm = context.getPackageManager();
            PackageInfo currentInfo = pm.getPackageInfo(context.getPackageName(), 0);
            long currentVersionCode = 0; // API 28+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                currentVersionCode = currentInfo.getLongVersionCode();
            }

            // 3. Get versionCode from APK
            PackageInfo apkInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), 0);
            if (apkInfo != null) {
                long apkVersionCode = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    apkVersionCode = apkInfo.getLongVersionCode();
                }

                // 4. Compare version codes
                if (apkVersionCode > currentVersionCode) {
                    ShowInstallDialog(apkFile);
                } else {
//                    Toast.makeText(context, "No update available", Toast.LENGTH_SHORT).show();
                }
            } else {
//                Toast.makeText(context, "Failed to read APK info", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(context, "Error checking update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowInstallDialog(File apkFile) {
//        final Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Load animation
        View rootView = dialog.findViewById(R.id.updt); // আপনার dialog layout-এর root ID
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_fade_in);
        rootView.startAnimation(anim);

        TextView btnInstall = dialog.findViewById(R.id.ok);
        TextView btnCancel = dialog.findViewById(R.id.cancel);
        TextView update_message1 = dialog.findViewById(R.id.update_message1);
        TextView update_message2 = dialog.findViewById(R.id.update_message2);
        TextView current_version = dialog.findViewById(R.id.current_version);
        TextView update_version = dialog.findViewById(R.id.update_version);

        update_message1.setText("Assalamu Alaikum " + context.getString(R.string.app_name) + " recommends that you update to the latest version.");

        // 2. Get current app versionCode
        PackageManager pm = context.getPackageManager();
        PackageInfo currentInfo = null;
        try {
            currentInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        long currentVersionCode = 0; // API 28+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            currentVersionCode = currentInfo.getLongVersionCode();
        }
        // 3. Get versionCode from APK
        PackageInfo apkInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), 0);
        if (apkInfo != null) {
            long apkVersionCode = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                apkVersionCode = apkInfo.getLongVersionCode();
            }
            current_version.setText("Your Current Version is : " + currentVersionCode);
            update_version.setText("Update Version is : " + apkVersionCode);

            btnInstall.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!context.getPackageManager().canRequestPackageInstalls()) {
                        ShowRequestInstallPermission();
                    } else {
                        dialog.dismiss();
                        installFromCache(apkFile);
                    }
                }
            });

            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();  // Just close the dialog
            });

            dialog.show();
        }
    }

    private void ShowRequestInstallPermission() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialog.setContentView(R.layout.app_install_permission);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Load animation
        View rootView = dialog.findViewById(R.id.install_per); // আপনার dialog layout-এর root ID
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_fade_in);
        rootView.startAnimation(anim);

        // Handle buttons
        TextView btnInstall = dialog.findViewById(R.id.txt_allow);
        TextView tv_permission_message = dialog.findViewById(R.id.tv_permission_message);
        ImageView img_app_icon = dialog.findViewById(R.id.app_icon);
        TextView txt_appname = dialog.findViewById(R.id.txt_appname);

        tv_permission_message.setText(Html.fromHtml("Enable <b>Allow this source</b> for Install updated version of <b>" + context.getString(R.string.app_name) + "</b>."));
        img_app_icon.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher_round));
        txt_appname.setText(context.getString(R.string.app_name));

        btnInstall.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse("package:" + context.getPackageName()));
            ((Activity) context).startActivityForResult(intent, 1234);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void installFromCache(File apkFile) {
        // Launch installer
        Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
