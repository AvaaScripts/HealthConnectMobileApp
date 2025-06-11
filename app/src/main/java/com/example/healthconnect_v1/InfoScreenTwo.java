package com.example.healthconnect_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class InfoScreenTwo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen_two);

        // Find the Next button and set click listener
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignInActivity
                Intent intent = new Intent(InfoScreenTwo.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
