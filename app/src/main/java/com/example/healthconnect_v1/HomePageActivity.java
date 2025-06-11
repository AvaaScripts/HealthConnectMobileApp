package com.example.healthconnect_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity {

    private Button mainActivityButton, todaysAppointmentsButton, viewConsultationsButton, viewPatientButton, bookConsultationButton;
    private TextView appTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Initialize views
        appTitleTextView = findViewById(R.id.appTitleTextView);
        mainActivityButton = findViewById(R.id.mainActivityButton);
        todaysAppointmentsButton = findViewById(R.id.todaysAppointmentsButton);
        viewConsultationsButton = findViewById(R.id.viewConsultationsButton);
        viewPatientButton = findViewById(R.id.ViewPatientButton);
        bookConsultationButton = findViewById(R.id.BookConsultationButton);

        // Set the app title
        appTitleTextView.setText("Health Connect");

        // Button: Navigate to MainActivity
        mainActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button: Show Today's Appointments
        todaysAppointmentsButton.setOnClickListener(v -> {
            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Intent intent = new Intent(HomePageActivity.this, TodaysAppointmentsActivity.class);
            intent.putExtra("TODAY_DATE", todayDate); // Pass today's date to the next activity
            startActivity(intent);
        });

        // Button: View Consultations
        viewConsultationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, AddPatientActivity.class); // Ensure this is the correct activity
            startActivity(intent);
        });

        // Button: View Patients
        viewPatientButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, MainActivity.class); // MainActivity for managing patients
            startActivity(intent);
        });

        // Button: Book a Consultation
        bookConsultationButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, AppointmentActivity.class);
            startActivity(intent);
        });



    }




}
