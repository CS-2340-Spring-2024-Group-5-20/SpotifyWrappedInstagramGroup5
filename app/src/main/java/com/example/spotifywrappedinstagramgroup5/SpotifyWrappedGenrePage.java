package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedGenrePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpotifyWrappedGenrePage extends AppCompatActivity {
    SpotifyWrappedGenrePageBinding binding;
    WrappedModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SpotifyWrappedGenrePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the data passed from the previous activity
        if (getIntent() != null) {
            Context context = this;
            String userId = getIntent().getStringExtra("userID");
            List<String> tracks = getIntent().getStringArrayListExtra("tracks");

            List<String> artists = getIntent().getStringArrayListExtra("artists");
            List<String> genres = getIntent().getStringArrayListExtra("genres");
            // Populate the genre text views
            if (genres != null && genres.size() >= 5) {
                Toast.makeText(context.getApplicationContext(), "made it here", Toast.LENGTH_SHORT).show();

                binding.genreSpot1.setText(userId);
                binding.genreSpot2.setText(genres.get(1));
                binding.genreSpot3.setText(genres.get(2));
                binding.genreSpot4.setText(genres.get(3));
                binding.genreSpot5.setText(genres.get(4));
            } else {
                binding.genreSpot1.setText("UNAVAILABLE");
                binding.genreSpot2.setText("UNAVAILABLE");
                binding.genreSpot3.setText("UNAVAILABLE");
                binding.genreSpot4.setText("UNAVAILABLE");
                binding.genreSpot5.setText("UNAVAILABLE");
            }
            ImageView forward = findViewById(R.id.forward_arrow);
            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new Intent object for the SpotifyWrappedGenrePage activity
                    Intent newIntent = new Intent(getApplicationContext(), SpotifyWrappedLastPageFestival.class);
                    // Put extras with the appropriate keys and values
                    newIntent.putExtra("userID", userId);
                    newIntent.putStringArrayListExtra("tracks", (ArrayList<String>) tracks);
                    newIntent.putStringArrayListExtra("artists", (ArrayList<String>) artists);
                    // Start the new activity with the newIntent
                    startActivity(newIntent);
                }
            });
        }
        ImageView backButton = findViewById(R.id.escape);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });




        ImageView likeButton = null;
//        likeButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                List<String> tracks = getIntent().getStringArrayListExtra("tracks");
//                FirebaseFirestore mStore = FirebaseFirestore.getInstance();
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                FirebaseUser curr = mAuth.getCurrentUser();
//                CollectionReference wraps = mStore.collection("Wraps");
//                wraps.whereEqualTo("tracks", tracks)
//                        .get()
//                        .addOnSuccessListener(queryDocumentSnapshots -> {
//                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                Map<String, Object> wrapData = document.getData();
//                                if (wrapData.containsKey("tracks") && (wrapData.get("tracks").equals(tracks))) {
//                                    toggleLike((String) wrapData.get("postId"));
//                                }
//                            }
//                        });
//
//                // toggleLike("cbf8da5d-0882-4b60-aeb9-9aa77c804ab6");
//                // MUST FIND WAY TO PERSIST POST ID INTO THIS CLASS.
//
//            }
//        });
    }
    private void toggleLike(String postId) {
        Context context = this;
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference wrapsRef = mStore.collection("Wraps");
        // create reference to Wraps collection
        TextView likeCountTextView = findViewById(R.id.likesCountTextView);
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
                        likeCountTextView.setText(String.valueOf(likes));

                    }
                });
    }

}
