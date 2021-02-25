package com.cm20257.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm20257.frontend.cacheUtils.Food;

import java.util.Collections;
import java.util.List;

public class FoodRcyAdapter extends RecyclerView.Adapter<FoodRcyAdapter.MyViewHolder>{

    //String data1[][];
    private List<Food> cache = Collections.emptyList();
    Context context;


    public int selectedItem;

    private final OnItemListener monItemListener;

    public FoodRcyAdapter(Context ct, OnItemListener onItemListener){
        context = ct;
        //data1 = s1;
        this.monItemListener = onItemListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public FoodRcyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fooditem, parent, false);
        return new MyViewHolder(view, monItemListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodRcyAdapter.MyViewHolder holder, int position) {
        // old string-based binder
        /*if(data1[position][2] != null) {
            holder.foodNameText.setText(data1[position][0]);
            if (data1[position][2].equals("Quantity")) {
                holder.foodQuantityText.setText("Quantity: " + data1[position][1]);
            } else {
                holder.foodQuantityText.setText("Quantity: " + data1[position][1] + data1[position][2]);
            }
        } else {
            holder.foodNameText.setText(data1[position][0]);
            holder.foodQuantityText.setText(data1[position][2]);
            holder.selectedCheck.setVisibility(View.GONE);

        }*/
        //holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.purple_500));

        // new food class-based binder - this will be used once the database system is complete
        Food current = cache.get(position);
        holder.foodNameText.setText(current.foodName);
        holder.foodQuantityText.setText(current.quantity + " " + current.quantityUnit);

    }

    @Override
    public int getItemCount() {
        //return data1.length;
        return cache.size();
    }

    void setFood(List<Food> food) {
        this.cache = food;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView foodNameText;
        TextView foodQuantityText;
        CheckBox selectedCheck;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            foodNameText = itemView.findViewById(R.id.foodText);
            foodQuantityText = itemView.findViewById(R.id.quantityText);
            selectedCheck = itemView.findViewById(R.id.selectedCheck);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            selectedCheck = itemView.findViewById(R.id.selectedCheck);
            if(selectedCheck.getVisibility() != View.GONE) {
                selectedCheck.setChecked(!selectedCheck.isChecked());
            }

            selectedItem = getAdapterPosition();
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}