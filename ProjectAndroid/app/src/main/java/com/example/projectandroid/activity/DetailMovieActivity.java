package com.example.projectandroid.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.example.projectandroid.adapter.CommentAdapter;
import com.example.projectandroid.databinding.ActivityDetailMovieBinding;
import com.example.projectandroid.model.Comment;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailMovieActivity extends AppCompatActivity {

    ActivityDetailMovieBinding binding;
    String username;
    int movieID;

    ArrayList<Comment> comments = new ArrayList<>();
    CommentAdapter adapter;
    Button btnSubmit;
    Dialog ratingDialog;
    RatingBar ratingBar;

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
        checkFavorite(movieID);

        loadComment();
        loadRating();

        binding.rvComment.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(comments);
        binding.rvComment.setAdapter(adapter);

        binding.edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND){
                    String comment = binding.edtComment.getText().toString();
                    if (comment.isEmpty()){
                        laporan("Your comment is empty");
                        return false;
                    }

                    submitComment(comment);
                    return true;
                }
                return false;
            }
        });

        binding.btnAddToWatchlist.setOnClickListener(this::onClick);
        binding.btnFavorite.setOnClickListener(this::onClick);
        binding.btnRate.setOnClickListener(this::onClick);

        ratingDialog = new Dialog(this);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingDialog.setContentView(R.layout.rating_dialog);

        ratingBar = ratingDialog.findViewById(R.id.ratingBar);
        btnSubmit = ratingDialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdateRating(ratingBar.getRating());
                ratingDialog.hide();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ratingDialog != null) {
            ratingDialog.dismiss();
            ratingDialog = null;
        }
    }

    private void submitComment(String comment){
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_comment),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            System.out.println(message);

                            loadComment();
                            binding.edtComment.setText("");
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
                params.put("function", "addComment");
                params.put("username", username);
                params.put("movie_id", movieID+"");
                params.put("comment", comment);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void loadComment(){
        comments.clear();
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_comment),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                JSONArray arrUser = jsonObject.getJSONArray("username");
                                JSONArray arrComm = jsonObject.getJSONArray("comment");
                                for (int i=arrUser.length()-1; i>=0; i--){
                                    Comment comment = new Comment(arrUser.get(i) + "", arrComm.get(i) + "");
                                    comments.add(comment);
                                }
                                adapter.notifyDataSetChanged();
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
                Map<String, String> params = new HashMap<>();
                params.put("function", "getComment");
                params.put("movie_id", movieID + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void onClick(View view){
        int viewID = view.getId();
        if (viewID == R.id.btnAddToWatchlist){
            addOrRemoveWatchlist();
        }
        else if(viewID == R.id.btnFavorite){
            addOrRemoveFavorite();
        }
        else if (viewID == R.id.btnRate){
            ratingDialog.show();
        }
    }

    private void loadRating(){
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_user),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String rating = jsonObject.getString("rating");
                            if (rating.isEmpty()){
                                binding.tvRating.setText("Rating : -");
                            }
                            else{
                                binding.tvRating.setText("Rating : "+rating);
                            }
                            boolean ratedByUser = jsonObject.getBoolean("ratedByUser");
                            if (ratedByUser){
                                btnSubmit.setText("Update");
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
                Map<String, String> params = new HashMap<>();
                params.put("function", "getRating");
                params.put("username", username);
                params.put("movie_id", movieID + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
    }

    private void addOrUpdateRating(float value){
        String function = btnSubmit.getText().toString().equalsIgnoreCase("Submit") ?
                "addRating" : "updateRating";

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
                            System.out.println(message);

                            if (btnSubmit.getText().toString().equalsIgnoreCase("Submit")){
                                btnSubmit.setText("Update");
                            }
                            else{
                                btnSubmit.setText("Submit");
                            }

                            loadRating();
                            ratingBar.setRating(0);
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
                params.put("rating", value+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(_StringRequest);
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

    private void addOrRemoveFavorite(){
        String function = binding.btnFavorite.getText().toString().equalsIgnoreCase("Favorite") ?
                "addFavorite" : "removeFavorite";

        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_favorite),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            laporan(message);

                            if (binding.btnFavorite.getText().toString().equalsIgnoreCase("Favorite")){
                                binding.btnFavorite.setText("Unfavorite");
                            }
                            else{
                                binding.btnFavorite.setText("Favorite");
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

    private void checkFavorite(int id){
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_favorite),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean addedToFavorite = jsonObject.getBoolean("addedToFavorite");
                            if (addedToFavorite){
                                binding.btnFavorite.setText("Unfavorite");
//                                binding.btnFavorite.setIcon(R.drawable.ic_favorited);
                            }
                            else{
                                binding.btnFavorite.setText("Favorite");
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
                params.put("function", "checkFavorite");
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
}