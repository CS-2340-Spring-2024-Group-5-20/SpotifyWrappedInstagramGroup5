package com.example.spotifywrappedinstagramgroup5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {

    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button updatePasswordButton, backButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forgot_password);


        currentPasswordEditText = findViewById(R.id.forget_current_password);
        newPasswordEditText = findViewById(R.id.forget_create_new_password);
        confirmPasswordEditText = findViewById(R.id.forget_confirm_password);
        updatePasswordButton = findViewById(R.id.update_password_button);
        backButton = findViewById(R.id.back_button);

        mAuth = FirebaseAuth.getInstance();

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void changePassword() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String currentPassword = currentPasswordEditText.getText().toString();
            final String newPassword = newPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            // User is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

}