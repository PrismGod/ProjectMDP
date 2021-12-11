package com.example.projectandroid.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectandroid.AdminActivity;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityDetailMovieBinding;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailMovieActivity extends AppCompatActivity {

    ActivityDetailMovieBinding binding;
    String username;
    int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("auth");
        movieID = getIntent().getIntExtra("movieID", 1);
        getMovieDetail(movieID);
        checkMovie(movieID);

        //  udah pake binding jadi kalo mau ambil component tinggal
        //  binding.namaComponent aja
        binding.edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                laporan(binding.edtComment.getText().toString());
                return false;
            }
        });

        binding.btnAddToWatchlist.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.btnAddToWatchlist){
            addOrRemoveWatchlist();
        }
    }

    private void addOrRemoveWatchlist(){
        String function = binding.btnAddToWatchlist.getText().toString().equalsIgnoreCase("Add to Watchlist") ?
                "addWatchlist" : "removeWatchlist";

        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_user),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            laporan(message);

                            if (binding.btnAddToWatchlist.getText().toString().equalsIgnoreCase("Add to Watchlist")){
                                binding.btnAddToWatchlist.setText("Remove from Watchlist");
                            }
                            else{
                                binding.btnAddToWatchlist.setText("Add to Watchlist");
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
                params.put("function", function);
                params.put("username", username);
                params.put("movie_id", movieID+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void checkMovie(int id){
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_user),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean addedToWatchlist = jsonObject.getBoolean("addedToWatchlist");
                            if (addedToWatchlist){
                                binding.btnAddToWatchlist.setText("Remove from Watchlist");
                            }
                            else{
                                binding.btnAddToWatchlist.setText("Add to Watchlist");
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
                params.put("function", "checkMovie");
                params.put("username", username);
                params.put("movie_id", movieID+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMovieDetail(int id){
        String url = getResources().getString(R.string.url_movie_detail) + id
                + "?api_key=" + getResources().getString(R.string.API_KEY)
                + "&language=en-US";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String title = jsonObject.getString("title");
                        binding.tvJudul.setText(title);

                        getSupportActionBar().setTitle(title);

                        String overview = jsonObject.getString("overview");
                        binding.tvDesc.setText(overview);

                        String release_date = "Release Date : " + jsonObject.getString("release_date");
                        binding.tvReleaseDate.setText(release_date);

                        JSONArray jsonArray = jsonObject.getJSONArray("genres");
                        StringBuilder genre = new StringBuilder("Genre : ");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            genre.append(itemObject.getString("name"));
                            if (i < jsonArray.length()-1){
                                genre.append(", ");
                            }
                        }
                        binding.tvGenre.setText(genre);

                        String url_poster = getResources().getString(R.string.url_poster);
                        String poster_path = jsonObject.getString("poster_path");
                        Glide.with(this)
                                .load(url_poster + poster_path)
                                .into(binding.ivMovie);
                        String backdrop_path = jsonObject.getString("backdrop_path");
                        Glide.with(this)
                                .load(url_poster + backdrop_path)
                                .into(binding.ivBackdrop);

                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                },
                error -> {

                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    void laporan(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }

    public void add_fav(View view) {

    }
}