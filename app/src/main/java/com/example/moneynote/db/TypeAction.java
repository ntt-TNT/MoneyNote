package com.example.moneynote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.moneynote.Bean.MoneyBean;

import java.util.ArrayList;
import java.util.List;

public class TypeAction {
    private static String TAG = "TypeAction";

    private static TypeAction typeAction;

    private SQLiteDatabase db;
    private List<MoneyBean> moneyBeanList = new ArrayList<>();

    public TypeAction(Context context){
        MoneySQLiteOpenHelper helper = new MoneySQLiteOpenHelper(context,MoneySQLiteOpenHelper.db_name,null,1);
        db = helper.getWritableDatabase();
    }

    public List<MoneyBean> getAll(){
        moneyBeanList.clear();
        Cursor cursor = db.query(MoneySQLiteOpenHelper.table1, null, null,null,null,null,null);

        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do {
                    MoneyBean moneyBean = new MoneyBean();
                    moneyBean.setType(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column2)));
                    moneyBean.setSpendMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column3)));
                    moneyBean.setRemainMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column4)));
                    moneyBean.setSumMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column5)));
                    Log.d(TAG, "getAll: "+moneyBean.getType()+moneyBean.getSpendMoney()+"  "+moneyBean.getRemainMoney()+"  "+moneyBean.getSumMoney());
                    moneyBeanList.add(moneyBean);
                }while (cursor.moveToNext());
            }
        }else {
            Log.d(TAG, "数据库为空");
        }
        cursor.close();
        return moneyBeanList;
    }

    public boolean saveTypes(MoneyBean moneyBean){
        if (moneyBean.getType()!=null){
            ContentValues values = new ContentValues();
            values.put(MoneySQLiteOpenHelper.table1_column2, moneyBean.getType());
            values.put(MoneySQLiteOpenHelper.table1_column3, moneyBean.getSpendMoney());
            values.put(MoneySQLiteOpenHelper.table1_column4, moneyBean.getRemainMoney());
            values.put(MoneySQLiteOpenHelper.table1_column5, moneyBean.getSumMoney());
            db.insert(MoneySQLiteOpenHelper.table1,null,values);
            values.clear();
            return true;
        }else {
            return false;
        }
    }

    public boolean updateSumMain(double sum){
        ContentValues values = new ContentValues();
        values.put(MoneySQLiteOpenHelper.table1_column5,sum);
        db.update(MoneySQLiteOpenHelper.table1,values,MoneySQLiteOpenHelper.table1_column2+" =?",new String[]{"总金额"});
        Log.d(TAG, moneyBeanList.get(0).getSpendMoney()+"");
        values.clear();
        return true;
    }

    public boolean updateRemainMian(){
        MoneyBean moneyBean = select("总金额");
        ContentValues values = new ContentValues();
        values.put(MoneySQLiteOpenHelper.table1_column4,moneyBean.getSumMoney()-moneyBean.getSpendMoney());
        Log.d(TAG, "updateRemainMian: 剩余金额为"+(moneyBean.getSumMoney()-moneyBean.getSpendMoney()));
        db.update(MoneySQLiteOpenHelper.table1,values,MoneySQLiteOpenHelper.table1_column2+" =?",new String[]{"总金额"});
        values.clear();
        return true;
    }

    public boolean updateSumType(String type, double sum){
        ContentValues values = new ContentValues();
        values.put(MoneySQLiteOpenHelper.table1_column5,sum);
        db.update(MoneySQLiteOpenHelper.table1,values,MoneySQLiteOpenHelper.table1_column2+" =?",new String[]{type});
        values.clear();
        return true;
    }


    public boolean updateSpend(String type, double spend){
        Log.d(TAG, "updateSpend: "+type+spend);
        MoneyBean moneyBean = select(type);
        Log.d(TAG, "updateSpend: "+moneyBean.getType()+moneyBean.getSpendMoney());
        moneyBean.setSpendMoney(spend+moneyBean.getSpendMoney());
        moneyBean.setRemainMoney(moneyBean.getRemainMoney()-spend);
        ContentValues values = new ContentValues();
        values.put(MoneySQLiteOpenHelper.table1_column3,moneyBean.getSpendMoney());
        values.put(MoneySQLiteOpenHelper.table1_column4,moneyBean.getRemainMoney());
        db.update(MoneySQLiteOpenHelper.table1,values,MoneySQLiteOpenHelper.table1_column2+" =?",new String[]{type});
        Log.d(TAG, moneyBean.getSpendMoney()+"");
        values.clear();
        return true;
    }

    public MoneyBean select(String type){
        Cursor cursor = db.query(MoneySQLiteOpenHelper.table1, new String[]{MoneySQLiteOpenHelper.table1_column2,MoneySQLiteOpenHelper.table1_column3,MoneySQLiteOpenHelper.table1_column4,MoneySQLiteOpenHelper.table1_column5}, MoneySQLiteOpenHelper.table1_column2+" =? ",new String[]{type},null,null,null);

        if (cursor.getCount()==1){
            cursor.moveToFirst();
            MoneyBean moneyBean = new MoneyBean();
            moneyBean.setType(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column2)));
            moneyBean.setSpendMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column3)));
            moneyBean.setRemainMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column4)));
            moneyBean.setSumMoney(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table1_column5)));
            Log.d(TAG, "select: "+moneyBean.getType()+moneyBean.getSpendMoney());
            return moneyBean;
        }else {
            Log.d(TAG, "数据库为空");
        }
        cursor.close();
        return null;
    }

}
