package com.jakir.playshop.tryourapps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakir.playshop.R;

import java.util.ArrayList;
import java.util.List;

public class TryOurAppsBottomSheet extends BottomSheetDialogFragment {
    List<Appinfo> appList = new ArrayList<>();

    public TryOurAppsBottomSheet(List<Appinfo> appList) {
        this.appList = appList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        AppListAdapter adapter = new AppListAdapter(getContext(), appList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView close = view.findViewById(R.id.close); // Correct way to find the close button
        close.setOnClickListener(v -> dismiss());
    }

    private static class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {
        private final Context context;
        private final List<Appinfo> appList;

        public AppListAdapter(Context context, List<Appinfo> appList) {
            this.context = context;
            this.appList = appList;
        }

        @NonNull
        @Override
        public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
            return new AppViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
            Appinfo app = appList.get(position);
            holder.appName.setText(app.getAppName());
            Glide.with(context).load(app.getAppIcon()).into(holder.appIcon);
            holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app.getPackageName()))));
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }

        private void openApp(String packageName) {
/*            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                context.startActivity(intent);
            }*/
        }

        static class AppViewHolder extends RecyclerView.ViewHolder {
            TextView appName;
            ImageView appIcon;

            public AppViewHolder(View itemView) {
                super(itemView);
                appName = itemView.findViewById(R.id.app_name);
                appIcon = itemView.findViewById(R.id.app_icon);
            }
        }
    }

    public static class Appinfo {
        private String appName;
        private String appIcon;
        private String packageName;

        public Appinfo(String appName, String appIcon, String packageName) {
            this.appName = appName;
            this.appIcon = appIcon;
            this.packageName = packageName;
        }

        public String getAppName() {
            return appName;
        }

        public String getAppIcon() {
            return appIcon;
        }

        public String getPackageName() {
            return packageName;
        }
    }

}
