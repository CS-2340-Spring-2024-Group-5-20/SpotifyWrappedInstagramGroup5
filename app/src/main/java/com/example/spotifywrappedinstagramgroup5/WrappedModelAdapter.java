package com.example.spotifywrappedinstagramgroup5;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WrappedModelAdapter extends RecyclerView.Adapter<WrappedModelAdapter.ViewHolder> {

    private List<WrappedModel> wrappedModelList;
    private Activity activity; // Change context to Activity
    private ImageView imageView;
    private ImageView likeButton;

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
                intent.putStringArrayListExtra("tracks", wrappedModel.getTrackList());
                intent.putStringArrayListExtra("artists", wrappedModel.getArtistList());
                intent.putStringArrayListExtra("genres", wrappedModel.getGenreList());
                activity.startActivity(intent); // Use 'activity' instead of 'context'
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleLike(wrappedModel.getPostId());
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
        public ImageView likeButton;



        public ViewHolder(View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.image_post);
            textUsername = itemView.findViewById(R.id.text_username);
            textDescription = itemView.findViewById(R.id.text_description);
            commentButton = itemView.findViewById(R.id.comment_icon);
            likeButton = itemView.findViewById(R.id.LikesButton);
        }
    }
    private void toggleLike(String postId) {
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference wrapsRef = mStore.collection("Wraps");
        // create reference to Wraps collection
        wrapsRef.whereEqualTo("PostId", postId)
                // queries for Wrap in collection with corresponding postId
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> wrapData = document.getData();
                        // creates local hashmap of wrap document
                        if (!wrapData.containsKey("LikedUserIds")) {
                            //checks if LikedUserIds exists already
                            wrapData.put("LikedIds", new ArrayList<String>());
                            //makes LikedUserIds if it does not already exist
                        }
                        ArrayList<String> likedIDS = (ArrayList<String>) wrapData.get("LikedUserIds");
                        // creates local array of liked user ids

                        long likes =  (long) wrapData.get("LikeCount");
                        // creates local number of likes
                        if (likedIDS.contains(currentUser.getUid())) {
                            // checks if user already liked the wrap
                            likes--;
                            likedIDS.remove(currentUser.getUid());
                            // will decrement likes and remove user from "likes" array
                            document.getReference().update("LikedUserIds", likedIDS);
                            document.getReference().update("LikeCount", likes);
                            //updates both relevant fields
                        } else {
                            likes++;
                            likedIDS.add(currentUser.getUid());
                            // will increment likes and add user to likes array
                            document.getReference().update("LikedUserIds", likedIDS);
                            document.getReference().update("LikeCount", likes);
                            // updates both relevant fields

                        }

                    }
                });
    }
}

