package com.example.projectandroid.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.projectandroid.databinding.FragmentUserFavoriteBinding;
import com.example.projectandroid.databinding.FragmentUserHomeBinding;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavoriteFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    FragmentUserFavoriteBinding binding;
    ArrayList<Movie> movies = new ArrayList<>();
    MovieAdapter adapter;

    public UserFavoriteFragment() {
        // Required empty public constructor
    }

    public static UserFavoriteFragment newInstance(String username) {
        UserFavoriteFragment fragment = new UserFavoriteFragment();
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
        binding = FragmentUserFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MovieAdapter(movies, getContext());
        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra("auth", username);
                intent.putExtra("movieID", movie.getId());
                startActivity(intent);
            }
        });
        binding.rvFavorite.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        movies.clear();
        loadFavorite();
    }

    private void loadFavorite(){
        binding.progressBar3.setVisibility(View.VISIBLE);
        StringRequest _StringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url_favorite),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200){
                                JSONArray movies_id = jsonObject.getJSONArray("result");
                                for (int i=0; i<movies_id.length(); i++){
                                    getMovieFromAPI(movies_id.getInt(i));
                                }
                            }
                            binding.progressBar3.setVisibility(View.GONE);
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
                params.put("function", "getFavorite");
                params.put("username", username);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(_StringRequest);
    }

    private void getMovieFromAPI(int id){
//        System.out.println("MASOK "+id);
        String url = getResources().getString(R.string.url_movie_detail) + id
                + "?api_key=" + getResources().getString(R.string.API_KEY)
                + "&language=en-US";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int movie_id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        String poster_path = jsonObject.getString("poster_path");

                        Movie movie = new Movie(movie_id, title, poster_path);
                        movies.add(movie);

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