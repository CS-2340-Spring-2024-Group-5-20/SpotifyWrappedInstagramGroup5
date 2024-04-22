package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentProfilepageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class ProfilePage extends AppCompatActivity {
    FragmentProfilepageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseFirestore mStore;

    Button editProfileButton, signOutButton, myWrapButton;
    FirebaseUser user;
//    Button button; (NOT NEEDED ANYMORE CURRENTLY)
    Toolbar toolbar;

    TextView favArtist, mostPlayed, username;
    DocumentReference docRef;

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
        mStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        docRef = mStore.collection("UserData").document(user.getUid());

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

//        ImageView imageView = findViewById(R.id.three_dot_extension);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle overflow menu click
//                // For example, navigate to SettingsActivity
//                Intent intent = new Intent(ProfilePage.this, Logout.class);
//                startActivity(intent);
//            }
//        });

        editProfileButton = findViewById(R.id.edit_profile_button);
        signOutButton = findViewById(R.id.signout_button);
        myWrapButton = findViewById(R.id.my_wrap_button);

        favArtist = findViewById(R.id.text_favorite_artist);
        mostPlayed = findViewById(R.id.text_most_played);

        username = findViewById(R.id.username);
        username.setText(user != null ? user.getEmail().replace("@gmail.com", "") : "Paul");

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> artists = (List<String>) documentSnapshot.get("FaveArtists");
                favArtist.setText(TextUtils.join(", ", artists));
            } else {
                Log.d("Document", "No such document");
            }
        }).addOnFailureListener(e -> Log.d("Document", "Failed with: ", e));

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> songs = (List<String>) documentSnapshot.get("FaveSongs");
                mostPlayed.setText(TextUtils.join(", ", songs));
            } else {
                Log.d("Document", "No such document");
            }
        }).addOnFailureListener(e -> Log.d("Document", "Failed with: ", e));

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignoutPage.class);
                startActivity(intent);
            }
        });

        myWrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyWraps.class);
                startActivity(intent);
            }
        });
    }

}