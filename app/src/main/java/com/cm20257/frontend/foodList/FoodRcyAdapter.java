package com.cm20257.frontend.foodList;

import android.annotation.SuppressLint;
import android.content.Context;
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


    public int selectedItem;

    private final OnItemListener onItemListener;

    public FoodRcyAdapter(Context ct, OnItemListener onItemListener){
        context = ct;
        this.onItemListener = onItemListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public FoodRcyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fooditem, parent, false);
        return new MyViewHolder(view, onItemListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodRcyAdapter.MyViewHolder holder, int position) {
        DateFormat df = DateFormat.getDateInstance();
        Food current = cache.get(position);
        holder.foodNameText.setText(current.foodName);
        holder.foodQuantityText.setText(current.quantity + " " + current.quantityUnit);
        holder.foodDateText.setText(df.format(current.expiration));
    }

    @Override
    public int getItemCount() {
        return cache.size();
    }

    void setFood(List<Food> food) {
        this.cache = food;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView foodNameText;
        TextView foodQuantityText;
        TextView foodDateText;
        CheckBox selectedCheck;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            foodNameText = itemView.findViewById(R.id.foodText);
            foodQuantityText = itemView.findViewById(R.id.quantityText);
            foodDateText = itemView.findViewById(R.id.dateText);
            selectedCheck = itemView.findViewById(R.id.selectedCheck);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            // This inverts the checkbox when a food item is clicked.
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