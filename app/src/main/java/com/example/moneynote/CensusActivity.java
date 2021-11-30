package com.example.moneynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneynote.Bean.ItemBean;
import com.example.moneynote.Bean.MoneyBean;
import com.example.moneynote.Bean.PieModel;
import com.example.moneynote.db.ItemAction;
import com.example.moneynote.db.TypeAction;

import java.util.ArrayList;
import java.util.List;

public class CensusActivity extends AppCompatActivity {
    private static final String TAG = "CensusActivity";
    private PieChartView id_pie_chart;
    private TypeAction typeAction;

    private TextView id_tv_1, id_tv_2, id_tv_3, id_tv_4;

    private List<PieModel> pieModelList	= new ArrayList<>();

    private List<Integer> colorList	= new ArrayList<>();

    private List<MoneyBean> moneyBeanList;
    private RecyclerView allListView;
    private DetailAdapter detailAdapter;
    private List<ItemBean> allItemBeanList;
    private ItemAction itemAction;

    private LinearLayout home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census);

//        id_tv_1 = findViewById(R.id.id_tv_1);
//        id_tv_2 = findViewById(R.id.id_tv_2);
//        id_tv_3 = findViewById(R.id.id_tv_3);
//        id_tv_4 = findViewById(R.id.id_tv_4);
        id_pie_chart = findViewById(R.id.id_pie_chart);
        typeAction = new TypeAction(this);
        if (typeAction.getAll()!=null){
            moneyBeanList = typeAction.getAll();
        }
        itemAction = new ItemAction(this);
        if (itemAction.getAll()!=null){
            allItemBeanList = itemAction.getAll();
        }

        List<Float> mList = new ArrayList<>();
        for (int i=1; i<moneyBeanList.size(); i++){
            mList.add((float)moneyBeanList.get(i).getSpendMoney()/(float)moneyBeanList.get(0).getSumMoney());
            Log.d(TAG, moneyBeanList.get(i).getSpendMoney()+"");
        }
        mList.add((float)moneyBeanList.get(0).getRemainMoney()/(float)moneyBeanList.get(0).getSumMoney());
        Log.d(TAG, "onCreate: "+(float)moneyBeanList.get(0).getRemainMoney()/(float)moneyBeanList.get(0).getSumMoney());

        ColorRandom colorRandom = new ColorRandom(10);
        for (int i = 0; i < moneyBeanList.size(); i++) {
            int colors = (int) colorRandom.getColors().get(i);
            pieModelList.add(new PieModel(colors, mList.get(i)));
        }

        id_pie_chart.setData(pieModelList);
        id_pie_chart.startAnima();



        allListView = findViewById(R.id.all_list);
        detailAdapter = new DetailAdapter(allItemBeanList);
        allListView.setAdapter(detailAdapter);
        allListView.setLayoutManager(new LinearLayoutManager(this));

        home_btn = findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CensusActivity.this,MainActivity .class);
                startActivity(intent);
                finish();
            }
        });
    }

}