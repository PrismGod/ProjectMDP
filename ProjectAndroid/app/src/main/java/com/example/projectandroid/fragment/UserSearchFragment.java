package com.example.projectandroid.fragment;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectandroid.R;
import com.example.projectandroid.activity.DetailMovieActivity;
import com.example.projectandroid.adapter.MovieAdapter;
import com.example.projectandroid.databinding.FragmentSearchBinding;
import com.example.projectandroid.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSearchFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    FragmentSearchBinding binding;

    ArrayList<Movie> movies = new ArrayList<>();
    MovieAdapter adapter;

    public UserSearchFragment() {
        // Required empty public constructor
    }


    public static UserSearchFragment newInstance(String username) {
        UserSearchFragment fragment = new UserSearchFragment();
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
        binding =FragmentSearchBinding.inflate(inflater,container,false);
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
        binding.edtSearchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND){
                    movies.clear();
                    doSearch();
                    return true;
                }
                return false;
            }
        });
    }

    public void doSearch (){
        String url = getResources().getString(R.string.url_search_movie)
                + getResources().getString(R.string.API_KEY)
                + "&language=en-US&query="+ binding.edtSearchbar.getText().toString() + "&page=1&include_adult=true";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        int size = jsonArray.length();
                        if (size == 0){
                            binding.tvResult.setText("There are no movies that matched your query.");
                            binding.tvResult.setVisibility(View.VISIBLE);
                            return;
                        }

                        binding.tvResult.setText("Showing results for \""+binding.edtSearchbar.getText().toString()+"\"");
                        binding.tvResult.setVisibility(View.VISIBLE);
                        for (int i=0; i<size; i++){
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
                },
                error -> {

                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}