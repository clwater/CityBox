package com.uit.uit2013.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uit.uit2013.model.KeBiao;

import java.util.Vector;

/**
 * Created by soul on 2016/1/22.
 */
public class ScheduleDateCtrl {
    public static ScheduleDateHelp dbHelper;
    public  static  SQLiteDatabase db;

    public static void createSQL(Context context){
        dbHelper = new ScheduleDateHelp(context, "schedule.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
    }

    public static void UpdateSchedule(Context context , String class_name , String classroom , String weeks , String colors , int INDEX_W , int INDEX_T ){
        createSQL(context);
        ContentValues values = new ContentValues();
        values.put("class_name", class_name);
        values.put("classroom", classroom);
        values.put("weeks", weeks);
        values.put("colors", colors);
        values.put("INDEX_W", INDEX_W);
        values.put("INDEX_T", INDEX_T);
        db.insert("schedule", null, values);
    }

    public static Vector<KeBiao> QuerySchedule(Context context){
        createSQL(context);
        Vector schedule = new Vector<KeBiao>();
        Cursor cursor = db.query("schedule", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String class_name = cursor.getString(cursor.getColumnIndex("class_name"));
                String classroom = cursor.getString(cursor.getColumnIndex("classroom"));
                String weeks = cursor.getString(cursor.getColumnIndex("weeks"));
                String colors = cursor.getString(cursor.getColumnIndex("colors"));
                String INDEX_W = cursor.getString(cursor.getColumnIndex("INDEX_W"));
                String INDEX_T = cursor.getString(cursor.getColumnIndex("INDEX_T"));

                KeBiao kb = new KeBiao();
                kb.setClass_name(class_name);
                kb.setClassroom(classroom);
                kb.setWeeks(weeks);
                kb.setColors(colors);
                kb.setINDEX_W(Integer.valueOf(INDEX_W));
                kb.setINDEX_T(Integer.valueOf(INDEX_T));
//                Log.d("-=", "class_name" + class_name);
//                Log.d("-=", "classroom" + classroom);
//                Log.d("-=", "weeks" + weeks);
//                Log.d("-=", "colors" + colors);
//                Log.d("-=", "INDEX_W" + INDEX_W);
//                Log.d("-=", "INDEX_T" + INDEX_T);
                schedule.add(kb);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return schedule;
    }

}