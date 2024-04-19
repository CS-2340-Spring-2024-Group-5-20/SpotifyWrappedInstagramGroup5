package com.example.spotifywrappedinstagramgroup5;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WrappedModel {
    private String description;
    private List<String> topArtists;
    private List<String> topGenres;
    private List<String> topTracks;
    private String userId;
    private int likes;
    private String identifier;

    public WrappedModel(String description, List<String> topArtists, List<String> topGenres, List<String> topTracks, String userId, String identifier) {
        this.description = description;
        this.topGenres = topGenres;
        this.topTracks = topTracks;
        this.userId = userId;
        this.topArtists = topArtists;
        this.likes = 0;
        this.identifier = identifier;
    }

    public static List<WrappedModel> loadData(FirebaseFirestore mStore, FirebaseAuth mAuth, DataCallback callback) {
        List<WrappedModel> wrappedModels = new ArrayList<>();

        mStore.collection("Wraps")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String description = documentSnapshot.getString("Description");
                        List<String> topArtists = (List<String>) documentSnapshot.get("TopArtists");
                        List<String> topGenres = (List<String>) documentSnapshot.get("TopGenres");
                        List<String> topTracks = (List<String>) documentSnapshot.get("TopTracks");
                        String userId = mAuth.getCurrentUser().getEmail().replace("@gmail.com", "");
                        String postID = documentSnapshot.getString("PostId");
                        WrappedModel wrappedModel = new WrappedModel(description, topArtists, topGenres, topTracks, userId, postID);
                        wrappedModels.add(wrappedModel);
                    }
                    callback.onCallback(wrappedModels);
                })
                .addOnFailureListener(e -> callback.onError(e));


        return wrappedModels;
    }

    public String getDescription(){
        return this.description;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getTracks(){
        return topTracks != null ? TextUtils.join(", ", this.topTracks) : "null";
    }

    public String getArtists(){
        return topArtists != null ? TextUtils.join(", ", this.topArtists) : "";
    }

    public String getGenres(){
        return topGenres != null ? TextUtils.join(", ", this.topGenres) : "";
    }
    public String getLikes() {
        return (String.valueOf(likes));
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
