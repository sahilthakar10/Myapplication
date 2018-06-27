package com.abc.example.com.myapplication.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABC on 29-09-2017.
 */

public class DATABASEHANDLER extends SQLiteOpenHelper {
    private static final String TABLE_REGISTRATION = "project2";
    private static final String KEY_ID = "id";
    private static final String KEY_bodyname = "bodyname";
    private static final String KEY_description = "description";

    public DATABASEHANDLER(Context context) {
        super(context, Dbversion.DATABASE_NAME, null, Dbversion.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTRATION = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTRATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_bodyname + " TEXT," + KEY_description + " TEXT"+")";
        db.execSQL(CREATE_REGISTRATION);
        Log.e("sqffl", CREATE_REGISTRATION);
    }

        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION);
        onCreate(db);
    }

    public void addDATA(Registration reeg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_bodyname, reeg.getBodyname());
        value.put(KEY_description,reeg.getDescription());
        db.insert(TABLE_REGISTRATION, null, value);
        db.close();
    }


    public List<Registration> getAllData(String bodyname , String version ) {
        try {
            List<Registration> list = new ArrayList<Registration>();
            String select = "SELECT * FROM " + TABLE_REGISTRATION + " WHERE bodyname LIKE '%"+bodyname+"%' OR description LIKE '%"+bodyname+"%'";
            Log.e("select1" , select);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    Registration obj = new Registration();
                    obj.setId(Integer.parseInt(cursor.getString(0)));
                    obj.setBodyname(cursor.getString(1));
                    obj.setDescription(cursor.getString(2));
                    list.add(obj);
                } while (cursor.moveToNext());
            }
            return list;
        } catch (Exception e) {

        }
        return null;
    }
    public void truncate(){
        SQLiteDatabase DB = this.getWritableDatabase();
        String url =  "DELETE FROM "+ TABLE_REGISTRATION;
        DB.execSQL(url);

    }


}
