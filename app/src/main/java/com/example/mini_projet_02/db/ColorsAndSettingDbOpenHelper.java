package com.example.mini_projet_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.mini_projet_02.models.Color;

public class ColorsAndSettingDbOpenHelper extends SQLiteOpenHelper {

    Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "colors_and_setting.db";
    private static final String CREATE_TABLE_COLORS = "CREATE TABLE IF NOT EXISTS "+ ColorsContract.Info.TABLE_NAME+" ("+ ColorsContract.Info.COLUMN_NAME+" TEXT PRIMARY KEY, "+ ColorsContract.Info.COLUMN_CODE + " TEXT);";
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS "+ SettingsContract.Info.TABLE_NAME+" ("+  SettingsContract.Info.COLUMN_NAME+" TEXT PRIMARY KEY, "+ SettingsContract.Info.COLUMN_VALUE + " TEXT);";
    public static final String DROP_TABLE_COLORS = "DROP TABLE IF EXISTS "+ ColorsContract.Info.TABLE_NAME;
    public static final String DROP_TABLE_SETTINGS = "DROP TABLE IF EXISTS "+ SettingsContract.Info.TABLE_NAME;

    public ColorsAndSettingDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COLORS);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_COLORS);
        db.execSQL(DROP_TABLE_SETTINGS);
    }

    public void addColor(String name , String code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("code", code);
        db.insert(ColorsContract.Info.TABLE_NAME, null, cv);
    }

    public void addSetting(Color color) {
        SQLiteDatabase db = getWritableDatabase();
        if (getSetting() == null){
            ContentValues cv = new ContentValues();
            cv.put("value", color.getCode());
            cv.put("value", color.getCode());
            db.insert(SettingsContract.Info.TABLE_NAME, null, cv);
        }else {
            updateSetting(color);
        }
    }

    public Color getSetting(){
        SQLiteDatabase db = getReadableDatabase();
        Color color = null;
        String[] projection = {
                SettingsContract.Info.COLUMN_NAME,
                SettingsContract.Info.COLUMN_VALUE
        };
        try (Cursor cursor = db.query(SettingsContract.Info.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)) {


            while (cursor.moveToNext()) {
                color = new Color(cursor.getString(0), cursor.getString(1));
            }
        }
        return color;
    }

    public void updateSetting(Color color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",color.getName() );
        cv.put("value", color.getCode());
        db.update(SettingsContract.Info.TABLE_NAME, cv, null,null);
    }
}
