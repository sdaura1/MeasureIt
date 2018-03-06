package com.example.shaheed.mtesting;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shaheed on 12/29/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Tailors_DB";
    public static final String TB_NAME = "Records_TB";
    public static final int DB_VER = 2;
    SQLiteDatabase db;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +  TB_NAME + " " +
                "( _id INTEGER primary key autoincrement, name TEXT not null," +
                "phone_number LONG not null, shirt INTEGER not null," +
                " shoulder INTEGER not null, hand INTEGER not null, trouser INTEGER not null);");
    }

    public boolean insertContact (String name_val, int shoulder_val, int trouser_val,
                                  int shirt_val, int hand_val, long ph_val) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name_val);
        contentValues.put("shoulder", shoulder_val);
        contentValues.put("trouser", trouser_val);
        contentValues.put("shirt", shirt_val);
        contentValues.put("hand", hand_val);
        contentValues.put("phone_number", ph_val);
        db.insert(TB_NAME, null, contentValues);
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}