package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profilepage);

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();

        Button returnButton = findViewById(R.id.logout_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity to return to the login page
                Intent intent = new Intent(getApplicationContext(), Logout.class);
                startActivity(intent);
                // Finish the current activity
            }
        });
    }
}