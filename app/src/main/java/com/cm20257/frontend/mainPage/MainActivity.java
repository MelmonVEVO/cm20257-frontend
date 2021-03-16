package com.cm20257.frontend.mainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;
import com.cm20257.frontend.foodList.MainFoodActivity;
import com.cm20257.frontend.recipePage.viewRecipes;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView welcomeText = findViewById(R.id.textWelcome);
        welcomeText.setText(String.format(getString(R.string.welcome), UserHandler.getUsername()));

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_and_logout, menu);
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
            Intent intent = new Intent(this, viewRecipes.class);
            startActivity(intent);
        }

        if (id == R.id.action_food_items) {
            Intent intent = new Intent(this, MainFoodActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToFoodScreen(View v) {
        Intent intent = new Intent(this, MainFoodActivity.class);
        startActivity(intent);
    }

    public void goToRecipes(View v) {
        Intent intent = new Intent(this, viewRecipes.class);
        startActivity(intent);
    }

}