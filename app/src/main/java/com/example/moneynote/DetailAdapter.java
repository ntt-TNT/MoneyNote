package com.example.moneynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneynote.Bean.ItemBean;
import com.example.moneynote.Bean.MoneyBean;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "DetailAdapter";
    private List<ItemBean> itemBeanList = new ArrayList<>();

    public DetailAdapter(List<ItemBean> itemBeanList){
        this.itemBeanList = itemBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item,parent,false);
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemBean itemBean = itemBeanList.get(position);

        TextView detail_remark = holder.itemView.findViewById(R.id.detail_remark);
        TextView detail_spend = holder.itemView.findViewById(R.id.detail_spend);
        TextView detail_date = holder.itemView.findViewById(R.id.detail_date);

        detail_remark.setText(itemBean.getRemark());
        detail_spend.setText(itemBean.getSpend()+"");
        detail_date.setText(itemBean.getDate());
    }

    @Override
    public int getItemCount() {
        return itemBeanList.size();
    }
}
