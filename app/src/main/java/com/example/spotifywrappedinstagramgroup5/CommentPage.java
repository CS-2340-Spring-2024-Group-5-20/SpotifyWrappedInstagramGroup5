package com.example.spotifywrappedinstagramgroup5;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.CommentPageBinding;

public class CommentPage extends AppCompatActivity {
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
    }
}
