package com.example.spotifywrappedinstagramgroup5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfile extends AppCompatActivity {

    private EditText editTextOldEmail, editTextNewEmail, editTextPassword;
    private Button updateButton, authenticateButton, backButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        editTextOldEmail = findViewById(R.id.edit_profile_old_email);
        editTextNewEmail = findViewById(R.id.edit_profile_new_email);
        editTextPassword = findViewById(R.id.edit_profile_current_password);
        updateButton = findViewById(R.id.update_button);
        authenticateButton = findViewById(R.id.authenticate_button);
        backButton = findViewById(R.id.back_button);

        editTextOldEmail.setText(user != null ? user.getEmail() : "Unknown");
        editTextNewEmail.setEnabled(false);
        updateButton.setEnabled(false);

        backButton.setOnClickListener(v -> finish());
        authenticateButton.setOnClickListener(v -> authenticateUser(user));
        updateButton.setOnClickListener(v -> attemptUpdateEmail(user));
    }

    private void authenticateUser(FirebaseUser user) {
        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter password for authentication.");
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProfile.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                editTextNewEmail.setEnabled(true);
                updateButton.setEnabled(true);
                authenticateButton.setEnabled(false);
            } else {
                Toast.makeText(EditProfile.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptUpdateEmail(FirebaseUser user) {
        String newEmail = editTextNewEmail.getText().toString();
        if (TextUtils.isEmpty(newEmail)) {
            editTextNewEmail.setError("Enter a new email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            editTextNewEmail.setError("Enter a valid email address");
            return;
        }
        if (newEmail.equals(user.getEmail())) {
            editTextNewEmail.setError("New email cannot be the same as the old email");
            return;
        }
        updateEmail(user, newEmail);
    }

    private void updateEmail(FirebaseUser user, String newEmail) {
        user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProfile.this, "Email updated successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(EditProfile.this, "Failed to update email.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
