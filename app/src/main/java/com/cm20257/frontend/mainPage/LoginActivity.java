package com.cm20257.frontend.mainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;
import com.cm20257.frontend.UserHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText passwordText;
    EditText emailText;
    Button loginBtn;
    Button registerNewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordText = findViewById(R.id.passwordInput);
        emailText = findViewById(R.id.emailInput);
        loginBtn = findViewById(R.id.registerBtn);
        registerNewBtn = findViewById(R.id.registerNewBtn);
    }

    private boolean checkData() {
        // ping with inputted text and password
        if (checkIfEmpty(passwordText)) {
            Toast t = Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        if (checkIfEmpty(emailText)) {
            Toast t = Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        return true;
    }

    private boolean checkIfEmpty(EditText text) {
        String s = text.getText().toString();
        return s.equals("");
    }

    public void login(View v) throws JSONException {
        if (checkData()) {
            loginBtn.setEnabled(false);
            registerNewBtn.setEnabled(false);
            RequestQueue queue = Volley.newRequestQueue(this);
            String loginServerUrl = "http://10.0.2.2:8080/account/login";

            JSONObject loginDetails = new JSONObject();
            loginDetails.put("username", emailText.getText().toString());
            loginDetails.put("password", passwordText.getText().toString());
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginServerUrl, loginDetails, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loginBtn.setEnabled(true);
                    registerNewBtn.setEnabled(true);
                    try {
                        String token = response.getString("token");
                        UserHandler.setToken(token);
                        UserHandler.setUsername(emailText.getText().toString());
                        goToMainScreen();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loginBtn.setEnabled(true);
                    registerNewBtn.setEnabled(true);
                    error.printStackTrace();
                    promptError();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() { // this is for putting headers in the request
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            queue.add(loginRequest);
        }
    }

    private void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToRegisterScreen(View v) {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    private void promptError() {
        Toast t = Toast.makeText(this, "An error has occurred. Perhaps the account does not exist?", Toast.LENGTH_SHORT);
        t.show();
    }

}