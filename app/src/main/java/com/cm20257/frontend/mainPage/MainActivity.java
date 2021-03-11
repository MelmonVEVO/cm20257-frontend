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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView welcomeText = findViewById(R.id.textWelcome);
        welcomeText.setText(String.format(getString(R.string.welcome), UserHandler.getUsername()));

        // Debug
        TextView tokenText = findViewById(R.id.textToken);
        tokenText.setText("Token: " + UserHandler.getToken());
    }

    public void goToFoodScreen(View v) {
        Intent intent = new Intent(this, MainFoodActivity.class);
        startActivity(intent);
    }
}