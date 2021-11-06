package com.example.moneynote.util;

import android.content.Context;

import com.example.moneynote.Bean.MoneyBean;

import java.util.ArrayList;
import java.util.List;

public class MoneyUtil {

    public static List<MoneyBean> getMoneyData(Context context){
        List<MoneyBean> list = new ArrayList<>();
        MoneyBean m = new MoneyBean();
        m.setType("总金额");
        m.setSumMoney(2000);
        m.setSpendMoney(1500);
        list.add(m);
        list.add(m);
        MoneyBean m1 = new MoneyBean();
        m1.setType("吃饭");
        m1.setSumMoney(1000);
        m1.setSpendMoney(500);
        list.add(m1);
        MoneyBean m2 = new MoneyBean();
        m2.setType("购物");
        m2.setSumMoney(500);
        m2.setSpendMoney(300);
        list.add(m2);
//        MoneyBean m3= new MoneyBean();
//        list.add(m3);
        return list;
    }
}
