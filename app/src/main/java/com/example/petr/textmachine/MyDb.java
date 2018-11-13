package com.example.petr.textmachine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmSDb.db" ;
    private static final String TABLE_NAME = "SMS" ;
    private static final String COLUMN_ID_NAME = "ID" ;
    private static final String COLUMN_NAME_NAME = "NAME" ;
    private static final String COLUMN_NUMBER_NAME = "NUMBER" ;
    private static final String COLUMN_TEXT_NAME = "TEXT" ;
    private static final String COLUMN_TIME_NAME = "TIME" ;
    private static final String COLUMN_ENABLE_NAME = "ENABLE" ;
    private static MyDb instance = null;

    public MyDb(@Nullable Context context,  @Nullable String name,  @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static MyDb getInstance(Context c) {
        if(instance == null) {
            instance = new MyDb(c,DATABASE_NAME,null,1);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+TABLE_NAME + " "+
                        "("+COLUMN_ID_NAME+" integer primary key, " +
                        COLUMN_NAME_NAME+" text, "+
                        COLUMN_NUMBER_NAME+" text, "+
                        COLUMN_TEXT_NAME+" text, "+
                        COLUMN_TIME_NAME+" text, "+
                        COLUMN_ENABLE_NAME+" text "+
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertSMS (String id, String name, String number, String text,String time,String enable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_NAME, id);
        contentValues.put(COLUMN_NAME_NAME, name);
        contentValues.put(COLUMN_NUMBER_NAME , number);
        contentValues.put(COLUMN_TEXT_NAME, text);
        contentValues.put(COLUMN_TIME_NAME , time);
        contentValues.put(COLUMN_ENABLE_NAME , enable);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public boolean updateSMS (String id, String name, String number, String text,String time,String enable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_NAME, id);
        contentValues.put(COLUMN_NAME_NAME, name);
        contentValues.put(COLUMN_NUMBER_NAME , number);
        contentValues.put(COLUMN_TEXT_NAME, text);
        contentValues.put(COLUMN_TIME_NAME , time);
        contentValues.put(COLUMN_ENABLE_NAME , enable);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }
    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_ID_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
