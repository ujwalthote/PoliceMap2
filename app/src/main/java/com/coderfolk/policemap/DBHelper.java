package com.coderfolk.policemap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    List<Data> mylist = new ArrayList<>();
    //double data[][] = new double[100][100];

    public DBHelper(Context context) {
        super(context, "MapsData", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "create table if not exists mapdata(latitude DOUBLE, longitude DOUBLE, victim TEXT, suspect TEXT, type Text)";
        db.execSQL(createTableQuery);
    }

    public boolean insertData(double value1, double value2, String victim, String suspect, String type) {
        try {
            //Log.i("values", String.valueOf(value1) + "  " + String.valueOf(value2));
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("latitude", value1);
            values.put("longitude", value2);
            values.put("victim",victim);
            values.put("suspect",suspect);
            values.put("type",type);


            db.insert("mapdata", null, values);
            values.clear();
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Data> readData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from mapdata", null);

        //coordinates.clear();
        if (cursor.moveToFirst()) {

            do {
                Data temp = new Data();
                double value1 = Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude")));
                double value2 = Double.parseDouble(cursor.getString(cursor.getColumnIndex("longitude")));
                String victim=cursor.getString(cursor.getColumnIndex("victim"));
                String suspect=cursor.getString(cursor.getColumnIndex("suspect"));
                String type=cursor.getString(cursor.getColumnIndex("type"));


                temp.latitude = value1;
                temp.longitude = value2;
                temp.victim=victim;
                temp.suspect=suspect;
                temp.type=type;
                mylist.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mylist;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}