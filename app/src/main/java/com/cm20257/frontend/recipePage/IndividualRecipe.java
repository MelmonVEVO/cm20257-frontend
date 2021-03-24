package com.cm20257.frontend.recipePage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;
import com.cm20257.frontend.foodList.MainFoodActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndividualRecipe extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("recipe");

        // Displays image of individual recipe.
        TextView title = findViewById(R.id.recipeTitle);
        title.setText(recipe.getTitle());

        // Displays image of individual recipe.
        TextView time = findViewById(R.id.time);
        time.setText(recipe.getTime());

        // Displays image of individual recipe.
        TextView ingredients = findViewById(R.id.ingredients);
        ingredients.setText(recipe.getIngredients());

        // Displays image of individual recipe using Picasso class.
        // must have "multiDexEnabled true" under "defaultConfig" in build.gradle
        // must have "implementation 'com.squareup.picasso:picasso:2.71828'" under "dependecies" in build.gradle
        ImageView image = findViewById(R.id.imageURL);
        Picasso.get().load(recipe.getImgURL()).into(image);

        // Sets a listener to go to the URL of the recipe on BBC Goodfood.
        Button urlButton = findViewById(R.id.buttonURL);
        urlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(recipe.getUrl());
                Intent urlClick = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(urlClick);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_and_add_favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_recipes) {
            finish();
        }

        if (id == R.id.action_add_favourites) {
            if (recipe.getFavouriteID() == -1) {
                add_favourite();
            } else {
                deleteFavourite();
                for (int i = 0; i < viewRecipes.recipeList.size(); i++) {
                    if (recipe.getId() == viewRecipes.recipeList.get(i).getId()) {
                        recipe.setFavouriteID(-1);
                        viewRecipes.recipeList.get(i).setFavouriteID(-1);
                        Toast.makeText(getApplicationContext(), recipe.getTitle() + " has been removed from favourites", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            invalidateOptionsMenu();
        }

        if (id == R.id.action_food_items) {
            Intent intent = new Intent(this, MainFoodActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, viewRecipes.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void add_favourite() {
        String URL = "http://10.0.2.2:8080/account/add-favourite";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, Integer> item_to_add = new HashMap<>();
        item_to_add.put("recipe", recipe.getId());

        JsonObjectRequest addRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(item_to_add), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                for (int i = 0; i < viewRecipes.recipeList.size(); i++) {
                    if (recipe.getId() == viewRecipes.recipeList.get(i).getId()) {
                        try {
                            recipe.setFavouriteID(response.getInt("favourite"));
                            viewRecipes.recipeList.get(i).setFavouriteID(response.getInt("favourite"));

                            Toast.makeText(getApplicationContext(), recipe.getTitle() + " has been added to favourites", Toast.LENGTH_LONG).show();
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        queue.add(addRequest);
    }

    public void deleteFavourite() {

        String URL = "http://10.0.2.2:8080/account/delete-favourite";
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, Integer> item_to_add = new HashMap<>();
        item_to_add.put("favourite", recipe.getFavouriteID());

        JsonObjectRequest addRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(item_to_add), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

        queue.add(addRequest);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add_favourites);

        if (recipe.getFavouriteID() != -1) {
            item.setTitle("Remove from Favourites");
        } else {
            item.setTitle("Add to Favourites");
        }

        return super.onPrepareOptionsMenu(menu);
    }

}
