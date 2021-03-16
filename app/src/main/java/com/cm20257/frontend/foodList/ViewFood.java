package com.cm20257.frontend.foodList;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;
import com.cm20257.frontend.cacheUtils.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewFood extends Fragment {

    private FoodViewModel vm;
    RecyclerView foodRecycler;
    FoodRcyAdapter adapter;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("addRequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Food created = new Food();
                created.uid = result.getInt("id");
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

        foodRecycler = view.findViewById(R.id.foodRecycler);
        adapter = new FoodRcyAdapter(getActivity());
        foodRecycler.setAdapter(adapter);
        foodRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodRecycler.addItemDecoration(new DividerItemDecoration(foodRecycler.getContext(), DividerItemDecoration.VERTICAL));

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ViewFood.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        }); // This is where AddFood is called

        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeSelectedItems();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        vm = new ViewModelProvider(this).get(FoodViewModel.class);
        vm.allFoods.observe(getViewLifecycleOwner(), obs);
        adapter.notifyDataSetChanged();

        refreshFood();
    }

    public List<Integer> get_id_of_selected_food() {

        List<Integer> selectedFoodsIDs = new ArrayList<>();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            FoodRcyAdapter.MyViewHolder holder = (FoodRcyAdapter.MyViewHolder) foodRecycler.findViewHolderForAdapterPosition(i);
            if (holder.selectedCheck.isChecked()) {
                selectedFoodsIDs.add(i);
            }
        }

        return selectedFoodsIDs;
    }

    public void removeSelectedItems() throws JSONException { // remove all items that have been checked

        List<Integer> selected = get_id_of_selected_food();

        for (int i : selected) {
            Map<String, Integer> foodToDelete = new HashMap<String, Integer>();
            FoodRcyAdapter.MyViewHolder hold = (FoodRcyAdapter.MyViewHolder) foodRecycler.findViewHolderForAdapterPosition(i);
            foodToDelete.put("id", hold.id);

            post_selected_items(foodToDelete);
            vm.remove(vm.allFoods.getValue().get(hold.getAdapterPosition()));
            adapter.notifyDataSetChanged();
        }

        if (selected.size() == 0) {
            Toast.makeText(getContext(), "No items selected, select an item to delete", Toast.LENGTH_LONG).show();
            return;
        }

        alert_deleted_item();
    }

    public void post_selected_items(Map<String, Integer> item_to_delete) {

        String deleteUrl = "http://10.0.2.2:8080/account/delete-food";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest delRequest = new JsonObjectRequest(Request.Method.POST, deleteUrl, new JSONObject(item_to_delete), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(UserHandler.getUsername());
                Log.d("RESPONSE", response.toString());
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

        queue.add(delRequest);
    }

    public void refreshFood(){

        vm.deleteAll();

        vm = new ViewModelProvider(this).get(FoodViewModel.class);
        vm.allFoods.observe(getViewLifecycleOwner(), obs);
        adapter.notifyDataSetChanged();

        Log.d("RESPONSE", "REFRESHED");

        String getFoodUrl = "http://10.0.2.2:8080/account/food";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest foodRequest = new JsonArrayRequest(Request.Method.GET, getFoodUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject handled;
                    try {
                        Log.d("RESPONSE", String.valueOf(response.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DateFormat jsonDF = new SimpleDateFormat("yy-MM-dd", Locale.UK);
                    try {
                        handled = response.getJSONObject(i);
                        long date = Objects.requireNonNull(jsonDF.parse(handled.getString("expiryDate"))).getTime();
                        Food toAdd = new Food();
                        toAdd.uid = handled.getInt("id");
                        toAdd.foodName = handled.getString("name");
                        toAdd.quantity = Float.parseFloat(handled.getString("quantity"));
                        toAdd.quantityUnit = handled.getString("quantityType");
                        toAdd.expiration = date;
                        vm.insert(toAdd);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
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

        queue.add(foodRequest);
    }

    final Observer<List<Food>> obs = new Observer<List<Food>>() {
        @Override
        public void onChanged(List<Food> foods) {
            adapter.setFood(foods);
        }
    };

    private void alert_deleted_item() {
        Toast t = Toast.makeText(getContext(), "Deleted selected items!", Toast.LENGTH_LONG);
        t.show();
    }

}