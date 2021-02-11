package com.cm20257.frontend;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class addFood extends Fragment {

    String foodData[];

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_food, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spinner code
        Spinner spinner = view.findViewById(R.id.unitSpinner);
        //sets content of the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText foodNameEt = view.findViewById(R.id.editTextItemName);
        EditText foodQuantityEt = view.findViewById(R.id.editTextNumber);
        EditText foodDateEt = view.findViewById(R.id.editTextNumber);

        CheckBox expDate = view.findViewById(R.id.expDatCheck);

        view.findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Retrieve user input and put into array foodInfo
                String foodName = foodNameEt.getText().toString();
                String foodQuantity = foodQuantityEt.getText().toString();
                String foodDate = foodDateEt.getText().toString();
                String unit = spinner.getSelectedItem().toString();
                Boolean noExpDate = expDate.isChecked();
                if(foodName.equals("")){
                    Toast toast1 = Toast.makeText(getContext(), "Enter Food Name to Add", Toast.LENGTH_SHORT);
                    toast1.show();
                } else if(foodQuantity.equals("")) {
                    Toast toast2 = Toast.makeText(getContext(), "Enter Food Quantity to Add", Toast.LENGTH_SHORT);
                    toast2.show();
                } else if((!noExpDate)) {
                    Toast toast3 = Toast.makeText(getContext(), "Enter Expiration Date to Add", Toast.LENGTH_SHORT);
                    toast3.show();
                } else {
                    if(!noExpDate && foodDate.equals("")) {
                        // PACK DATA
                        foodData = new String[]{foodName, foodQuantity, unit, foodDate};
                    } else {
                        foodData = new String[]{foodName, foodQuantity, unit, "NaN"};
                    }

                    MainActivity.foodList.add(foodData);

                    //RESET WIDGETS
                    foodNameEt.setText("");
                    spinner.setSelection(0);
                    //go back to first fragment
                    NavHostFragment.findNavController(addFood.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            }
        });
    }
}