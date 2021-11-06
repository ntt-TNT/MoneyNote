package com.example.moneynote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.moneynote.Bean.MoneyBean;

public class MoneySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "MoneySQLiteOpenHelper";
    public static String db_name = "Money";
    public static String table1 = "Types";
    public static String table2 = "DetailList";

    public static String table1_column1 = "type_id";
    public static String table1_column2 = "type";
    public static String table1_column3 = "spend";
    public static String table1_column4 = "remain";
    public static String table1_column5 = "sum";

    public static String table2_column1 = "type_id";
    public static String table2_column2 = "type";
    public static String table2_column3 = "spend";
    public static String table2_column4 = "remark";
    public static String table2_column5 = "date";

    private String CREATE_WORDS1 = "create table " + table1 + "(" + table1_column1 + " INTEGER PRIMARY KEY autoincrement NOT NULL," +
            table1_column2 + " text," + table1_column3 + " INTEGER," + table1_column4 + " INTEGER," + table1_column5 + " INTEGER )";
    private String CREATE_WORDS2 = "create table " + table2 + "(" + table2_column1 + " INTEGER PRIMARY KEY autoincrement NOT NULL," +
            table2_column2 + " text," + table2_column3 + " INTEGER," + table2_column4 + " text," + table2_column5 + " text )";


    private final static String SQL_DELETE_DATABASE_TABLE1 = "DROP TABLE IF EXISTS "+ table1;
    private final static String SQL_DELETE_DATABASE_TABLE2 = "DROP TABLE IF EXISTS "+ table2;

    private static boolean op = true;


    public MoneySQLiteOpenHelper(Context context, String db_name,
                                 SQLiteDatabase.CursorFactory factory,
                                 int version) {
        super(context, db_name, factory, version);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        db.execSQL(CREATE_WORDS1);
        db.execSQL(CREATE_WORDS2);

        if (op){
            ContentValues values = new ContentValues();
            values.put(MoneySQLiteOpenHelper.table1_column2, "总金额");
            values.put(MoneySQLiteOpenHelper.table1_column3, 0);
            values.put(MoneySQLiteOpenHelper.table1_column4, 0);
            values.put(MoneySQLiteOpenHelper.table1_column5, 0);
            db.insert(MoneySQLiteOpenHelper.table1,null,values);
            values.clear();
            op = false;
            Log.d(TAG, "onCreate: ");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        db.execSQL(SQL_DELETE_DATABASE_TABLE1);
        db.execSQL(SQL_DELETE_DATABASE_TABLE2);
        onCreate(db);
    }
}
