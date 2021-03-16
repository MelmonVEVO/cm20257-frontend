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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cm20257.frontend.R;
import com.cm20257.frontend.foodList.MainFoodActivity;
import com.squareup.picasso.Picasso;

public class IndividualRecipe extends AppCompatActivity {

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
        Recipe recipe = intent.getParcelableExtra("recipe");

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

//        // Displays image of individual recipe.
//        Button button = findViewById(R.id.backButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(IndividualRecipe.this, viewRecipes.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.action_food_items) {
            Intent intent = new Intent(this, MainFoodActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
