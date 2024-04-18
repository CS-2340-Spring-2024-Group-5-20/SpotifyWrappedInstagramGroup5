package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Adapter.UserAdapter;
import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentSearchpageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    FragmentSearchpageBinding binding;
    FirebaseAuth auth;
    EditText userSearch;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ArrayList<User> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();  // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSearchpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // UI Components
        userSearch = binding.searchButton; // Assuming you have searchField as ID for your EditText
        recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        userAdapter = new UserAdapter(this, list);
        recyclerView.setAdapter(userAdapter);

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


        // Event listener for the search input
        userSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call the search function with the updated text
                performSearch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing needed here
            }
        });
    }

    // Inside your SearchPage class
    private void performSearch(String queryText) {
        if (!queryText.isEmpty()) {
            User.loadUsersBySearch(queryText, db, new DataCallBackSearch() {
                @Override
                public void onCallback(List<User> users) {
                    list.clear();
                    list.addAll(users);
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                }
            });
        } else {
            list.clear();
            userAdapter.notifyDataSetChanged();
        }
    }
}
