package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.databinding.CommentPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CommentPage extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();  // Firestore instance

    private Context context = this;
    public String thisPostID;
    public String postOwner;
    CommentPageBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CommentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView backButton = findViewById(R.id.commentPageBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // This will close the current activity and take you back to the previous one
            }
        });
        Button postCommentButton = findViewById(R.id.button_post);
        EditText commentBox = findViewById(R.id.edit_text_content);
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = auth.getCurrentUser().getEmail().replace("@gmail.com", "");
                String comment = commentBox.getText().toString();
                addComment(comment,"cbf8da5d-0882-4b60-aeb9-9aa77c804ab6", userId);
                // need to get postID to persist across state changes.
            }
        });

    }
    protected void setThisPostID(String postID) {
        thisPostID = postID;
    }
    protected void setPostOwner(String postOwnerUserID) {
        postOwner = postOwnerUserID;
    }
    private void addComment (String comment, String postID, String commenterID) {
        if (!comment.isEmpty()) {
            CollectionReference wrapsRef = db.collection("Wraps");
            // Example: Query wraps for matching postID
            wrapsRef.whereEqualTo("PostId", postID)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Handle the list of documents that match the query
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Process each document
                            Map<String, Object> data = document.getData();
                            if (data.get("PostId").equals(postID)) { // the wrap has been found.
                                Object commentsData = data.get("Comments"); // put comments arraylist in object
                                Object commentorData = data.get("CommentedIds"); // put commentedID's arraylist in object

                                if (commentsData == null) {
                                    commentsData = new ArrayList<>();
                                }

                                if (commentorData == null) {
                                    commentorData = new ArrayList<>();
                                }

                                if (commentsData instanceof Map && commentorData instanceof ArrayList) {

                                    // ensuring both arraylists are indeed arraylists and operations can proceed.
                                    Map<String, String> comments = (Map<String, String>) data.get("Comments");
                                    // put comments map into local object
                                    comments.put(commenterID, comment);
                                    // add comment to comments map under key of userID, value is actual comment
                                    document.getReference().update("Comments", comments);
                                    // updates comments map in firestore to reflect changes in local object

                                    ArrayList<Object> commentedIds = (ArrayList<Object>) data.get("CommentedIds");
                                    // put commentedIds into local object
                                    if (!commentedIds.contains(commenterID)) {
                                        commentedIds.add((commenterID));
                                    }
                                    // add commenterID string to commentedIds local arraylist (if not already present)
                                    document.getReference().update("CommentedIds", commentedIds);
                                    // updates comments arraylist in firestore with changes in local object
                                    Toast.makeText(context.getApplicationContext(), "Comment Successful", Toast.LENGTH_SHORT).show();
                                    // success message
                                    break;
                                    // breaks loop
                                }
                            }

                        }
                        finish(); // perhaps replace with a call to update all comments on page.

                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        Log.e("FirestoreQuery", "Error querying Firestore", e);
                    });

        }
    }
}
