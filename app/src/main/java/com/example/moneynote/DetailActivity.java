package com.example.moneynote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneynote.Bean.ItemBean;
import com.example.moneynote.Bean.MoneyBean;
import com.example.moneynote.db.ItemAction;
import com.example.moneynote.db.TypeAction;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private List<ItemBean> itemBeanList;
    private List<MoneyBean> moneyBeanList;
    private RecyclerView detailListView;
    private DetailAdapter detailAdapter;
    private ItemAction itemAction;
    private TypeAction typeAction;

    private static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent!=null){
            type = intent.getStringExtra("type");
            Log.d(TAG, "onCreate: "+type);
        }

        itemAction = new ItemAction(this);
        if (itemAction.select(type)!=null){
            itemBeanList = itemAction.select(type);
        }

        typeAction = new TypeAction(this);
        if (typeAction.getAll()!=null){
            moneyBeanList = typeAction.getAll();
        }

        detailListView = findViewById(R.id.detail_list);
        detailAdapter = new DetailAdapter(itemBeanList);
        detailListView.setAdapter(detailAdapter);
        detailListView.setLayoutManager(new LinearLayoutManager(this));


        initSumMoney();

        //返回
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void initSumMoney(){
        MoneyBean moneySum = typeAction.select(type);
        TextView type_detail = findViewById(R.id.type_detail);
        TextView spend_detail = findViewById(R.id.spend_detail);
        EditText sum_detail = findViewById(R.id.sum_detail);
        type_detail.setText(type);
        spend_detail.setText(moneySum.getSpendMoney()+"");
        sum_detail.setText(moneySum.getSumMoney()+"");
        ProgressBar progressBar_detail = findViewById(R.id.progressBar_detail);
        progressBar_detail.setProgress((int) moneySum.getSpendMoney());
        progressBar_detail.setMax((int) moneySum.getSumMoney());
        sum_detail.clearFocus();

        sum_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                double sum = Double.valueOf(sum_detail.getText().toString());
                if (b){
//                    Log.d(TAG, "onFocus");
                    progressBar_detail.setMax((int) sum);
                }else {
                    double mainSumMoney  = moneyBeanList.get(0).getSumMoney();
                    double sumMoney = 0-typeAction.select(type).getSumMoney();
                    for (int i=1; i<moneyBeanList.size(); i++){
                        Log.d(TAG, moneyBeanList.get(i).getType()+moneyBeanList.get(i).getSumMoney());
                        sumMoney += moneyBeanList.get(i).getSumMoney();
                        Log.d(TAG, "SumMoney"+sumMoney);
                    }
                    if ((sumMoney +Double.valueOf(sum_detail.getText().toString()))>mainSumMoney){
                        Toast.makeText(view.getContext(), "金额数过大",Toast.LENGTH_LONG).show();
                    }else {
//                    Log.d(TAG, "loseFocus");
                        progressBar_detail.setMax((int) sum);
                        typeAction.updateSumType(type,Double.valueOf(sum_detail.getText().toString()));

                        //更新
                        moneyBeanList = typeAction.getAll();
                        for (int i = 0; i<moneyBeanList.size();i++){
                            Log.d(TAG, "onFocusChange: "+i+moneyBeanList.get(i).getType()+moneyBeanList.get(i).getSumMoney());
                        }

                    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}