package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedGenrePageBinding;

import java.util.ArrayList;
import java.util.List;

public class SpotifyWrappedGenrePage extends AppCompatActivity {
    SpotifyWrappedGenrePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SpotifyWrappedGenrePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the data passed from the previous activity
        if (getIntent() != null) {
            String userId = getIntent().getStringExtra("userID");
            List<String> tracks = getIntent().getStringArrayListExtra("tracks");
            List<String> artists = getIntent().getStringArrayListExtra("artists");
            List<String> genres = getIntent().getStringArrayListExtra("genres");
            // Populate the genre text views
            if (genres != null && genres.size() >= 5) {
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
    }

}
