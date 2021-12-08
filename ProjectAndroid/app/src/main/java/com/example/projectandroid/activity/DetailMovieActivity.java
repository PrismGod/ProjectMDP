package com.example.projectandroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityDetailMovieBinding;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



//                        JSONArray jsonArrayGenre = jsonObject.getJSONArray("genres");


                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
//                    binding.progressBar.setVisibility(View.GONE);
                },
                error -> {

                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}