package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentProfilepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfilePage extends AppCompatActivity {
    FragmentProfilepageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;
//    Button button; (NOT NEEDED ANYMORE CURRENTLY)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProfilepageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());
//        button = findViewById(R.id.logout_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Logout.class);
//                startActivity(intent);
//            }
//        });

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();

        // Set up bottom navigation menu
        BottomNavigationView bottomMenu = binding.bottomMenu; // Corrected reference to bottom menu
        bottomMenu.setBackground(null); // Set background to null if needed
        binding.bottomMenu.setBackground(null);
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_button) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.search_button) {
                Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.post_button) {
                Intent intent = new Intent(getApplicationContext(), PostPage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
        toolbar = findViewById(R.id.profile_top_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ImageView imageView = findViewById(R.id.three_dot_extension);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle overflow menu click
                // For example, navigate to SettingsActivity
                Intent intent = new Intent(ProfilePage.this, Logout.class);
                startActivity(intent);
            }
        });
    }

}