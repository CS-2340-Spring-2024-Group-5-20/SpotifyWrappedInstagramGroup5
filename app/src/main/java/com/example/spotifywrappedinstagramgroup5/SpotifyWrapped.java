package com.example.spotifywrappedinstagramgroup5;

public class SpotifyWrapped {
    private String userUID; //UID of user who made the wrap.
    private final String[] topFiveArtists = new String[5]; // string array for holding names of top 5 artists.
    private final String[] topFiveSongs = new String[5]; //string array for holding names of top 5 songs.
    public SpotifyWrapped(String[] topArtists, String[] topSongs, String userUID) {
        for (int i = 0; i < 5; i++) {
            topFiveArtists[i] = topArtists[i];
            topFiveSongs[i] = topSongs[i];
        }
        this.userUID = userUID;
    }
}
