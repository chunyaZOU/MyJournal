package com.example.h2601864.myjournal.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by H2601864 on 2015/9/30.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String CREATE_TABLE_JOURNAL =
            "create table if not exists journal(_id integer primary key autoincrement,title TEXT,content TEXT,path TEXT,createTime TEXT)";
    private static DBHelper instance = null;

    private DBHelper(Context context) {
        super(context, "myJournal.db", null, 1);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOURNAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
