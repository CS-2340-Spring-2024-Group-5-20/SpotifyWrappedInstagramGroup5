package com.example.spotifywrappedinstagramgroup5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {


    private EditText editTextOldEmail, editTextNewEmail, editTextPassword;

    private String oldEmail, newEmail, userPassword;
    private Button updateButton, backButton;
    private TextView forgotPasswordRedirect;
    private FirebaseAuth mAuth;

    FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        editTextNewEmail = findViewById(R.id.edit_profile_new_email);
        editTextOldEmail = findViewById(R.id.edit_profile_old_email);
        editTextPassword = findViewById(R.id.edit_profile_current_password);
        updateButton = findViewById(R.id.update_button);
        backButton = findViewById(R.id.back_button);
        forgotPasswordRedirect = findViewById(R.id.forgot_password_redirect);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        updateButton.setEnabled(false);
        editTextNewEmail.setEnabled(false);


        oldEmail = user.getEmail();

        if (user.equals("")) {
            Toast.makeText(EditProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            reAuthenticate(user);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                finish();
            }
        });




    }

    private void reAuthenticate(FirebaseUser user) {
        Button authenticateButton = findViewById(R.id.authenticate_button);
        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userPassword = editTextPassword.getText().toString();
                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(EditProfile.this, "No Password Written", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Please enter password for authentication.");
                    editTextPassword.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, userPassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfile.this, "Password Verified. Update email now.",
                                        Toast.LENGTH_SHORT).show();

                                editTextNewEmail.setEnabled(true);
                                editTextPassword.setEnabled(false);
                                authenticateButton.setEnabled(false);
                                updateButton.setEnabled(true);

                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String newEmail = editTextNewEmail.getText().toString();
                                        if (TextUtils.isEmpty(newEmail)) {
                                            Toast.makeText(EditProfile.this, "Write a new Email", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("Enter new Email");
                                            editTextNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                                            Toast.makeText(EditProfile.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("Enter valid email");
                                            editTextNewEmail.requestFocus();
                                        } else if (newEmail.matches(oldEmail)) {
                                            Toast.makeText(EditProfile.this, "New email cannot be old email", Toast.LENGTH_SHORT).show();
                                            editTextNewEmail.setError("New email cannot be old email");
                                            editTextNewEmail.requestFocus();
                                        } else {
                                            updateEmail(user);
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    private void updateEmail(FirebaseUser user) {
        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    user.sendEmailVerification();

                    Toast.makeText(EditProfile.this, "Email updated, verification email sent.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }


}