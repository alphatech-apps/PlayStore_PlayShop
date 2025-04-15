package com.jakir.playstoreplayshop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jakir.playstore.tryourapps.AppOpenUtil;
import com.jakir.playstore.tryourapps.TryOurAppHelper;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppOpenUtil.countAppOpen(this); // this for splash screen


        new DoRemoteJob(this);  // Remote job for load background data

        // Back Pressed Callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                // for doubleBackPressed function
                if (doubleBackPressed) {
                    finish(); // Exit the app
                } else {
                    doubleBackPressed = true;
                    Toast.makeText(MainActivity.this, "Press once again to exit", Toast.LENGTH_SHORT).show();

                    // Check if the rate dialog should be shown
                    // this for play store version
                    if (new TryOurAppHelper().shouldShowTryAppDialog(MainActivity.this, 3)) {  // openCountWant means how much time app open then try to show
                        new TryOurAppHelper().showTryOurAppsDialog(MainActivity.this);
                    }
//                  else Toast.makeText(MainActivity.this, "Exit Dialog Show", Toast.LENGTH_SHORT).show();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackPressed = false, 2000);
                }

/*                // for exit dialog function

                // Check if the rate dialog should be shown
                if (new TryOurAppHelper().shouldShowTryAppDialog(MainActivity.this, 6)) {  // openCountWant means how much time app open then try to show
                    new TryOurAppHelper().showTryOurAppsDialog(MainActivity.this);
                }else {
                    new Exit_Dialog(this).show();
                }*/
            }
        });
    }
}