package com.example.healthconnect_v1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;




import com.bumptech.glide.Glide;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);  // Set your splash screen layout

        // Find the ImageView and load the GIF using Glide
        ImageView splashImage = findViewById(R.id.splash_image);
        Glide.with(this).load(R.drawable.stat1).into(splashImage); // Replace 'stat2' with your actual drawable name

        // Set a delay before transitioning to the main activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Transition to the main activity
            Intent intent = new Intent(SplashActivity.this, InfoScreenOne.class);  // Replace MainActivity with your actual main activity class
            startActivity(intent);
            finish();  // Close the splash screen activity so it doesn't show again
        }, 7000);  // 8-second delay before transitioning
    }
}