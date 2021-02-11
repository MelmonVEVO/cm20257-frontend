package com.cm20257.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RcyAdapter extends RecyclerView.Adapter<RcyAdapter.MyViewHolder>{

    String data1[][];
    Context context;

    public int selectedItem;

    private OnItemListener monItemListener;

    public RcyAdapter(Context ct, String[][] s1, OnItemListener onItemListener){
        context = ct;
        data1 = s1;
        this.monItemListener = onItemListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public RcyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fooditem, parent, false);
        return new MyViewHolder(view, monItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RcyAdapter.MyViewHolder holder, int position) {
        if(data1[position][2] != null) {
            holder.myText1.setText(data1[position][0]);
            if (data1[position][2].equals("Quantity")) {
                holder.myText2.setText("Quantity: " + data1[position][1]);
            } else {
                holder.myText2.setText("Quantity: " + data1[position][1] + data1[position][2]);
            }
        } else {
            holder.myText1.setText(data1[position][0]);
            holder.myText2.setText(data1[position][2]);
            holder.selectedCheck.setVisibility(View.GONE);

        }
        //holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.purple_500));

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView myText1;
        TextView myText2;
        CheckBox selectedCheck;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.foodText);
            myText2 = itemView.findViewById(R.id.quantityText);
            selectedCheck = itemView.findViewById(R.id.selectedCheck);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            selectedCheck = itemView.findViewById(R.id.selectedCheck);
            if(selectedCheck.getVisibility() != View.GONE) {
                if (selectedCheck.isChecked()) {
                    selectedCheck.setChecked(false);
                } else {
                    selectedCheck.setChecked(true);
                }
            }

            selectedItem = getAdapterPosition();
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}