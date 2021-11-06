package com.example.moneynote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.moneynote.Bean.ItemBean;
import com.example.moneynote.Bean.MoneyBean;

import java.util.ArrayList;
import java.util.List;

public class ItemAction {
    private static String TAG = "AddItemAction";

    private SQLiteDatabase db;
    private List<ItemBean> itemBeanList = new ArrayList<>();

    private static String COLUMN_TYPE = MoneySQLiteOpenHelper.table2_column2;
    private static String COLUMN_SPEND = MoneySQLiteOpenHelper.table2_column3;
    private static String COLUMN_REMARK = MoneySQLiteOpenHelper.table2_column4;
    private static String COLUMN_DATE = MoneySQLiteOpenHelper.table2_column5;

    public static final String ORDER_BY="date("+COLUMN_DATE+") desc";

    public ItemAction(Context context){
        MoneySQLiteOpenHelper helper = new MoneySQLiteOpenHelper(context,MoneySQLiteOpenHelper.db_name,null,1);
        db = helper.getWritableDatabase();
    }

    public List<ItemBean> getAll(){
        itemBeanList.clear();
        Cursor cursor = db.query(MoneySQLiteOpenHelper.table2, null, null,null,null,null,ORDER_BY);

        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do {
                    ItemBean itemBean = new ItemBean();
                    itemBean.setType(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column2)));
                    itemBean.setSpend(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column3)));
                    itemBean.setRemark(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column4)));
                    itemBean.setDate(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column5)));
                    Log.d(TAG, "getAll: "+itemBean.getType());
                    itemBeanList.add(itemBean);
                }while (cursor.moveToNext());
            }
        }else {
            Log.d(TAG, "数据库为空");
        }
        cursor.close();
        return itemBeanList;
    }

    public boolean saveTypes(ItemBean itemBean){
        if (itemBean.getType()!=null){
            ContentValues values = new ContentValues();
            values.put(MoneySQLiteOpenHelper.table2_column2, itemBean.getType());
            values.put(MoneySQLiteOpenHelper.table2_column3, itemBean.getSpend());
            values.put(MoneySQLiteOpenHelper.table2_column4, itemBean.getRemark());
            values.put(MoneySQLiteOpenHelper.table2_column5, itemBean.getDate());
            db.insert(MoneySQLiteOpenHelper.table2,null,values);
            values.clear();
            return true;
        }else {
            return false;
        }
    }

    public List<ItemBean> select(String type){
        itemBeanList.clear();
        Cursor cursor = db.query(MoneySQLiteOpenHelper.table2, new String[]{MoneySQLiteOpenHelper.table2_column2,MoneySQLiteOpenHelper.table2_column3,MoneySQLiteOpenHelper.table2_column4,MoneySQLiteOpenHelper.table2_column5}, MoneySQLiteOpenHelper.table2_column2+" =? ",new String[]{type},null,null,ORDER_BY);

        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do {
                    ItemBean itemBean = new ItemBean();
                    itemBean.setType(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column2)));
                    itemBean.setSpend(cursor.getDouble(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column3)));
                    itemBean.setRemark(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column4)));
                    itemBean.setDate(cursor.getString(cursor.getColumnIndex(MoneySQLiteOpenHelper.table2_column5)));
                    Log.d(TAG, "getAll: "+itemBean.getType());
                    itemBeanList.add(itemBean);
                }while (cursor.moveToNext());
            }
        }else {
            Log.d(TAG, "数据库为空");
        }
        cursor.close();
        return itemBeanList;
    }

}
