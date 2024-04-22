package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedInitialPageBinding;

import java.util.ArrayList;
import java.util.List;

public class SpotifyWrappedInitialPage extends AppCompatActivity {
    SpotifyWrappedInitialPageBinding binding;

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
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);  // This will close the current activity and take you back to the previous one
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

            forwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new Intent object for the SpotifyWrappedGenrePage activity
                    Intent newIntent = new Intent(getApplicationContext(), SpotifyWrappedAnotherPage.class);
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
}
