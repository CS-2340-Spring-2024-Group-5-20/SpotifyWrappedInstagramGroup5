package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Adapter.UserAdapter;
import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.databinding.ProfilePageSearchPopoutBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfilePageSearchPopOut extends AppCompatActivity {
    ProfilePageSearchPopoutBinding binding;
    private Context context;


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

        Button followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser == null) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentUserEmail = currentUser.getEmail();
                User userToFollow = UserAdapter.getUser();

                if (currentUserEmail == null || userToFollow == null) {
                    Toast.makeText(context, "Error retrieving user information", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference userDoc = mStore.collection("UserData").document(currentUserEmail);

                userDoc.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, proceed with update
                        userDoc.update("Followers", FieldValue.arrayUnion(userToFollow.getUsername()))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Successfully followed " + userToFollow.getUsername(), Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error following user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Document does not exist
                        Toast.makeText(context, "User document does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Error fetching document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


                // Update the Followers array
                userDoc.update("Followers", FieldValue.arrayUnion(userToFollow.getUsername()))
                        .addOnSuccessListener(aVoid -> {
                            // Show success message
                            Toast.makeText(context, "Successfully followed " + userToFollow.getUsername(), Toast.LENGTH_SHORT).show();
                        });
//                        .addOnFailureListener(e -> {
//                            // Show error message
//                            Toast.makeText(context, "Error following user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
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
