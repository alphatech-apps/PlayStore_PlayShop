package com.jakir.playstore.rateusdialog;

//
// Created by JAKIR HOSSAIN on 3/12/2025.
//

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakir.playstore.R;


public class Rate_Dialog extends Dialog {

    Context context;

    public Rate_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide_in_down; //dialog animation

        setContentView(R.layout.rate_us_dialog);

        ImageView s1 = findViewById(R.id.star01);
        ImageView s2 = findViewById(R.id.star02);
        ImageView s3 = findViewById(R.id.star03);
        ImageView s4 = findViewById(R.id.star04);
        ImageView s5 = findViewById(R.id.star05);

        s1.setOnClickListener(view2 -> {
            s1.setImageResource(R.drawable.ic_star_fill);
            s2.setImageResource(R.drawable.ic_star_outline);
            s3.setImageResource(R.drawable.ic_star_outline);
            s4.setImageResource(R.drawable.ic_star_outline);
            s5.setImageResource(R.drawable.ic_star_outline);
        });
        s2.setOnClickListener(view2 -> {
            s1.setImageResource(R.drawable.ic_star_fill);
            s2.setImageResource(R.drawable.ic_star_fill);
            s3.setImageResource(R.drawable.ic_star_outline);
            s4.setImageResource(R.drawable.ic_star_outline);
            s5.setImageResource(R.drawable.ic_star_outline);
        });
        s3.setOnClickListener(view2 -> {
            s1.setImageResource(R.drawable.ic_star_fill);
            s2.setImageResource(R.drawable.ic_star_fill);
            s3.setImageResource(R.drawable.ic_star_fill);
            s4.setImageResource(R.drawable.ic_star_outline);
            s5.setImageResource(R.drawable.ic_star_outline);
        });
        s4.setOnClickListener(view2 -> {
            s1.setImageResource(R.drawable.ic_star_fill);
            s2.setImageResource(R.drawable.ic_star_fill);
            s3.setImageResource(R.drawable.ic_star_fill);
            s4.setImageResource(R.drawable.ic_star_fill);
            s5.setImageResource(R.drawable.ic_star_outline);
            openPlayStore(context);
        });
        s5.setOnClickListener(view2 -> {
            s1.setImageResource(R.drawable.ic_star_fill);
            s2.setImageResource(R.drawable.ic_star_fill);
            s3.setImageResource(R.drawable.ic_star_fill);
            s4.setImageResource(R.drawable.ic_star_fill);
            s5.setImageResource(R.drawable.ic_star_fill);
            openPlayStore(context);
        });

        TextView rate = findViewById(R.id.rate_txt_btn);
        rate.setOnClickListener(view2 -> {
            openPlayStore(context);
        });
        TextView btnRemindLater = findViewById(R.id.btnRemindLater);
        btnRemindLater.setOnClickListener(view2 -> {
            Rate_DialogHelper.saveRemindMeLater(context); // Save remind me later date
            dismiss();
        });

    }

    private void openPlayStore(Context context) {
        dismiss();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
