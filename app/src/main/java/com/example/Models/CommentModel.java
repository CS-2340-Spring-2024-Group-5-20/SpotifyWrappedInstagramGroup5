package com.example.Models;

import com.example.spotifywrappedinstagramgroup5.CommentPageCallBack;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentModel {
    private String poster;
    private String comment;

    public CommentModel(String poster, String comment) {
        this.poster = poster;
        this.comment = comment;
    }

    // Adding userId parameter to only load data for a specific user's post
    public static List<CommentModel> loadData(FirebaseFirestore mStore, String postId, CommentPageCallBack callback) {
        List<CommentModel> commentModels = new ArrayList<>();

        mStore.collection("Wraps")
                .whereEqualTo("PostId", postId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Map<String, List<String>> commentsMap = (Map<String, List<String>>) documentSnapshot.getData().get("Comments");

                        if (commentsMap != null) {
                            for (Map.Entry<String, List<String>> entry : commentsMap.entrySet()) {
                                String username = entry.getKey();
                                List<String> userComments = entry.getValue();

                                for (String comment : userComments) {
                                    CommentModel commentModel = new CommentModel(username, comment);
                                    commentModels.add(commentModel);
                                }
                            }
                        }
                    }
                    callback.onCallback(commentModels);
                })
                .addOnFailureListener(e -> callback.onError(e));

        return commentModels;
    }

    public String getPoster() {
        return this.poster;
    }

    public String getComments() {
        return this.comment;
    }
}
