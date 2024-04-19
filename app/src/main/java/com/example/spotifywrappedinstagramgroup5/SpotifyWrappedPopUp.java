package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpotifyWrappedPopUp extends AppCompatActivity {
    FirebaseFirestore mStore;
    SpotifyWrapPageBinding binding;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.spotify_wrap_page);

        binding = SpotifyWrapPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConstraintLayout constraintLayout = findViewById(R.id.spotifyWrappedPageAnimation);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        // Set up the back button
        ImageView backButton = findViewById(R.id.wrapPageBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });

        ImageView likeButton = findViewById(R.id.LikeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleLike("cbf8da5d-0882-4b60-aeb9-9aa77c804ab6");
                // MUST FIND WAY TO PERSIST POST ID INTO THIS CLASS.

            }
        });

        if (getIntent() != null) {
            String userId = getIntent().getStringExtra("userID");
            String tracks = getIntent().getStringExtra("tracks");
            String artists = getIntent().getStringExtra("artists");
            String genres = getIntent().getStringExtra("genres");

            binding.spotifyWrappedUserID.setText("User ID: " + userId);
            binding.spotifyWrappedFavoriteTracks.setText("Favorite Tracks: " + tracks);
            binding.spotifyWrappedFavoriteArtists.setText("Favorite Artists: " + artists);
            binding.spotifyWrappedFavoriteGenres.setText("Favorite Genre: " + genres);
        }
    }
    private void toggleLike(String postId) {
        mStore = FirebaseFirestore.getInstance();
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
                        if (likedIDS.contains(currentUser.getEmail())) {
                            // checks if user already liked the wrap
                            likes--;
                            likedIDS.remove(currentUser.getEmail());
                            // will decrement likes and remove user from "likes" array
                            document.getReference().update("LikedUserIds", likedIDS);
                            document.getReference().update("LikeCount", likes);
                            //updates both relevant fields
                            Toast.makeText(context.getApplicationContext(), "Unliked wrap", Toast.LENGTH_SHORT).show();
                        } else {
                            likes++;
                            likedIDS.add(currentUser.getEmail());
                            // will increment likes and add user to likes array
                            document.getReference().update("LikedUserIds", likedIDS);
                            document.getReference().update("LikeCount", likes);
                            // updates both relevant fields
                            Toast.makeText(context.getApplicationContext(), "Liked wrap", Toast.LENGTH_SHORT).show();

                        }
                    }
                        });
    }
}
