package com.example.cm20257_addfood4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class viewFood extends Fragment implements RcyAdapter.OnItemListener {

    RecyclerView foodRecycler;
//    LinkedList foodList = new LinkedList();

    int selectedPositions[] = new int[] {9999};
    int selectedIndex = 0;

    View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_food, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        listTest(view);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(viewFood.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPositions[0] == 9999){
                    Toast toast = Toast.makeText(getContext(), "Select Items to Delete", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    MainActivity.foodList.remove(selectedPositions);
                    updateRecycler(MainActivity.foodList.get(), view);
                    selectedPositions = new int[]{9999};
                    selectedIndex = 0;
                }
            }
        });
    }

    private void listTest(View view){
        updateRecycler(MainActivity.foodList.get(),view);
    }

    private void updateRecycler(String[][] s1, View view){
        foodRecycler = view.findViewById(R.id.foodRecycler);
        RcyAdapter adapter = new RcyAdapter(getActivity(), s1, this);
        foodRecycler.setAdapter(adapter);
        foodRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void removeItemFromSelected(int position){
        for(int i=0; i<selectedPositions.length; i++){
            if(selectedPositions[i] == position){
                if (i > 0) {
                    for (int j=i; j<selectedPositions.length; j++){
                        selectedPositions[j-1] = selectedPositions[j];
                    }
                } else {
                    selectedPositions[i] = 9999;
                }
            }
        }
        selectedIndex--;
    }

    public void addItemToSelected(int position){
        //check if the index to add this index is in the array
        if (selectedIndex < selectedPositions.length) {
            selectedPositions[selectedIndex] = position;
        } else {
            //if not double the length of the selectedPositions array
            int temp[] = new int[1 + selectedPositions.length];
            for (int i = 0; i < selectedPositions.length; i++) {
                temp[i] = selectedPositions[i];
            }
            selectedPositions = temp;
            selectedPositions[selectedIndex] = position;
        }
        selectedIndex++;
    }

    @Override
    public void onItemClick(int position) {
        //what to do now you have clicked food item
        Log.d("ONCLICKXXX", Integer.toString(position));

        int duplicate = 0;
        for(int i=0; i<selectedPositions.length; i++){
            if(selectedPositions[i] == position){
                duplicate = 1;
            }
        }

        //if this is not a duplicate, add to the array
        if(duplicate == 0) {
            addItemToSelected(position);
        } else {
            //otherwise remove the original
            removeItemFromSelected(position);
        }
    }
}