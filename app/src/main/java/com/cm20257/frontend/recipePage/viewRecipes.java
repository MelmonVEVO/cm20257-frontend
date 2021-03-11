package com.cm20257.frontend.recipePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

        String time = "Time: " + recipeList.get(position).getTime();
        myHolder.time.setText(time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class MyHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time, title;
        OnClickListener onClickListener;

        public MyHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            this.time = itemView.findViewById(R.id.preview_time);
            this.title = itemView.findViewById(R.id.preview_title);
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}

public class viewRecipes extends AppCompatActivity implements RecipeAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList;
    private List<String> foodList;

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, IndividualRecipe.class);
        intent.putExtra("recipe", recipeList.get(position));
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        get_users_food(UserHandler.getToken(), new VolleyCallbackFood() {
            @Override
            public void onSuccessResponse(List<String> result) {

                get_all_recipes(UserHandler.getToken(), result, new VolleyCallbackRecipe() {
                    @Override
                    public void onSuccessResponse(List<Recipe> result) {
                        adapter = new RecipeAdapter(getApplicationContext(), result, viewRecipes.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
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
                                Recipe newRecipe = new Recipe(object.getInt("id"), object.getString("title"),
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

    public void goToMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}