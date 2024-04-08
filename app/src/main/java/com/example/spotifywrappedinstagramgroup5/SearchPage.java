package com.example.spotifywrappedinstagramgroup5;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
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
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.home_button) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.profile_button) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });

        userSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Perform search operation
                    String userNameSearch = String.valueOf(userSearch.getText());
                    searchForUser(userNameSearch, new Callback() {
                        @Override
                        public void onSuccess(String username) {
                            Toast.makeText(SearchPage.this, String.format("User %s.", username), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNotFound() {
                            Toast.makeText(SearchPage.this, "User not found.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle error if needed
                        }
                    });
                    return true; // Consume the event
                }
                return false; // Allow other listeners to handle the event
            }
        });
    }
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();



//    /**
//     * Method to search for profiles after user inputs profile username.
//     * @param user username to search for
//     */
//    private void searchForUser(String user, final Callback callback) {
//        DocumentReference docRef = mStore.collection("UserData").document(user);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()) {
//                        String uuid = doc.getString("Username");
//                        callback.onSuccess(uuid); // Display user in some manner
//                    } else {
//                        callback.onNotFound(); // Notify caller that user was not found
//
//                    }
//                } else {
//                    callback.onError(task.getException()); // Notify caller of error
//                }
//            }
//
//        });
//    }

    private void searchForUser(String username, final Callback callback) {
        mStore.collection("UserData")
                .whereEqualTo("Username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // If there's at least one document found
                                DocumentSnapshot doc = querySnapshot.getDocuments().get(0); // Assuming there's only one matching document
                                String uuid = doc.getString("UUID");
                                callback.onSuccess(uuid);
                            } else {
                                callback.onNotFound();
                            }
                        } else {
                            callback.onError(task.getException());
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