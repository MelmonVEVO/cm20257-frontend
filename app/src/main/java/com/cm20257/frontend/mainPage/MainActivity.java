package com.cm20257.frontend.mainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;
import com.cm20257.frontend.foodList.AddFood;
import com.cm20257.frontend.foodList.MainFoodActivity;

public class MainActivity extends AppCompatActivity {
    TextView nameShow;
    TextView tokenShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nameShow = findViewById(R.id.userText);
        tokenShow = findViewById(R.id.tokenText);
        nameShow.setText("Name: " + UserHandler.getUsername());
        tokenShow.setText("Token: " + UserHandler.getToken());
    }

    public void goToFoodScreen(View v) {
        Intent intent = new Intent(this, MainFoodActivity.class);
        startActivity(intent);
    }
}