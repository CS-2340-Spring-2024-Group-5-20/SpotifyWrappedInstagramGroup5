package com.example.spotifywrappedinstagramgroup5;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrappedInitialPageBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SpotifyWrappedInitialPage extends AppCompatActivity {
    FirebaseFirestore mStore;
    SpotifyWrappedInitialPageBinding binding;
    TextView initialPageText;

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
        ImageView backButton = findViewById(R.id.wrapPageBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });
        // Start animation for TypeWriter
        TypeWriter initialPageText = findViewById(R.id.initialPageText);
        initialPageText.startAnimation();
    }
}
