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
    private static final String COLUMN_NAME_NAME = "NAME" ; //desc
    private static final String COLUMN_NUMBER_NAME = "NUMBER" ;
    private static final String COLUMN_TEXT_NAME = "TEXT" ; //sms
    private static final String COLUMN_HOUR_NAME = "HOUR" ;
    private static final String COLUMN_MINUTE_NAME = "MINUTE" ;
    private static final String COLUMN_ENABLE_NAME = "ENABLE" ;

    private static final String COLUMN_PO = "PO" ;
    private static final String COLUMN_UT = "UT" ;
    private static final String COLUMN_ST = "ST" ;
    private static final String COLUMN_CT = "CT" ;
    private static final String COLUMN_PA = "PA" ;
    private static final String COLUMN_SO = "SO" ;
    private static final String COLUMN_NE = "NE" ;
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
    public static MyDb getInstance() {
        if(instance != null) {
            return instance;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+TABLE_NAME + " "+
                        "("+COLUMN_ID_NAME+" integer primary key AUTOINCREMENT, " +
                        COLUMN_NAME_NAME+" text, "+
                        COLUMN_NUMBER_NAME+" text, "+
                        COLUMN_TEXT_NAME+" text, "+
                        COLUMN_HOUR_NAME+" integer, "+
                        COLUMN_MINUTE_NAME+" integer," +
                        COLUMN_PO+" integer,"+
                        COLUMN_UT+" integer,"+
                        COLUMN_ST+" integer,"+
                        COLUMN_CT+" integer,"+
                        COLUMN_PA+" integer,"+
                        COLUMN_SO+" integer,"+
                        COLUMN_NE+" integer,"+
                        COLUMN_ENABLE_NAME+" integer "+
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public AlarmData getAlarm(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME + " WHERE id = " +id, null );
        if (res.getCount()>0)
            res.moveToFirst();

        while(res.isAfterLast() == false){
            AlarmData alarmFromDB = new AlarmData(
                    res.getInt(res.getColumnIndex(COLUMN_HOUR_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_MINUTE_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_PO)),
                    res.getInt(res.getColumnIndex(COLUMN_UT)),
                    res.getInt(res.getColumnIndex(COLUMN_ST)),
                    res.getInt(res.getColumnIndex(COLUMN_CT)),
                    res.getInt(res.getColumnIndex(COLUMN_PA)),
                    res.getInt(res.getColumnIndex(COLUMN_SO)),
                    res.getInt(res.getColumnIndex(COLUMN_NE)),
                    res.getString(res.getColumnIndex(COLUMN_TEXT_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NUMBER_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NAME_NAME)));
            alarmFromDB.id=res.getInt(res.getColumnIndex(COLUMN_ID_NAME));
           return alarmFromDB;
        }
    return null;
    }
    public boolean insertSMS (int hour,int min,int monday,int tuesday, int wednesday,int thursday,
                              int friday,int saturday,int sunday,String smsText,String tel, String description, int enable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(COLUMN_ID_NAME, id);
        contentValues.put(COLUMN_NAME_NAME, description);
        contentValues.put(COLUMN_NUMBER_NAME , tel);
        contentValues.put(COLUMN_TEXT_NAME, smsText);
        contentValues.put( COLUMN_HOUR_NAME, hour);
        contentValues.put( COLUMN_MINUTE_NAME , min);
        contentValues.put(COLUMN_PO,monday   );
        contentValues.put(COLUMN_UT,tuesday  );
        contentValues.put(COLUMN_ST,wednesday);
        contentValues.put(COLUMN_CT,thursday );
        contentValues.put(COLUMN_PA,friday   );
        contentValues.put(COLUMN_SO,saturday );
        contentValues.put(COLUMN_NE,sunday);
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
    public boolean updateAlarmEnabled (int alarmid, boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ENABLE_NAME , enabled?1:0);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { String.valueOf(alarmid)} );
        return true;
    }
    public boolean updateAlarm (AlarmData edited) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NAME, edited.description);
        contentValues.put(COLUMN_NUMBER_NAME , edited.tel);
        contentValues.put(COLUMN_TEXT_NAME, edited.smsText);
        contentValues.put( COLUMN_HOUR_NAME, edited.hour);
        contentValues.put( COLUMN_MINUTE_NAME , edited.min);
        contentValues.put(COLUMN_PO,edited.monday?1:0 );
        contentValues.put(COLUMN_UT,edited.tuesday  ?1:0);
        contentValues.put(COLUMN_ST,edited.wednesday?1:0);
        contentValues.put(COLUMN_CT,edited.thursday ?1:0);
        contentValues.put(COLUMN_PA,edited.friday   ?1:0);
        contentValues.put(COLUMN_SO,edited.saturday ?1:0);
        contentValues.put(COLUMN_NE,edited.sunday?1:0);
        contentValues.put(COLUMN_ENABLE_NAME , edited.enable?1:0);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { String.valueOf(edited.id )} );
        return true;
    }
    public Integer deleteAlarm (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<AlarmData> getAllAlarm() {
        ArrayList<AlarmData> array_list = new ArrayList<AlarmData>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
       if (res.getCount()>0)
        res.moveToFirst();

        while(res.isAfterLast() == false){
                 AlarmData alarmFromDB = new AlarmData(
                    res.getInt(res.getColumnIndex(COLUMN_HOUR_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_MINUTE_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_PO)),
                    res.getInt(res.getColumnIndex(COLUMN_UT)),
                    res.getInt(res.getColumnIndex(COLUMN_ST)),
                    res.getInt(res.getColumnIndex(COLUMN_CT)),
                    res.getInt(res.getColumnIndex(COLUMN_PA)),
                    res.getInt(res.getColumnIndex(COLUMN_SO)),
                    res.getInt(res.getColumnIndex(COLUMN_NE)),
                    res.getString(res.getColumnIndex(COLUMN_TEXT_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NUMBER_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NAME_NAME)));

            alarmFromDB.enable = res.getInt(res.getColumnIndex((COLUMN_ENABLE_NAME)))==1?true:false;
            alarmFromDB.id=res.getInt(res.getColumnIndex(COLUMN_ID_NAME));

            array_list.add(alarmFromDB);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<AlarmData> getAlarmsToExecute(int hour, int min, int days ){

        String day = "";
        switch (days){
            case 1: day = COLUMN_NE + "= 1";
                break;
            case 2: day = COLUMN_PO + "= 1";
                break;
            case 3: day = COLUMN_UT + "= 1";
                break;
            case 4: day = COLUMN_ST + "= 1";
                break;
            case 5: day = COLUMN_CT + "= 1";
                break;
            case 6: day = COLUMN_PA + "= 1";
                break;
            case 7: day = COLUMN_SO + "= 1";
                break;
        }


        ArrayList<AlarmData> array_list = new ArrayList<AlarmData>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "   +TABLE_NAME +   " Where "    +COLUMN_ENABLE_NAME+   "=1 and "   +COLUMN_HOUR_NAME+ " = "   +hour + " and "+COLUMN_MINUTE_NAME+" = "+min +" and "+ day , null );
        if (res.getCount()>0)
            res.moveToFirst();

        while(res.isAfterLast() == false){
            AlarmData alarmFromDB = new AlarmData(
                    res.getInt(res.getColumnIndex(COLUMN_HOUR_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_MINUTE_NAME)),
                    res.getInt(res.getColumnIndex(COLUMN_PO)),
                    res.getInt(res.getColumnIndex(COLUMN_UT)),
                    res.getInt(res.getColumnIndex(COLUMN_ST)),
                    res.getInt(res.getColumnIndex(COLUMN_CT)),
                    res.getInt(res.getColumnIndex(COLUMN_PA)),
                    res.getInt(res.getColumnIndex(COLUMN_SO)),
                    res.getInt(res.getColumnIndex(COLUMN_NE)),
                    res.getString(res.getColumnIndex(COLUMN_TEXT_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NUMBER_NAME)),
                    res.getString(res.getColumnIndex(COLUMN_NAME_NAME)));

            alarmFromDB.enable = res.getInt(res.getColumnIndex((COLUMN_ENABLE_NAME)))==1?true:false;
            alarmFromDB.id=res.getInt(res.getColumnIndex(COLUMN_ID_NAME));

            array_list.add(alarmFromDB);
            res.moveToNext();
        }
        return array_list;

    }
}
