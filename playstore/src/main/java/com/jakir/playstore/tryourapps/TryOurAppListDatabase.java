package com.jakir.playstore.tryourapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TryOurAppListDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tryapplistdatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_APPLIST = "tryapplist";
    public static final String ID = "_id";
    public static final String PACKAGE_NAME = "packagename";
    public static final String APP_NAME = "appname";
    public static final String APP_ICON = "appicon";
    private static final String CREATE_APPLIST_TABLE =
            "CREATE TABLE " + TABLE_APPLIST + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PACKAGE_NAME + " TEXT, " +
                    APP_NAME + " TEXT, " +
                    APP_ICON + " TEXT)";
    Context context;

    public TryOurAppListDatabase(Context context) {
        super(context, getExternalDatabasePath(context), null, DATABASE_VERSION);
        this.context = context;
    }

    public static String getExternalDatabasePath(Context context) {
//        File dir = new File(String.valueOf(context.getExternalFilesDir("Database"))); //internal_storage/android/data/app_name/files/Database
//        File dir = new File(Environment.getExternalStorageDirectory(),"Database"); //internal_storage/Database
        File dir = new File(String.valueOf(context.getDatabasePath("Database"))); //data/user/0/app_name/database/Database
//        File dir = new File(String.valueOf(context.getCacheDir()),"Database"); //data/user/0/app_name/cache/Database
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, DATABASE_NAME).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APPLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPLIST);
        onCreate(db);
    }

    public boolean insertData(String packageName, String appName, String appIcon) {
        if (!checkIfPackageExists(packageName)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(PACKAGE_NAME, packageName);
            contentValues.put(APP_NAME, appName);
            contentValues.put(APP_ICON, appIcon);
            long result = db.insert(TABLE_APPLIST, null, contentValues);
            return result != -1; // returns true if data is inserted successfully
        }else {
            return false;
        }
 }


    public boolean checkIfPackageExists(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPLIST, null, PACKAGE_NAME + "=?", new String[]{packageName}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public Cursor readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_APPLIST, null);
//        return db.query(TABLE_APPLIST, null, null, null, null, null, APP_NAME + " ASC");

    }
    public Integer deleteData(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_APPLIST, PACKAGE_NAME + " = ?", new String[]{packageName});
    }

    public boolean backupDatabase() {
        File backupDBPath = new File(Environment.getExternalStorageDirectory(), "Backup_Database");
        if (!backupDBPath.exists()) {
            backupDBPath.mkdir();
        }
        File currentDB = new File(getExternalDatabasePath(context));
        File backupDB = new File(backupDBPath, DATABASE_NAME);

        try {
            FileInputStream src = new FileInputStream(currentDB);
            FileOutputStream dst = new FileOutputStream(backupDB);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = src.read(buffer)) > 0) {
                dst.write(buffer, 0, length);
            }
            src.close();
            dst.close();
            Toast.makeText(context, "Database backup completed successfully", Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to backup database", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
