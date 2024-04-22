package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedAnotherPageBinding;
import java.util.ArrayList;
import java.util.List;

public class SpotifyWrappedAnotherPage extends AppCompatActivity {
    SpotifyWrappedAnotherPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SpotifyWrappedAnotherPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the data passed from the previous activity
        if (getIntent() != null) {
            String userId = getIntent().getStringExtra("userID");
            List<String> tracks = getIntent().getStringArrayListExtra("tracks");
            List<String> artists = getIntent().getStringArrayListExtra("artists");
            List<String> genres = getIntent().getStringArrayListExtra("genres");
            // Populate the genre text views
            if (genres != null && artists.size() >= 5) {
                binding.artistsSpot1.setText(artists.get(1));
                binding.artistsSpot2.setText(artists.get(2));
                binding.artistsSpot3.setText(artists.get(3));
                binding.artistsSpot4.setText(artists.get(4));
                binding.artistsSpot5.setText(artists.get(5));
            } else {
                binding.artistsSpot1.setText("UNAVAILABLE");
                binding.artistsSpot2.setText("UNAVAILABLE");
                binding.artistsSpot3.setText("UNAVAILABLE");
                binding.artistsSpot4.setText("UNAVAILABLE");
                binding.artistsSpot5.setText("UNAVAILABLE");
            }
            ImageView forward = findViewById(R.id.forward_arrow);
            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a new Intent object for the SpotifyWrappedGenrePage activity
                    Intent newIntent = new Intent(getApplicationContext(), SpotifyWrappedGenrePage.class);
                    // Put extras with the appropriate keys and values
                    newIntent.putExtra("userID", userId);
                    newIntent.putStringArrayListExtra("tracks", (ArrayList<String>) tracks);
                    newIntent.putStringArrayListExtra("genres", (ArrayList<String>) genres);
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
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);            }
        });
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });
    }

}
