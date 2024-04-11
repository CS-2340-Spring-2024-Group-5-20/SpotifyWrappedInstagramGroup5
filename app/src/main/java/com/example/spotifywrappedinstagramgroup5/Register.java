package com.example.spotifywrappedinstagramgroup5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {



    EditText editTextEmail, editTextPassword;
    Button registerButton;

    String userID;

    FirebaseAuth mAuth;

    FirebaseFirestore mStore;

    TextView textView;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signuppage);
        editTextEmail = findViewById(R.id.signup_username);
        editTextPassword = findViewById(R.id.signup_password);
        registerButton = findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        textView = findViewById(R.id.loginRedirectText);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                                    userID = mAuth.getCurrentUser().getUid();

//
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("Username", email); //initializes default username to be user's email
                                    data.put("UUID", userID); // creates UUID field within document for query purposes
                                    data.put("Wraps", new ArrayList<>()); // initializes initial array of wraps to be empty.
                                    //the wraps arraylist will be where the user puts their wraps
                                    data.put("Followers", new ArrayList<>()); // initializes initial array of followers to be empty.
                                    //the followers arraylist will contain UUID's and Usernames of all followers (tentative)
                                    data.put("FaveArtists", new ArrayList<>()); //Arraylist of the users favorite genres and artists
                                    //has to be both in one arraylist because app crashes if i add another arraylist
                                    data.put("FaveSongs", new ArrayList<>()); // arraylist containing user's top 3 songs

                                    //storing spotify data
                                    data.put("Spotify Token", "");
                                    data.put("Spotify Code", "");

//                                  data.put("FaveGenres", new ArrayList<>()); // does not work with this array added


                                    mStore.collection("UserData").document(userID)
                                    .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Document successfully written.");
                                                }
                                            });
                                    CollectionReference userDataReference = mStore.collection("Userdata"); // reference to Userdata collection
                                    Query query = userDataReference.whereEqualTo("Username", email); // query to search for documents in the collection where username field matches email




                                    Intent intent = new Intent(getApplicationContext(), SpotifyLogin.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}