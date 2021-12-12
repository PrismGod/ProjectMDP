package com.example.projectandroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.activity.DetailMovieActivity;
import com.example.projectandroid.adapter.MovieAdapter;
import com.example.projectandroid.adapter.RatingAdapter;
import com.example.projectandroid.databinding.FragmentUserRatingBinding;
import com.example.projectandroid.databinding.FragmentUserWatchListBinding;
import com.example.projectandroid.model.Movie;
import com.example.projectandroid.model.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRatingFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    FragmentUserRatingBinding binding;

    ArrayList<Rating> ratings = new ArrayList<>();
    RatingAdapter adapter;

    public UserRatingFragment() {
        // Required empty public constructor
    }

    public static UserRatingFragment newInstance(String username) {
        UserRatingFragment fragment = new UserRatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadRating();

        binding.rvRating.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RatingAdapter(ratings, getContext());
        adapter.setOnItemClickCallback(new RatingAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Rating rating) {
                Dialog ratingDialog = new Dialog(getContext());
                ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.show();

                RatingBar ratingBar = ratingDialog.findViewById(R.id.ratingBar);
                ratingBar.setRating(rating.getRating());
                Button btnSubmit = ratingDialog.findViewById(R.id.btnSubmit);
                btnSubmit.setText("Update");

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateRating(rating.getMovie_id(), ratingBar.getRating());
                        ratingDialog.hide();
                    }
                });
            }
        });
        binding.rvRating.setAdapter(adapter);
    }

    private void updateRating(int id, float value){
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

                            loadRating();
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
                params.put("function", "updateRating");
                params.put("username", username);
                params.put("movie_id", id+"");
                params.put("rating", value+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(_StringRequest);
    }

    private void loadRating(){
        ratings.clear();
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_user),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200){ // success code
                                JSONArray arrMovieId = jsonObject.getJSONArray("movie_id");
                                JSONArray arrRating = jsonObject.getJSONArray("rating");

                                for (int i=0; i<arrMovieId.length(); i++){
                                    float rating = Float.parseFloat(arrRating.getString(i));
                                    getMovieDetail(arrMovieId.getInt(i), rating);
                                }
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
                params.put("function", "getMyRating");
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(_StringRequest);
    }

    private void getMovieDetail(int id, float rating){
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
                        String poster_path = jsonObject.getString("poster_path");

                        Rating ratingObject = new Rating(id, title, poster_path, rating);
                        ratings.add(ratingObject);

                        adapter.notifyDataSetChanged();
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                },
                error -> {

                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}