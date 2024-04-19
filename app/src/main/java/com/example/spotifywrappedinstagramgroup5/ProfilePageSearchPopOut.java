package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Adapter.UserAdapter;
import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.databinding.ProfilePageSearchPopoutBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.SpotifyWrapPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfilePageSearchPopOut extends AppCompatActivity {
    ProfilePageSearchPopoutBinding binding;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfilePageSearchPopoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.spotify_wrap_page);


        // Set up the back button
        ImageView backButton = findViewById(R.id.profilePagePopUpBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });

        Button followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFollower(UserAdapter.getUser().getUsername());
                 }
        });

        if (getIntent() != null) {
            String username = getIntent().getStringExtra("username");
            String faveArtist = getIntent().getStringExtra("faveArtists");
            String mostPlayed = getIntent().getStringExtra("mostPlayed");

            binding.profilePagePopUpUsername.setText(username);
            binding.profilePagePopUpFavArtist.setText(faveArtist);
            binding.profilePagePopUpMostPlayed.setText(mostPlayed);
        }
    }
    private void addFollower(String personToFollow) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        CollectionReference targetUserDataRef = mStore.collection("UserData");
        // get reference to UserData document in firestore
        targetUserDataRef.whereEqualTo("Username",personToFollow)
                // Query document to look for the target user we need to follow.
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->  {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> data = document.getData();
                        String targetUUID = (String) data.get("UUID");
                        // gets their document once found, puts it into the local data hashmap.
                        if (data.get("Username").equals(personToFollow)) {
                            // checking to make sure we have the right one
                            Object followersData = data.get("Followers");
                            // puts their array of followers into local variable
                            if (followersData == null) {
                                followersData = new ArrayList<>();
                                // makes followers array if not already present
                            }
                            if (!data.containsKey("Following")) {
                                //checks to see if they have a following array of people theyre following.
                                data.put("Following", new ArrayList<String>());
                                // puts following array into local data object
                                mStore.collection("UserData").document(targetUUID).set(data);
                                // sets local data object to be new remote userdata document (solely to put the following arraylist in there).
                            }
                            if (followersData instanceof ArrayList) {
                                ArrayList<String> followersArray = (ArrayList<String>) data.get("Followers");
                                //puts followersArray into ArrayList object
                                if (!followersArray.contains(currentUser.getEmail())) {
                                    //checks if user is in the array already.
                                    followersArray.add(currentUser.getEmail());
                                    //adds user into followers array, signifying current user is now following target user.
                                    document.getReference().update("Followers", followersArray);
                                    // updates the corresponding field accordingly.
                                    Toast.makeText(context.getApplicationContext(), "Following Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context.getApplicationContext(), "Already Following User", Toast.LENGTH_SHORT).show();
                                    // displays if already following user.
                                }
                            }
                        }
                    }
                });
        CollectionReference currentUserDataRef = mStore.collection("UserData");
        // creates new collection reference
        currentUserDataRef.whereEqualTo("Username", currentUser.getEmail())
                // queries UserData db for the data document of the currently logged in user
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                   for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                       Map<String, Object> data = document.getData();
                       // creates local variable of the data document's hashmap
                       if (data.get("Username").equals(currentUser.getEmail())) {
                           // makes sure we have the right one
                           if (!data.containsKey("Following")) {
                               // checks if following array is present
                               data.put("Following", new ArrayList<>());
                               // adds if not
                           }
                           ArrayList<String> thisUserFollowingArray = (ArrayList<String>) data.get("Following");
                           // creates local variable for the following array
                           if (!thisUserFollowingArray.contains(personToFollow)) {
                               // checks to make sure we are not already following them
                               thisUserFollowingArray.add(personToFollow);
                               // adds user we just followed into our following array.
                               // this completes both ways of the operation. The user is now following someone,
                               // and we are now a follower of the target user.
                               mStore.collection("UserData").document((String) data.get("UUID")).set(data);
                               //replaces existing data hashmap in firestore with our up to date local hashmap.
                           }
                       }
                   }
                });

    }
}
