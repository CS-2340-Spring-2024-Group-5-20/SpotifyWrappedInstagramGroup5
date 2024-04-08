package com.example.spotifywrappedinstagramgroup5;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentSearchpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class SearchPage extends AppCompatActivity {
    FragmentSearchpageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;
    EditText userSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSearchpageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());
        userSearch = findViewById(R.id.search_button);



        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();

        // Set up bottom navigation menu
        BottomNavigationView bottomMenu = binding.bottomMenu; // Corrected reference to bottom menu
        bottomMenu.setBackground(null); // Set background to null if needed
        binding.bottomMenu.setBackground(null);
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.post_button) {
                Intent intent = new Intent(getApplicationContext(), PostPage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.home_button) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
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

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameSearch = String.valueOf(userSearch.getText());
                String found;
                searchForUser(userNameSearch, new Callback() {
                    @Override
                    public void onSuccess(String username) {
//                        found = username; //do something
                    }

                    @Override
                    public void onNotFound() {
                        Toast.makeText(SearchPage.this, "User not found.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Exception e) {
                        //hopefully will not throw an error
                    }
                });
            }
        });


    }
    FirebaseFirestore mStore = FirebaseFirestore.getInstance();



    /**
     * Method to search for profiles after user inputs profile username.
     * @param user username to search for
     * @return a single capacity string array containing the UUID of the user's profile which was searched for.
     */
    private void searchForUser(String user, final Callback callback) {
        DocumentReference docRef = mStore.collection("Userdata").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        String uuid = doc.getString("Username");
                        callback.onSuccess(uuid); // Display user in some manner
                    } else {
                        callback.onNotFound(); // Notify caller that user was not found

                    }
                } else {
                    callback.onError(task.getException()); // Notify caller of error
                }
            }
        });
    }

    // Define a callback interface
    interface Callback {
        void onSuccess(String uuid);
        void onNotFound();
        void onError(Exception e);
    }

}