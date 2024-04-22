package com.example.Models;
import android.text.TextUtils;

import com.example.spotifywrappedinstagramgroup5.DataCallBackSearch;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<String> faveArtists;
    private List<String> mostPlayed;

    public User(String username, List<String> faveArtists, List<String> mostPlayed) {
        this.username = username;
        this.faveArtists = faveArtists != null ? faveArtists : new ArrayList<>();
        this.mostPlayed = mostPlayed != null ? mostPlayed : new ArrayList<>();
    }

    public static void loadUsersBySearch(String searchText, FirebaseFirestore mStore, DataCallBackSearch callback) {
        String start = searchText;
        String end = searchText + "\uf8ff";

        mStore.collection("UserData")
                .whereGreaterThanOrEqualTo("Username", start)
                .whereLessThanOrEqualTo("Username", end)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String username = documentSnapshot.getString("Username");
                        List<String> profileArtists = (List<String>) documentSnapshot.get("FaveArtists");
                        List<String> profileGenres = (List<String>) documentSnapshot.get("FaveSongs");
                        User user = new User(username, profileArtists, profileGenres);
                        users.add(user);
                    }
                    callback.onCallback(users);
                })
                .addOnFailureListener(e -> callback.onError(e));
    }

    public String getUsername() {
        return this.username;
    }

    public String getFaveArtists() {
        if (faveArtists == null || faveArtists.isEmpty()) {
            return "The user has not listened to any music recently.";
        }
        return TextUtils.join(", ", this.faveArtists);
    }

    public String getMostPlayed() {
        if (mostPlayed == null || mostPlayed.isEmpty()) {
            return "The user has not listened to any music recently.";
        }
        return TextUtils.join(", ", this.mostPlayed);
    }
}
