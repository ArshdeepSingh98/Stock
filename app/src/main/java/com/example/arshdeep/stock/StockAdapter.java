package com.example.arshdeep.stock;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arshdeep on 7/9/2017.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder>{
    private List<Stock> stocks;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Symbol , Name , ChangeinPercent,Change;
        public MyViewHolder(View view){
            super(view);
            Symbol = (TextView) view.findViewById(R.id.Symbol);
            Name = (TextView) view.findViewById(R.id.Name);
            Change = (TextView) view.findViewById(R.id.Change);
            ChangeinPercent = (TextView) view.findViewById(R.id.ChangeinPercent);
        }
    }

    public StockAdapter(List <Stock> stocks){
        this.stocks = stocks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item , parent , false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.Symbol.setText(stock.Symbol);
        holder.Name.setText(stock.Name);
        holder.Change.setText(stock.Change);
        holder.ChangeinPercent.setText(stock.ChangeinPercent);
        if(holder.ChangeinPercent.getText().subSequence(0,1).equals("+")){
            holder.ChangeinPercent.setTextColor(Color.parseColor("#ff99cc00"));
        }else{
            holder.ChangeinPercent.setTextColor(Color.parseColor("#ffff4444"));
        }
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }
}
