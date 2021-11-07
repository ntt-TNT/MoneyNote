package com.example.moneynote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneynote.Bean.MoneyBean;
import com.example.moneynote.db.ItemAction;
import com.example.moneynote.db.TypeAction;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {
    private static final String TAG = "TypeAdapter";
    private List<MoneyBean> moneyBeanList = new ArrayList<>();

    public TypeAdapter(List<MoneyBean> moneyBeanList){
        this.moneyBeanList = moneyBeanList;
        Log.d(TAG, "TypeAdapter: ");
    }

    @Override
    public int getItemViewType(int position) {
        if (moneyBeanList.get(position).getType()==null){
            return 0;
        }else if (!moneyBeanList.get(position).getType().equals("总金额")){
            return 1;
        }else {
            return 2;
        }
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_list_item,parent,false);

            return new TypeViewHolder(view);
        }else if(viewType==0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_list_add_item,parent,false);
            return new TypeViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty,parent,false);
            return new TypeViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        MoneyBean moneyBean = moneyBeanList.get(position);
        if (moneyBean.getType()!=null && !moneyBean.getType().equals("总金额")){
            TextView type = holder.itemView.findViewById(R.id.type);
            type.setText(moneyBean.getType());
            TextView spend = holder.itemView.findViewById(R.id.spend);
            spend.setText(moneyBean.getSpendMoney()+"");
            TextView sum = holder.itemView.findViewById(R.id.sum);
            sum.setText(moneyBean.getSumMoney()+"");
            ProgressBar progressBar = holder.itemView.findViewById(R.id.progressBar);
            progressBar.setMax((int)moneyBean.getSumMoney());
            progressBar.setProgress((int)moneyBean.getSpendMoney());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemAction itemAction = new ItemAction(holder.itemView.getContext());

                    Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
                    intent.putExtra("type",moneyBean.getType());
                    holder.itemView.getContext().startActivity(intent);

                    if (MainActivity.class.isInstance(holder.itemView.getContext())) {
                        // 转化为activity，然后finish就行了
                        MainActivity activity = (MainActivity) holder.itemView.getContext();
                        activity.finish();
                    }
                }
            });

        }else if (moneyBean.getType()==null){
            EditText type_add = holder.itemView.findViewById(R.id.type_add_et);
            EditText sum_add = holder.itemView.findViewById(R.id.sum_add);
            type_add.setText("");
            sum_add.setText("");

//            type_add.setFocusable(true);
//            type_add.setFocusableInTouchMode(true);
//            type_add.requestFocus();
//
//            //弹出软键盘
//            InputMethodManager inputManager = (InputMethodManager)type_add.getContext()
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.showSoftInput(type_add, 0);
            double sumMoney = 0;
            for (int i=1; i<moneyBeanList.size(); i++){
                Log.d(TAG, moneyBeanList.get(i).getType()+moneyBeanList.get(i).getSumMoney());
                sumMoney += moneyBeanList.get(i).getSumMoney();
                Log.d(TAG, "SumMoney"+sumMoney);
            }

            double finalSumMoney = sumMoney;
            double mainSumMoney  = moneyBeanList.get(0).getSumMoney();
            Log.d(TAG, "mainSumMoney"+mainSumMoney);
            type_add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Log.d(TAG, "onFocusChange: ");
                    if (b){
                        Log.d(TAG, "onFocus");
                    }else {
                        Log.d(TAG, "loseFocus");

                        if(!type_add.getText().toString().equals("") && !sum_add.getText().toString().equals("")){
                            if ((finalSumMoney +Double.valueOf(sum_add.getText().toString()))>mainSumMoney){
                                Toast.makeText(view.getContext(), "金额数过大",Toast.LENGTH_LONG).show();
                            }else {
                                moneyBeanList.remove(moneyBeanList.size()-1);
                                MoneyBean moneyBean = new MoneyBean();
                                moneyBean.setType(type_add.getText().toString());
                                moneyBean.setSumMoney(Double.valueOf(sum_add.getText().toString()));
                                moneyBean.setSpendMoney(0);
                                moneyBean.setRemainMoney(Double.valueOf(sum_add.getText().toString()));
                                moneyBeanList.add(moneyBean);

                                TypeAction typeAction = new TypeAction(view.getContext());
                                typeAction.saveTypes(moneyBean);
                            }

                        }else if (type_add.getText().toString().equals("") && sum_add.getText().toString().equals("")){
                            moneyBeanList.remove(moneyBeanList.size()-1);
                        }
                    }
                }
            });
            sum_add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    Log.d(TAG, "onFocusChange: ");
                    if (b){
                        Log.d(TAG, "onFocus");
                    }else {
                        Log.d(TAG, "loseFocus");

                        if(!type_add.getText().toString().equals("") && !sum_add.getText().toString().equals("")){
                            if ((finalSumMoney +Double.valueOf(sum_add.getText().toString()))>mainSumMoney){
                                Toast.makeText(view.getContext(), "金额数过大",Toast.LENGTH_LONG).show();
                            }else {
                                moneyBeanList.remove(moneyBeanList.size()-1);
                                MoneyBean moneyBean = new MoneyBean();
                                moneyBean.setType(type_add.getText().toString());
                                moneyBean.setSumMoney(Double.valueOf(sum_add.getText().toString()));
                                moneyBean.setSpendMoney(0);
                                moneyBean.setRemainMoney(Double.valueOf(sum_add.getText().toString()));
                                moneyBeanList.add(moneyBean);

                                TypeAction typeAction = new TypeAction(view.getContext());
                                typeAction.saveTypes(moneyBean);
                            }
                        }else if (type_add.getText().toString().equals("") && sum_add.getText().toString().equals("")){
                            moneyBeanList.remove(moneyBeanList.size()-1);
                        }
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return moneyBeanList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder{
        TextView tvType;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.type);
        }
    }
}
