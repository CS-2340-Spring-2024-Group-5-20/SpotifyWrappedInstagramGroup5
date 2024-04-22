package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedInitialPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SpotifyWrappedInitialPage extends AppCompatActivity {
    SpotifyWrappedInitialPageBinding binding;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_wrapped_initial_page);


        binding = SpotifyWrappedInitialPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConstraintLayout constraintLayout = findViewById(R.id.spotifyWrappedInitialPage);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        // Set up the back button
        ImageView backButton = findViewById(R.id.escape);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });
        // Start animation for TypeWriter
        TypeWriter initialPageText = findViewById(R.id.initialPageText);
        initialPageText.startAnimation();

        ImageView forwardButton = findViewById(R.id.imageView);
        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("userID");
            List<String> tracks = intent.getStringArrayListExtra("tracks");
            List<String> artists = intent.getStringArrayListExtra("artists");
            List<String> genres = intent.getStringArrayListExtra("genres");



                    // toggleLike("cbf8da5d-0882-4b60-aeb9-9aa77c804ab6");
                    // MUST FIND WAY TO PERSIST POST ID INTO THIS CLASS.




            forwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new Intent object for the SpotifyWrappedGenrePage activity
                    Intent newIntent = new Intent(getApplicationContext(), SpotifyWrappedGenrePage.class);
                    // Put extras with the appropriate keys and values
                    newIntent.putExtra("userID", userId);
                    newIntent.putStringArrayListExtra("tracks", (ArrayList<String>) tracks);
                    newIntent.putStringArrayListExtra("artists", (ArrayList<String>) artists);
                    newIntent.putStringArrayListExtra("genres", (ArrayList<String>) genres);
                    // Start the new activity with the newIntent
                    startActivity(newIntent);
                }
            });
        }
    }
    private void toggleLike(String postId) {
        Context context = this;
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
