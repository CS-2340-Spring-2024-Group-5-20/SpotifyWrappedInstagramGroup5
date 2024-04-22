package com.example.spotifywrappedinstagramgroup5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SignoutPage.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        mStore.collection("UserData").document(userId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firestore", "User document successfully deleted!");
                                        user.delete()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Log.d("Auth", "User account deleted.");
                                                        Toast.makeText(SignoutPage.this, "User Profile Deleted", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignoutPage.this, Login.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(SignoutPage.this, "Failed to delete user account", Toast.LENGTH_SHORT).show();
                                                        Log.w("Auth", "Failed to delete user account", task.getException());
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firestore", "Error deleting user document", e);
                                        Toast.makeText(SignoutPage.this, "Failed to delete user document", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}