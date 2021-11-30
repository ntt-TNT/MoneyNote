package com.example.moneynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneynote.Bean.MoneyBean;
import com.example.moneynote.db.TypeAction;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<MoneyBean> moneyBeanList;
    private RecyclerView moneyListView;
    private TypeAdapter typeAdapter;
    private TypeAction typeAction;
    private LinearLayout census_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        typeAction = new TypeAction(this);
        if (typeAction.getAll()!=null){
            moneyBeanList = typeAction.getAll();
        }

//        moneyBeanList = MoneyUtil.getMoneyData(this);
        moneyListView = findViewById(R.id.type_list);
        typeAdapter = new TypeAdapter(moneyBeanList);
//        Log.d(TAG, "onCreate: "+moneyBeanList.get(0).getType());
        moneyListView.setAdapter(typeAdapter);
        moneyListView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onCreate: "+typeAdapter.getItemCount());

//        moneyBeanList = typeAction.getAll();
        for (int i = 0; i<moneyBeanList.size();i++){
            Log.d(TAG, "总列表："+i+moneyBeanList.get(i).getType()+moneyBeanList.get(i).getSpendMoney());
        }

        initSumMoney();
        census_btn = findViewById(R.id.census);
        census_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CensusActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void initSumMoney(){
        MoneyBean moneySum = moneyBeanList.get(0);
        TextView spend_main = findViewById(R.id.spend_main);
        EditText sum_main = findViewById(R.id.sum_main);
        spend_main.setText(moneySum.getSpendMoney()+"");
        sum_main.setText(moneySum.getSumMoney()+"");
        ProgressBar progressBar_main = findViewById(R.id.progressBar_main);
        progressBar_main.setProgress((int) moneySum.getSpendMoney());
        progressBar_main.setMax((int) moneySum.getSumMoney());
        sum_main.clearFocus();
        sum_main.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                double sum = Double.valueOf(sum_main.getText().toString());
                if (b){
//                    Log.d(TAG, "onFocus");
                    progressBar_main.setMax((int) sum);
                }else {
//                    Log.d(TAG, "loseFocus");
                    progressBar_main.setMax((int) sum);
                    typeAction.updateSumMain(Double.valueOf(sum_main.getText().toString()));
                    typeAction.updateRemainMian();

                    //更新
                    moneyBeanList = typeAction.getAll();
                    for (int i = 0; i<moneyBeanList.size();i++){
                        Log.d(TAG, "onFocusChange: "+i+moneyBeanList.get(i).getType()+moneyBeanList.get(i).getSumMoney());
                    }
                    moneyListView.setAdapter(typeAdapter);
                }
            }
        });



        ImageButton add_type_btn = findViewById(R.id.add_type_btn);
        add_type_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (moneyBeanList.size()>1){
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(MainActivity.this,"请添加分类(点击右上角)",Toast.LENGTH_LONG).show();
                }


            }
        });


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        //添加标题栏的确定按钮
        MenuItem item=menu.add(0,0,0,"添加");

        /**
         * setShowAsAction参数说明   MenuItem接口的一些常量
         * SHOW_AS_ACTION_ALWAYS   总是显示这个项目作为一个操作栏按钮。
         * SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW   此产品的动作视图折叠成一个正常的菜单项。
         * SHOW_AS_ACTION_IF_ROOM   显示此项目作为一个操作栏的按钮,如果系统有空间。
         * SHOW_AS_ACTION_NEVER     从不显示该项目作为一个操作栏按钮。
         * SHOW_AS_ACTION_WITH_TEXT  当这个项目是在操作栏中,始终以一个文本标签显示它,即使它也有指定一个图标。
         */
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//主要是这句话

        item.setIcon(R.drawable.add);//设置图标
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(moneyBeanList.get(moneyBeanList.size()-1).getType()!=null){
                    MoneyBean newType = new MoneyBean();
                    moneyBeanList.add(newType);

                    typeAdapter.notifyItemInserted(moneyBeanList.size() - 1);
                    moneyListView.scrollToPosition(moneyBeanList.size() - 1);
                }


                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//                final View view_add_item = inflater.inflate(R.layout.type_list_add_item,null);
                View view_add_item = View.inflate(getApplicationContext(), R.layout.type_list_add_item, null);
                EditText type_add = view_add_item.findViewById(R.id.type_add_et);

                Log.d(TAG, "onClick: "+type_add.isFocusable());

                return true;
            }
        });

        return true;
    }

}