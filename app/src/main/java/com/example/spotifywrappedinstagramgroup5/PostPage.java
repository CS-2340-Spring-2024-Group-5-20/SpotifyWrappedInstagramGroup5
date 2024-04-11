package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentPostpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostPage extends AppCompatActivity {
    FragmentPostpageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentPostpageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());

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
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.search_button) {
                Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.profile_button) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            }
            return true;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> test = new HashMap<>();
        test.put("FirstPost", "One");

        EditText editTextContent = binding.editTextContent;

        binding.buttonPost.setOnClickListener(view -> {
            // Get the content from EditText
            String content = editTextContent.getText().toString().trim();

            if (!content.isEmpty()) {
                // Create a map to store the post
                Map<String, Object> post = new HashMap<>();
                post.put("PostOne", content); // You can add more fields here

                // Add a new document with a generated ID
                db.collection("PostTests").document("JSR")
                        .set(post)
                        .addOnSuccessListener(documentReference -> Toast.makeText(PostPage.this, "Successfully Posted", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(PostPage.this, "Error adding post", Toast.LENGTH_SHORT).show());
            } else {
                // Handle case where EditText is empty
                Toast.makeText(PostPage.this, "Post content cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
//        db.collection("PostTest").document("JSR").set(test).addOnCompleteListener(new OnCompleteListener<Void>() {
//
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(PostPage.this, "Test string added.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }
}