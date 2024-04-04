package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentProfilepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {
    FragmentProfilepageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentProfilepageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());
        button = findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Logout.class);
                startActivity(intent);
            }
        });

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

    }

}