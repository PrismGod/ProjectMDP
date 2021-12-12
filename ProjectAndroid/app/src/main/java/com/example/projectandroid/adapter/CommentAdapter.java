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
import com.example.projectandroid.databinding.RvItemCommentBinding;
import com.example.projectandroid.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments){
        this.comments = comments;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        RvItemCommentBinding binding = RvItemCommentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new Holder(binding);
    }

    @Override
    public int getItemCount(){return comments.size();}

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position){
        Comment comment = comments.get(position);

        holder.binding.tvUsername.setText(comment.getUsername());
        holder.binding.tvComment.setText(comment.getComment());
    }

    public class Holder extends RecyclerView.ViewHolder{
        private final RvItemCommentBinding binding;
        public Holder(@NonNull RvItemCommentBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
