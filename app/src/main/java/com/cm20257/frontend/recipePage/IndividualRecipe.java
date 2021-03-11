package com.cm20257.frontend.recipePage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cm20257.frontend.R;
import com.squareup.picasso.Picasso;

public class IndividualRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_recipe);

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

        // Displays image of individual recipe.
        Button button = findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndividualRecipe.this, viewRecipes.class);
                startActivity(intent);
            }
        });

    }
}
