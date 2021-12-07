package com.example.projectandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.RvItemMovieBinding;
import com.example.projectandroid.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {

    private ArrayList<Movie> movies;
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter(ArrayList<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemMovieBinding binding = RvItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Movie movie = movies.get(position);

        holder.binding.tvTitle.setText(movie.getTitle());
        String url_poster = context.getResources().getString(R.string.url_poster);
        Glide.with(context)
                .load(url_poster+movie.getPoster_path())
                .into(holder.binding.ivPosterMovie);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final RvItemMovieBinding binding;
        public Holder(@NonNull RvItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Movie movie);
    }
}
