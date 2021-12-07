package com.example.projectandroid.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityLoginBinding;
import com.example.projectandroid.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(this::onClick);
        binding.tvToLogin.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        int viewId = view.getId();
        if (viewId == R.id.btnRegister){
            String user = binding.edtUsername.getText().toString();
            String pass = binding.edtPassword.getText().toString();
            String conPass = binding.edtConfirmPassword.getText().toString();
            if (user.isEmpty() || pass.isEmpty() || conPass.isEmpty()){
                makeToast("Please fill all fields");
                return;
            }
            if (!pass.equals(conPass)){
                makeToast("Invalid Confirmation Password");
                return;
            }
            if (user.equalsIgnoreCase("admin")){
                makeToast("Invalid Username");
                return;
            }

            attemptRegister();
        }
        else if (viewId == R.id.tvToLogin){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void attemptRegister(){
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
                            String message = jsonObject.getString("message");
                            System.out.println(message);
                            makeToast(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                //handle error
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
                params.put("function","addmhs");
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