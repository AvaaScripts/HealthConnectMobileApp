package com.example.healthconnect_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class InfoScreenOne extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen_one);

        // Find the Next button
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Info Screen Two
                Intent intent = new Intent(InfoScreenOne.this, InfoScreenTwo.class);
                startActivity(intent);
            }
        });
    }
}
