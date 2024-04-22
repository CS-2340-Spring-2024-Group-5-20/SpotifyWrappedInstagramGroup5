package com.example.spotifywrappedinstagramgroup5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotifywrappedinstagramgroup5.R;

import org.w3c.dom.Comment;

import java.util.List;

public class WrappedModelAdapter extends RecyclerView.Adapter<WrappedModelAdapter.ViewHolder> {

    private List<WrappedModel> wrappedModelList;
    private Activity activity; // Change context to Activity
    private ImageView imageView;

    public WrappedModelAdapter(Activity activity, List<WrappedModel> wrappedModelList) {
        this.activity = activity;
        this.wrappedModelList = wrappedModelList;
    }

    @NonNull
    @Override
    public WrappedModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.homepage_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WrappedModelAdapter.ViewHolder holder, int position) {
        WrappedModel wrappedModel = wrappedModelList.get(position);
        holder.textUsername.setText(wrappedModel.getTitle()); // Assuming the userId is the username
        holder.textDescription.setText(wrappedModel.getDescription());

        holder.imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SpotifyWrappedInitialPage.class); // Use 'activity' instead of 'context'
                intent.putExtra("userID", wrappedModel.getUserId());
                intent.putExtra("description", wrappedModel.getDescription());
                intent.putExtra("tracks", wrappedModel.getTracks());
                intent.putExtra("artists", wrappedModel.getArtists());
                intent.putExtra("genres", wrappedModel.getGenres());
                activity.startActivity(intent); // Use 'activity' instead of 'context'
            }
        });

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CommentPage.class); // Use 'activity' instead of 'context'
                intent.putExtra("postID", wrappedModel.getPostId());
                activity.startActivity(intent); // Use 'activity' instead of 'context'
            }
        });

        Log.d("e", wrappedModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return wrappedModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagePost;
        public TextView textUsername;
        public TextView textDescription;
        public ImageView commentButton;



        public ViewHolder(View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.image_post);
            textUsername = itemView.findViewById(R.id.text_username);
            textDescription = itemView.findViewById(R.id.text_description);
            commentButton = itemView.findViewById(R.id.comment_icon);
        }
    }
}

