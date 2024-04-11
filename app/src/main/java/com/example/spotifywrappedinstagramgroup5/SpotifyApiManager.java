package com.example.spotifywrappedinstagramgroup5;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Class managing integration with the Spotify API
 */
public class SpotifyApiManager {
    public static final String CLIENT_ID = "ca9ec80d6f114d96b610cfcb74f3d05c";
    public static final String REDIRECT_URI = "spotifywrappedinstagramgroup5://auth";
    public static final String SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1/";

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private FirebaseUser user;

    private String userId;

    public SpotifyApiManager(Activity context, String mAccessCode, String mAccessToken, FirebaseFirestore mStore, FirebaseAuth mAuth) {
        if (mAccessToken == null || mAccessCode == null) {
            Logger.log("Access token or code was null");
            throw new RuntimeException("SpotifyAPIManager failed to construct.");
        }

        if (mAuth == null || mStore == null) {
            throw new RuntimeException("Firebase was not loaded correctly into API manager");
        }

        this.mAccessCode = mAccessCode;
        this.mAccessToken = mAccessToken;

        this.mAuth = mAuth;
        this.mStore = mStore;

        user = mAuth.getCurrentUser();
        userId = user.getUid();
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    public Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked(Activity context) {
        if (mAccessToken == null) {
            Toast.makeText(context, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(context, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    //setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(context, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Generic API call to read data from Spotify
     */
    public CompletableFuture<JSONArray> makeApiCall(Activity context, String endpoint, String parameters) {
        CompletableFuture<JSONArray> future = new CompletableFuture<>();

        if (mAccessToken == null) {
            Toast.makeText(context, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            future.completeExceptionally(new IOException("Access token was null"));
        } else {

            String url = SPOTIFY_API_BASE_URL + endpoint;
            if (parameters != null && !parameters.isEmpty()) {
                url += "?" + parameters;
            }

            // Create a request
            final Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();


            cancelCall();
            mCall = mOkHttpClient.newCall(request);

            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("HTTP", "Failed to fetch data: " + e);
                    Toast.makeText(context, "Failed to fetch data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                    future.completeExceptionally(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseBody = new JSONObject(response.body().string());
                            JSONArray artists = responseBody.getJSONArray("items");
                            future.complete(artists); // Complete with the result
                        } catch (JSONException e) {
                            Logger.log("Failed to parse data: " + e);
                            Toast.makeText(context, "Failed to parse data, watch Logcat for more details",
                                    Toast.LENGTH_SHORT).show();
                            future.completeExceptionally(e);
                        }
                    } else {
                        Logger.log("Error: " + response.code() + " " + response.message());
                        future.completeExceptionally(new IOException("Unexpected response code: " + response.code()));
                    }
                }
            });
        }
        return future;
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        //runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get top tracks
     */
    public void getTopTracks(Activity context, int limit) throws JSONException {
        String endpoint = "me/top/tracks";
        String parameters = "limit=" + limit;
        CompletableFuture<JSONArray> result = makeApiCall(context, endpoint, parameters);
        JSONArray tracks = result.join(); // Block and get the result

        List<String> trackNames = new ArrayList<>();

        for (int i = 0; i < tracks.length(); i++) {
            JSONObject trackObject = tracks.getJSONObject(i);
            String trackName = trackObject.getString("name");
            trackNames.add(trackName);
        }

        HashMap<String, Object> trackData = new HashMap<>();
        trackData.put("FaveSongs", trackNames);

        mStore.collection("UserData").document(userId)
                .update(trackData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Songs successfully written.");
                    }
                });
    }

    /**
     * Get top artists
     */
    public void getTopArtists(Activity context, int limit) throws JSONException {
        String endpoint = "me/top/artists";
        String parameters = "limit=" + limit;
        CompletableFuture<JSONArray> result = makeApiCall(context, endpoint, parameters);
        JSONArray artistsArray = result.join(); // Block and get the result

        Log.d("test", artistsArray.toString());

        List<String> artistNames = new ArrayList<>();

        for (int i = 0; i < artistsArray.length(); i++) {
            JSONObject artistObject = artistsArray.getJSONObject(i);
            String artistName = artistObject.getString("name");
            artistNames.add(artistName);
        }

        HashMap<String, Object> artistsData = new HashMap<>();
        artistsData.put("FaveArtists", artistNames);

        mStore.collection("UserData").document(userId)
                .update(artistsData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully written.");
                    }
                });

    }

    public void saveCreds(Activity context) {
        Map<String, Object> spotifyData = new HashMap<>();
        spotifyData.put("Spotify Token", mAccessToken);
        spotifyData.put("Spotify Code", mAccessCode);

        mStore.collection("UserData").document(userId)
                .update(spotifyData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully written.");
                    }
                });
    }
}
