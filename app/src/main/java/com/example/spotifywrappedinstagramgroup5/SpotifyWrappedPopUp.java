package com.example.spotifywrappedinstagramgroup5;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SpotifyWrappedPopUp extends AppCompatActivity {
    FirebaseFirestore mStore;
    SpotifyWrapPageBinding binding;

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
}
