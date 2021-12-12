package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private ArrayList<Mahasiswa> arrMhs = new ArrayList<>();
    RecyclerView rv;
    MahasiswaAdapter _MahasiswaAdapter;
    Button blogout,bban,bunban;
    TextView tv;
    public static String urldatabase;

    static {
        urldatabase = getResources().getString(R.string.url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rv = findViewById(R.id.rvMhs);

        rv.setLayoutManager(new LinearLayoutManager(this));
        _MahasiswaAdapter= new MahasiswaAdapter(arrMhs);
        rv.setAdapter(_MahasiswaAdapter);

        getALlMhs();

        _MahasiswaAdapter.notifyDataSetChanged();

        tv = findViewById(R.id.admin_judge);

        bban = findViewById(R.id.admin_ban);
        bunban = findViewById(R.id.admin_unban);
        blogout = findViewById(R.id.admin_logout);

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banMhs();

                Intent i=new Intent(AdminActivity.this,AdminActivity.class);
                startActivity(i);

                finish();
            }
        });

        bunban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbanMhs();

                Intent i=new Intent(AdminActivity.this,AdminActivity.class);
                startActivity(i);

                finish();
            }
        });

    }

    private void getALlMhs(){
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

                                JSONArray jsonArray= jsonObject.getJSONArray("datamhs");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject mhsObj = jsonArray.getJSONObject(i);
                                    Mahasiswa mhs = new Mahasiswa(
                                            mhsObj.getString("username"),
                                            mhsObj.getString("password"),
                                            mhsObj.getString("banned")
                                    );
//                                    arrMhs.add(mhs);
                                    String a=mhsObj.getString("username");
                                    String b=mhsObj.getString("password");
                                    String c=mhsObj.getString("banned");

                                    addTOArray(a,b,c);
                                    _MahasiswaAdapter.notifyDataSetChanged();
                                }


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
                params.put("function","getallmhs");
                return params;
            }
        };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void banMhs(){
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
                            toaster(message);
                            System.out.println(message);
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
                params.put("function","banmhs");
                params.put("username",tv.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void unbanMhs(){
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
                            toaster(message);
                            System.out.println(message);
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
                params.put("function","unbanmhs");
                params.put("username",tv.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void addTOArray(String a,String b,String c)
    {
        Mahasiswa mhs=new Mahasiswa(a,b,c);
        arrMhs.add(mhs);
    }

    public void toaster(String a)
    {
        Context context = getApplicationContext();
        CharSequence text = a;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}



