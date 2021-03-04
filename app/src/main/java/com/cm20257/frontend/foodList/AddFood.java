package com.cm20257.frontend.foodList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cm20257.frontend.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddFood extends Fragment {

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

        EditText foodNameEt = view.findViewById(R.id.editFoodName);
        EditText foodQuantityEt = view.findViewById(R.id.editFoodQuantity);
        EditText foodDateEt = view.findViewById(R.id.editExpiryDate);


        view.findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.UK);
                //Retrieve user input and put into array foodInfo
                String foodName = foodNameEt.getText().toString();
                Float foodQuantity;
                try {
                    foodQuantity = Float.parseFloat(foodQuantityEt.getText().toString());
                }
                catch (NumberFormatException ignored) {
                    foodQuantity = null;
                }
                long foodDate;
                try {
                    String foodDateText = foodDateEt.getText().toString();
                    if (!foodDateText.equals("")) {
                        foodDate = df.parse(foodDateText).getTime();
                    }
                    else {
                        foodDate = 0L;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    foodDate = -1L;
                }
                String unit = spinner.getSelectedItem().toString();
                if(foodName.equals("")){
                    Toast toast1 = Toast.makeText(getContext(), "Enter Food Name to Add", Toast.LENGTH_SHORT);
                    toast1.show();
                } else if(foodQuantity == null) {
                    Toast toast2 = Toast.makeText(getContext(), "Enter Food Quantity to Add", Toast.LENGTH_SHORT);
                    toast2.show();
                } else if (foodDate == -1L) {
                    Toast toast0 = Toast.makeText(getContext(), "Please Enter Valid Date", Toast.LENGTH_SHORT);
                    toast0.show();
                } else {
                    Bundle result = new Bundle();

                    result.putString("name", foodName);
                    result.putFloat("quantity", foodQuantity);
                    result.putString("unit", unit);
                    result.putLong("date", foodDate);

                    //RESET WIDGETS
                    foodNameEt.setText("");
                    spinner.setSelection(0);
                    //go back to first fragment
                    NavHostFragment.findNavController(AddFood.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                    getParentFragmentManager().setFragmentResult("addRequestKey", result);
                }
            }
        });
    }
}