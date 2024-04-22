package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentPublishPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class PublishPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private SpotifyApiManager manager;

    FragmentPublishPageBinding binding; // Corrected binding class
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_publish_page);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference docRef = mStore.collection("UserData").document(userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String token = (String)documentSnapshot.get("Spotify Token");
                String code = (String)documentSnapshot.get("Spotify Code");
                manager = new SpotifyApiManager(PublishPage.this, code, token, mStore, mAuth);
            } else {
                Log.d("Document", "No such document");
            }
        }).addOnFailureListener(e -> Log.d("Document", "Failed with: ", e));

        Spinner postTitleSpinner = findViewById(R.id.postTitleSpinner);
        Button postButton = findViewById(R.id.postButton);

        // Load post titles into the spinner
        loadPostTitles(postTitleSpinner);

        // Button click listener to post the selected title
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTitle = postTitleSpinner.getSelectedItem().toString();
                String description = "";
                postWrap(selectedTitle, description);
            }
        });

        toolbar = findViewById(R.id.publish_top_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ImageView backButton = findViewById(R.id.escape);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadPostTitles(final Spinner spinner) {
        WrappedModel.loadMyData(mStore, mAuth, new DataCallback() {
            @Override
            public void onCallback(List<WrappedModel> wrappedModels) {
                // Extract titles from WrappedModel objects
                String[] titles = new String[wrappedModels.size()];
                for (int i = 0; i < wrappedModels.size(); i++) {
                    titles[i] = wrappedModels.get(i).getTitle();
                }

                // Populate spinner with titles
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PublishPage.this,
                        android.R.layout.simple_spinner_item, titles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(PublishPage.this, "Error loading post titles: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postWrap(String title, String description) {
        WrappedModel.loadMyData(mStore, mAuth, new DataCallback() {
                    @Override
                    public void onCallback(List<WrappedModel> wrappedModels) {
                        for (WrappedModel wrappedModel : wrappedModels) {
                            if (wrappedModel.getTitle().equals(title)) {
                                manager.postWrapped(wrappedModel.getPostId(), wrappedModel.getUserId(), "");
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(PublishPage.this, "Error loading post titles: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        Toast.makeText(this, "Posting wrap with title: " + title, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
