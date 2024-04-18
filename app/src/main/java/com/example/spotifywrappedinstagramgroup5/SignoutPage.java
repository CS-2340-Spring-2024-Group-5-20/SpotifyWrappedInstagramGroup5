package com.example.spotifywrappedinstagramgroup5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignoutPage extends AppCompatActivity {

    Button signOutButton, backButton, deleteButton;

    FirebaseAuth mAuth;

    FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signout_page);

        signOutButton = findViewById(R.id.signout_button);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_button);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call sign out method
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("Auth", "User account deleted.");
                                Toast.makeText(SignoutPage.this, "User Profile Deleted", Toast.LENGTH_SHORT).show();
                                mStore.collection("UserData").document(user.getUid()).delete();
                            } else {
                                Toast.makeText(SignoutPage.this, "Log out and Log back in again before deleting", Toast.LENGTH_SHORT).show();
                                Log.w("Auth", "Failed to delete user account", task.getException());
                            }
                        });
                Intent intent = new Intent(SignoutPage.this, Login.class);
                startActivity(intent);
            }
        });
    }
}