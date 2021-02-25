package com.cm20257.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cm20257.frontend.cacheUtils.Food;

import java.util.List;

public class viewFood extends Fragment implements FoodRcyAdapter.OnItemListener {

    RecyclerView foodRecycler;
    FoodRcyAdapter adapter;
    private FoodViewModel vm;
//    LinkedList foodList = new LinkedList();

    int selectedPositions[] = new int[] {9999};
    int selectedIndex = 0;

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("addRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Food created = new Food();
                created.foodName = result.getString("name");
                created.quantity = result.getFloat("quantity");
                created.quantityUnit = result.getString("unit");
                created.expiration = result.getLong("expiration");
                vm.insert(created);
            }
        });
    }

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
        //listTest(view);

        foodRecycler = view.findViewById(R.id.foodRecycler);
        adapter = new FoodRcyAdapter(getActivity(), this);
        foodRecycler.setAdapter(adapter);
        foodRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodRecycler.addItemDecoration(new DividerItemDecoration(foodRecycler.getContext(), DividerItemDecoration.VERTICAL));

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(viewFood.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        }); // THIS IS WHERE ADDFOOD IS CALLED

        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPositions[0] == 9999){
                    Toast toast = Toast.makeText(getContext(), "Select Items to Delete", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //MainActivity.foodList.remove(selectedPositions);
                    for (int selectedPosition : selectedPositions) {
                        vm.remove(vm.allFoods.getValue().get(selectedPosition));
                    }
                    //updateRecycler(MainActivity.foodList.get(), view);
                    selectedPositions = new int[]{9999};
                    selectedIndex = 0;
                }
            }
        });

        vm = new ViewModelProvider(this).get(FoodViewModel.class);
        vm.allFoods.observe(getViewLifecycleOwner(), obs);
    }

    //private void listTest(View view){
    //    updateRecycler(MainActivity.foodList.get(),view);
    //}

    private void updateRecycler(String[][] s1, View view){


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

    final Observer<List<Food>> obs = new Observer<List<Food>>() {
        @Override
        public void onChanged(List<Food> foods) {
            adapter.setFood(foods);
        }
    };

}