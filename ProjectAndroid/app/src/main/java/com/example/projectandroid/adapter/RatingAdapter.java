package com.example.projectandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.RvItemRatingBinding;
import com.example.projectandroid.model.Movie;
import com.example.projectandroid.model.Rating;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.Holder> {

    private ArrayList<Rating> ratings;
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public RatingAdapter(ArrayList<Rating> ratings, Context context){
        this.ratings = ratings;
        this.context = context;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemRatingBinding binding = RvItemRatingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RatingAdapter.Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Rating rating = ratings.get(position);

        holder.binding.tvTitle.setText(rating.getTitle());
        holder.binding.ratingBar.setRating(rating.getRating());

        String url_poster = context.getResources().getString(R.string.url_poster);
        Glide.with(context)
                .load(url_poster+rating.getPoster_path())
                .into(holder.binding.ivPosterMovie);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(rating);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private final RvItemRatingBinding binding;
        public Holder(@NonNull RvItemRatingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Rating rating);
    }
}
