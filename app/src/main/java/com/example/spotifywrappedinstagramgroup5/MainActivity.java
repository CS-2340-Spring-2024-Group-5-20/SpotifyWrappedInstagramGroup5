package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.spotifywrappedinstagramgroup5.ProfilePage;
import com.example.spotifywrappedinstagramgroup5.R.layout;
import com.example.spotifywrappedinstagramgroup5.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    ActivityMainBinding binding;

    //    Button button; old usage of button
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        button = (Button) findViewById(R.id.middle_menu_button); old usage of button
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.bottomMenu.setBackground(null);
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_button) {
                com.example.spotifywrappedinstagramgroup5.HomePage reminderPage = new com.example.spotifywrappedinstagramgroup5.HomePage();
//                changeFragment(reminderPage);
            } else if (item.getItemId() == R.id.search_button) {
                com.example.spotifywrappedinstagramgroup5.SearchPage searchPage = new com.example.spotifywrappedinstagramgroup5.SearchPage();
//                changeFragment(searchPage);
            } else if (item.getItemId() == R.id.post_button) {
                Intent intent = new Intent(getApplicationContext(), PostPage.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.profile_button) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
            return true;
        });
//        binding.middleMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {buttonClicked();}
//        });
//        changeFragment(new ReminderPage()); old stuff
    }
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(MainActivity.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

}