package com.example.projectandroid.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectandroid.AdminActivity;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(this::onClick);
        binding.tvToRegister.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.btnLogin){
            String user = binding.edtUsername.getText().toString();
            String pass = binding.edtPassword.getText().toString();
            if (user.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            attemptLogin();
        }
        else if (viewId == R.id.tvToRegister){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void attemptLogin(){
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code != 1){
                                String message = jsonObject.getString("message");
                                makeToast(message);
                            }

                            if(code==13) {
                                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(i);
                            }
                            else if(code==1) {
                                String username = jsonObject.getString("message");

                                // pake sharedpreference buat auto login
                                SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.apply();

                                Intent i = new Intent(LoginActivity.this, UserHomeActivity.class);
                                i.putExtra("auth", username);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("function","logins");
                params.put("username", binding.edtUsername.getText().toString());
                params.put("password", binding.edtPassword.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    public void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}