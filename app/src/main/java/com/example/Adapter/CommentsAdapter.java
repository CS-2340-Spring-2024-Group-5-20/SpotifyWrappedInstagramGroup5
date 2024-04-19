package com.example.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.CommentPage;
import com.example.spotifywrappedinstagramgroup5.ProfilePageSearchPopOut;
import com.example.spotifywrappedinstagramgroup5.R;
import com.example.spotifywrappedinstagramgroup5.SpotifyWrappedPopUp;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<String> commentsList;

    public CommentsAdapter(List<String> commentsList) {
        this.commentsList = commentsList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        String comment = commentsList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_item_comment);
        }

        public void bind(String comment) {
            commentTextView.setText(comment);
        }
    }
}

