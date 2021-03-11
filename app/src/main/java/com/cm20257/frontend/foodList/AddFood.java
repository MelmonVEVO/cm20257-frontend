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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddFood extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_food, container, false);
    }

    // Here be dragons.
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
                    result.putFloat("quantity", foodQuantity.intValue());
                    result.putString("unit", unit);
                    result.putLong("date", foodDate);

                    // post the data to the server
                    String addFoodUrl = "http://192.168.1.16:8080/account/add-food";
                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    SimpleDateFormat jsonDF = new SimpleDateFormat("dd-MM-yy");
                    JSONObject foodRequestDetails = new JSONObject();
                    try {
                        foodRequestDetails.put("name", foodName);
                        foodRequestDetails.put("quantity", foodQuantity);
                        foodRequestDetails.put("quantityType", unit);
                        foodRequestDetails.put("expiryDate", jsonDF.format(new Date(foodDate)));
                        //int foodID;
                        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, addFoodUrl, foodRequestDetails, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    System.out.println("TEST");
                                    int foodID = response.getInt("id");
                                    result.putInt("id", foodID);
                                    getParentFragmentManager().setFragmentResult("addRequestKey", result);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() { // this is for putting headers in the request
                                Map<String, String> params = new HashMap<>();
                                params.put("Content-Type", "application/json");
                                params.put("token", UserHandler.getToken());
                                return params;
                            }
                        };
                        queue.add(loginRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //RESET WIDGETS
                    foodNameEt.setText("");
                    spinner.setSelection(0);
                    //go back to first fragment
                    NavHostFragment.findNavController(AddFood.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);


                }
            }
        });
    }
}