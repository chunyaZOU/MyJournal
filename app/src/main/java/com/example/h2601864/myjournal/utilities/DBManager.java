package com.example.h2601864.myjournal.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by H2601864 on 2015/9/30.
 */
public class DBManager {

    private SQLiteDatabase db;

    public DBManager(Context context) {
        SQLiteOpenHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public long addJournal(String title, String content, String path, String time) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("path", path);
        values.put("createTime", time);
        return db.insert("journal", null, values);
    }

    public int deleteJournal(String id) {
        return db.delete("journal", "_id=?", new String[]{id});
    }

    public int updateJournal(String id, String title, String content, String path, String time) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("path", path);
        //values.put("createTime", time);
        return db.update("journal", values, "_id=?", new String[]{id});
    }

    public Cursor queryJournal(String id, String searchStr, String time) {
        if (!id.equals("") || !searchStr.equals("") || !time.equals("")) {
            return db.rawQuery("select * from journal where title like '%" + searchStr + "%' or content like '%" + searchStr + "%' ORDER BY datetime(createTime) DESC", null);
            //return db.query("journal", new String[]{"_id", "title", "content", "path", "createTime"}, "title like '%"+?+"%' or content like '%"+?+"%', new String[]{searchStr,searchStr}, null, null, null);
        } else {
            return db.rawQuery("select * from journal ORDER BY datetime(createTime) DESC", null);
            /*return db.query("journal", new String[]{"_id", "title", "content","path" ,"createTime"},
                    "_id = ? or title like '%"+?+"%' or content like '%"+?+"%' or createTime= ?", new String[]{id, searchStr, searchStr, time}, null, null, null);*/

        }
    }

    /**
     * get data by month
     * @param time
     * @return
     */
    public Cursor queryJournalByMonth(String time){
        return db.rawQuery("select * from journal where substr(createTime,6,2)='" + time + "' ORDER BY datetime(createTime) DESC", null);
    }
}

