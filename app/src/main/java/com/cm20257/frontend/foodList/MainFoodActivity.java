package com.cm20257.frontend.foodList;

import android.os.Bundle;

import com.cm20257.frontend.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public class MainFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfoodmain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        if (id == R.id.action_settings) {
            return true;
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