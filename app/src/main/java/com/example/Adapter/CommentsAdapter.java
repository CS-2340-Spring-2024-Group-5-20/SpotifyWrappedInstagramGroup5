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

import com.example.Models.CommentModel;
import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.CommentPage;
import com.example.spotifywrappedinstagramgroup5.ProfilePageSearchPopOut;
import com.example.spotifywrappedinstagramgroup5.R;
import com.example.spotifywrappedinstagramgroup5.SpotifyWrappedPopUp;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private Context context;
    private ArrayList<CommentModel> list;


    public CommentsAdapter(Context context, ArrayList<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentModel commentModel = list.get(position);
        holder.username.setText(commentModel.getPoster());
        holder.comment.setText(commentModel.getComments());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView comment;

        public CommentViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.comment_item_username);
            comment = itemView.findViewById(R.id.comment_item_comment);
        }
    }
}

