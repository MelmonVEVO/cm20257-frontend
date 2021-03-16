package com.cm20257.frontend.foodList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm20257.frontend.R;
import com.cm20257.frontend.cacheUtils.Food;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class FoodRcyAdapter extends RecyclerView.Adapter<FoodRcyAdapter.MyViewHolder>{

    private List<Food> cache = Collections.emptyList();
    Context context;

    public FoodRcyAdapter(Context ct){
        context = ct;
    }

    @NonNull
    @Override
    public FoodRcyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Invoked by the layout manager to create new views
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fooditem, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodRcyAdapter.MyViewHolder holder, int position) {
        // Fills the contents of a view with data
        DateFormat df = DateFormat.getDateInstance();
        Food current = cache.get(position);
        holder.foodNameText.setText(current.foodName);
        holder.foodQuantityText.setText(current.quantity + " " + current.quantityUnit);
        if (current.expiration < 0) {
            holder.foodDateText.setText("No Expiry");
        } else {
            holder.foodDateText.setText(df.format(current.expiration));
        }
        holder.id = current.uid;
    }

    @Override
    public int getItemCount() {
        return cache.size();
    }

    void setFood(List<Food> food) {
        this.cache = food;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Layout to class
        TextView foodNameText;
        TextView foodQuantityText;
        TextView foodDateText;
        CheckBox selectedCheck;
        public int id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameText = itemView.findViewById(R.id.foodText);
            foodQuantityText = itemView.findViewById(R.id.quantityText);
            foodDateText = itemView.findViewById(R.id.dateText);
            selectedCheck = itemView.findViewById(R.id.selectedCheck);
        }
    }
}