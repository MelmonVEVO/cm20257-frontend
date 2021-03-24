package com.cm20257.frontend.recipePage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;
import com.cm20257.frontend.foodList.MainFoodActivity;
import com.cm20257.frontend.mainPage.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyHolder> {

    LayoutInflater layoutInflater;
    List<Recipe> recipeList;
    OnClickListener onClickListener;

    public RecipeAdapter(Context context, List<Recipe> recipeList, OnClickListener onClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.recipeList = recipeList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preview_recipe, parent, false);
        return new MyHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(MyHolder myHolder, int position) {
        myHolder.title.setText(recipeList.get(position).getTitle());

        if (recipeList.get(position).getFavouriteID() == -1) {
            myHolder.favourite.setText("");
        } else {
            myHolder.favourite.setText("Favourite");
        }

        String time = "Time: " + recipeList.get(position).getTime();
        myHolder.time.setText(time);
    }

    @Override
    public int getItemCount() { return recipeList.size(); }

    public static class MyHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time, title, favourite;
        OnClickListener onClickListener;

        public MyHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            this.time = itemView.findViewById(R.id.preview_time);
            this.title = itemView.findViewById(R.id.preview_title);
            this.onClickListener = onClickListener;
            this.favourite = itemView.findViewById(R.id.favouriteTag);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(getAdapterPosition());
        }
    }

    public void updateList(List<Recipe> recipeList) {
        this.recipeList.clear();
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}

public class viewRecipes extends AppCompatActivity implements RecipeAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    public List<Recipe> adapterList;
    public static List<Recipe> recipeList;
    public static List<Recipe> favouritesList;
    private List<String> foodList;

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, IndividualRecipe.class);
        intent.putExtra("recipe", adapterList.get(position));
        startActivity(intent);
    }

    public interface VolleyCallbackRecipe {
        void onSuccessResponse(List<Recipe> result);
    }

    public interface VolleyCallbackFood {
        void onSuccessResponse(List<String> result);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipes);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipes");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        get_users_food(UserHandler.getToken(), new VolleyCallbackFood() {
            @Override
            public void onSuccessResponse(List<String> result) {

                get_all_recipes(UserHandler.getToken(), result, new VolleyCallbackRecipe() {
                    @Override
                    public void onSuccessResponse(List<Recipe> result) {
                        adapterList = new ArrayList<>(result);
                        get_favourite_recipes(UserHandler.getToken());
                        adapter = new RecipeAdapter(getApplicationContext(), adapterList, viewRecipes.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_and_favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_food_items) {
            Intent intent = new Intent(this, MainFoodActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_get_favourites) {
            if (item.getTitle().equals("Show Favourite Recipes")) {
                if (favouritesList.size() == 0) {
                    Toast.makeText(this, "You haven't set any recipes as favourite", Toast.LENGTH_LONG).show();
                    return super.onOptionsItemSelected(item);
                }
                adapterList = new ArrayList<>(favouritesList);
                adapter.updateList(adapterList);
                recyclerView.setAdapter(adapter);
                item.setTitle("Show All Recipes");
            } else {
                adapterList = new ArrayList<>(recipeList);
                adapter.updateList(adapterList);
                recyclerView.setAdapter(adapter);
                item.setTitle("Show Favourite Recipes");
            }
        }

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // close this activity and return to previous activity
        }

        return super.onOptionsItemSelected(item);
    }

    public void get_favourite_recipes(String authentication) {

        RequestQueue queue = Volley.newRequestQueue(viewRecipes.this);
        String url = "http://10.0.2.2:8080/account/favourites";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Recipe> food_favourites = new ArrayList<>();
                favouritesList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject object = response.getJSONObject(i);
                        Recipe newRecipe = new Recipe(object.getInt("id"), -1, object.getString("title"),
                                object.getString("time"), "",
                                object.getString("imageUrl"), object.getString("url"));

                        food_favourites.add(newRecipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < recipeList.size(); i++) {
                    for (int j = 0; j < food_favourites.size(); j++) {
                        if (recipeList.get(i).getTitle().equals(food_favourites.get(j).getTitle())) {
                            recipeList.get(i).setFavouriteID(food_favourites.get(j).getId());
                            favouritesList.add(recipeList.get(i));
                            break;
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authentication);
                return params;
            }
        };

        queue.add(jsonArrayRequest);
    }

    public void get_all_recipes(String authentication, List<String> food_names, VolleyCallbackRecipe callback) {

        RequestQueue queue = Volley.newRequestQueue(viewRecipes.this);
        String url = "http://10.0.2.2:8080/recipes";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                recipeList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        String listOfIngredients = (String) object.get("ingredients");
                        String newLines = listOfIngredients.replaceAll("\", ", "\n");
                        listOfIngredients = newLines.replaceAll("(\")|([\\[\\]])", "");

                        for (String name : food_names) {
                            if (listOfIngredients.contains(name) || listOfIngredients.toUpperCase().contains(name.toUpperCase())) {
                                Recipe newRecipe = new Recipe(object.getInt("id"), -1, object.getString("title"),
                                        object.getString("time"), listOfIngredients,
                                        object.getString("imageUrl"), object.getString("url"));

                                recipeList.add(newRecipe);
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callback.onSuccessResponse(recipeList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authentication);
                return params;
            }
        };

        queue.add(jsonArrayRequest);
    }

    public void get_users_food(String authentication, VolleyCallbackFood callback) {

        RequestQueue queue = Volley.newRequestQueue(viewRecipes.this);
        String url = "http://10.0.2.2:8080/account/food";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                foodList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        foodList.add(object.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                callback.onSuccessResponse(foodList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authentication);
                return params;
            }
        };

        queue.add(jsonArrayRequest);
    }
}