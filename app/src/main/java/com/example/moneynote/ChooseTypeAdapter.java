package com.example.moneynote;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneynote.Bean.MoneyBean;

import java.util.ArrayList;
import java.util.List;

public class ChooseTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ChooseTypeAdapter";
    private List<MoneyBean> moneyBeanList = new ArrayList<>();
    public static int index = 1;

    public ChooseTypeAdapter(List<MoneyBean> moneyBeanList){
        this.moneyBeanList = moneyBeanList;
        Log.d(TAG, "ChooseTypeAdapter");
    }

    @Override
    public int getItemViewType(int position) {
        if (moneyBeanList.get(position).getType().equals("总金额")){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_choose_item,parent,false);
            return new RecyclerView.ViewHolder(view) {};
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty,parent,false);
            return new RecyclerView.ViewHolder(view) {};
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==1){
            MoneyBean moneyBean = moneyBeanList.get(position);

            RadioButton button = holder.itemView.findViewById(R.id.type_choose);
            button.setText(moneyBean.getType());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            if (index==position){
                button.setChecked(true);
            }else {
                Log.d(TAG, "onBindViewHolder: "+position);
                button.setChecked(false);
            }
        }

    }

    @Override
    public int getItemCount() {
        return moneyBeanList.size();
    }
}
