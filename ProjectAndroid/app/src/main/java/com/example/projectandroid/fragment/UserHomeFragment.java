package com.example.projectandroid.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectandroid.R;
import com.example.projectandroid.activity.DetailMovieActivity;
import com.example.projectandroid.activity.LoginActivity;
import com.example.projectandroid.activity.RegisterActivity;
import com.example.projectandroid.adapter.MovieAdapter;
import com.example.projectandroid.databinding.FragmentUserHomeBinding;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    int page = 1;

    FragmentUserHomeBinding binding;
    ArrayList<Movie> movies = new ArrayList<>();
    MovieAdapter adapter;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    public static UserHomeFragment newInstance(String username) {
        UserHomeFragment fragment = new UserHomeFragment();
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
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvMovie.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
        binding.rvMovie.setAdapter(adapter);

        loadPopularMovies(page);

        binding.btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                loadPopularMovies(page);
            }
        });
    }

    private void loadPopularMovies(int page){
        binding.progressBar.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.url_popular_now)
                + getResources().getString(R.string.API_KEY)
                + "&language=en-US&page=" + page;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            int id = itemObject.getInt("id");
                            String title = itemObject.getString("title");
                            String poster_path = itemObject.getString("poster_path");

                            Movie movie = new Movie(id, title, poster_path);
                            movies.add(movie);
                        }

                        adapter.notifyDataSetChanged();
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                },
                error -> {

                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}