package com.example.spotifywrappedinstagramgroup5;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.ProfilePageSearchPopoutBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfilePageSearchPopOut extends AppCompatActivity {
    ProfilePageSearchPopoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfilePageSearchPopoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.spotify_wrap_page);


        // Set up the back button
        ImageView backButton = findViewById(R.id.profilePagePopUpBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });

        if (getIntent() != null) {
            String username = getIntent().getStringExtra("username");
            String faveArtist = getIntent().getStringExtra("faveArtists");
            String mostPlayed = getIntent().getStringExtra("mostPlayed");

            binding.profilePagePopUpUsername.setText(username);
            binding.profilePagePopUpFavArtist.setText(faveArtist);
            binding.profilePagePopUpMostPlayed.setText(mostPlayed);
        }
    }
}
