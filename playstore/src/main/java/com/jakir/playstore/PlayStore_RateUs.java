package com.jakir.playstore;

import android.content.Context;

import com.jakir.playstore.rateusdialog.Rate_Dialog;
import com.jakir.playstore.rateusdialog.Rate_DialogHelper;

//
// Created by JAKIR HOSSAIN on 4/11/2025.
//
public class PlayStore_RateUs {
    public PlayStore_RateUs(Context context, int day) {

        // Check if the rate dialog should be shown
        if (Rate_DialogHelper.shouldShowRateDialog(context, day)) {
            showRateDialog(context);
        }
    }

    private void showRateDialog(Context context) {
        Rate_Dialog rate_dialog = new Rate_Dialog(context);
        rate_dialog.show();

        Rate_DialogHelper.saveRateDialogShown(context); // Save that the dialog was shown
    }
}
