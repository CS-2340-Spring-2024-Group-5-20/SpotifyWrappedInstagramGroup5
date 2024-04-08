package com.example.spotifywrappedinstagramgroup5;
import java.util.HashMap;
import java.util.Enumeration;

public class SpotifyWrapped  {
    private String userUID; //UID of user who made the wrap.
    private final String[] topFiveArtists = new String[5]; // string array for holding names of top 5 artists.
    private final String[] topFiveSongs = new String[5]; //string array for holding names of top 5 songs.
    private int likes;
    private HashMap<String, String> comments; //first string is user UID, second is for the actual comment.
    public SpotifyWrapped(String[] topArtists, String[] topSongs, String userUID) {
        for (int i = 0; i < 5; i++) {
            topFiveArtists[i] = topArtists[i];
            topFiveSongs[i] = topSongs[i];
        }
        this.likes = 0;
        this.userUID = userUID;
        this.comments = new HashMap<String, String>(); //UUID as the key, comment as the value.
    }
}
