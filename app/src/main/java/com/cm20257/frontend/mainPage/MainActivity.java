package com.cm20257.frontend.mainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cm20257.frontend.R;
import com.cm20257.frontend.foodList.AddFood;
import com.cm20257.frontend.foodList.MainFoodActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void goToFoodScreen(View v) {
        Intent intent = new Intent(this, MainFoodActivity.class);
        startActivity(intent);
    }
}