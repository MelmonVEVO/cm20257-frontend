package com.cm20257.frontend.foodList;

import android.content.Intent;
import android.os.Bundle;

import com.cm20257.frontend.R;
import com.cm20257.frontend.mainPage.MainActivity;
import com.cm20257.frontend.recipePage.viewRecipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public class MainFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfoodmain);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
            Intent intent = new Intent(this, viewRecipes.class);
            startActivity(intent);
        }

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    // I don't know why this is here but I'm keeping it just in case it's useful later
//    @Override
//    public void sendData(String[] a) {
//        viewFood viewfood = new viewFood();
//        Bundle bundle = new Bundle();
//        bundle.putStringArray("key", a);
//        viewfood.setArguments(bundle);
//
//        fragmentManager.beginTransaction().replace(R.id.container, viewfood, null).commit();
//    }
}