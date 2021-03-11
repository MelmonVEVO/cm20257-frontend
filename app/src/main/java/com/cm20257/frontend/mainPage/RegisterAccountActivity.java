package com.cm20257.frontend.mainPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cm20257.frontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterAccountActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        passwordText = findViewById(R.id.passwordInput);
        usernameText = findViewById(R.id.emailInput);
        registerButton = findViewById(R.id.registerBtn);
    }

    private boolean checkData() {
        // ping with inputted text and password
        if (checkIfEmpty(passwordText)) {
            Toast t = Toast.makeText(this, "Please enter your password.", Toast.LENGTH_LONG);
            t.show();
            return false;
        }
        if (checkIfEmpty(usernameText)) {
            Toast t = Toast.makeText(this, "Please enter your username.", Toast.LENGTH_LONG);
            t.show();
            return false;
        }
        return true;
    }

    private boolean checkIfEmpty(EditText text) {
        String s = text.getText().toString();
        return s.equals("");
    }

    public void register(View v) throws JSONException {
        if (checkData()) {
            registerButton.setEnabled(false);
            RequestQueue queue = Volley.newRequestQueue(this);
            String loginServerUrl = "http://192.168.1.16:8080/account/create";

            StringRequest registrationRequest = new StringRequest(Request.Method.POST, loginServerUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    registerButton.setEnabled(true);
                    promptSuccess();
                    goToLoginScreen();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    registerButton.setEnabled(true);
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

                @Override
                public byte[] getBody() {
                    JSONObject registerParams = new JSONObject();
                    try {
                        registerParams.put("username", usernameText.getText().toString());
                        registerParams.put("password", passwordText.getText().toString());
                        return registerParams.toString().getBytes();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return new JSONObject().toString().getBytes(); // if an error happened here, a bad request will be made
                    }
                }
            };
            queue.add(registrationRequest);
        }
    }

    private void goToLoginScreen() {
        finish();
    }

    private void promptError() {
        Toast t = Toast.makeText(this, "An error has occurred. Perhaps a user with the same name already exists?", Toast.LENGTH_SHORT);
        t.show();
    }

    private void promptSuccess() {
        Toast t = Toast.makeText(this, "Account " + usernameText.getText().toString() + " successfully created!", Toast.LENGTH_SHORT);
        t.show();
    }
}