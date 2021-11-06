package com.example.moneynote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moneynote.Bean.ItemBean;
import com.example.moneynote.Bean.MoneyBean;
import com.example.moneynote.db.ItemAction;
import com.example.moneynote.db.TypeAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
    private List<MoneyBean> moneyBeanList;
    private List<ItemBean> itemBeanList;
    private RecyclerView typeListView;
    private ChooseTypeAdapter chooseTypeAdapter;
    private TypeAction typeAction;
    private ItemAction itemAction;

    private ImageButton send_btn;
    private EditText spend_et;
    private EditText remark_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        typeAction = new TypeAction(this);
        if (typeAction.getAll()!=null){
            moneyBeanList = typeAction.getAll();
        }

        itemAction = new ItemAction(this);
        if (itemAction.getAll()!=null){
            itemBeanList = itemAction.getAll();
        }

        typeListView = findViewById(R.id.type_choose_list);
        chooseTypeAdapter = new ChooseTypeAdapter(moneyBeanList);
        typeListView.setAdapter(chooseTypeAdapter);
        typeListView.setLayoutManager(new LinearLayoutManager(this));

        spend_et = findViewById(R.id.spend);
        remark_et = findViewById(R.id.remark);
        send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!spend_et.getText().toString().equals("")){
                    ItemBean itemBean = new ItemBean();
                    String type = moneyBeanList.get(ChooseTypeAdapter.index).getType();
                    double spend = Double.valueOf(spend_et.getText().toString());
                    if (spend<=typeAction.select(type).getRemainMoney()){
                        itemBean.setType(type);
                        itemBean.setSpend(spend);
                        itemBean.setRemark(remark_et.getText().toString());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        itemBean.setDate(format.format(new Date()));
                        itemAction.saveTypes(itemBean);

                        typeAction.updateSpend(type,spend);
                        typeAction.updateSpend("总金额",spend);

                        Intent intent = new Intent(AddItemActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(AddItemActivity.this,"超出剩余金额，"+type+"还剩余"+typeAction.select(type).getRemainMoney(),Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(AddItemActivity.this,"请输入花费金额",Toast.LENGTH_LONG).show();
                }

                if (itemAction.getAll()!=null){
                    itemBeanList = itemAction.getAll();
                    for (int i=0; i<itemBeanList.size(); i++){
                        Log.d(TAG, itemBeanList.get(i).getType()+itemBeanList.get(i).getDate());
                    }
                }
            }
        });

        //返回
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddItemActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}