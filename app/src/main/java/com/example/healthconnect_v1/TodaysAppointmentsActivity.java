package com.example.healthconnect_v1;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TodaysAppointmentsActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private TextView appointmentsTextView;
    private String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_appointments);



        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                finish();
            }
        });

        // Initialize database helper
        dbHelper = new HealthDatabaseHelper(this);

        // Get today's date from intent
        todayDate = getIntent().getStringExtra("TODAY_DATE");

        // Initialize views
        appointmentsTextView = findViewById(R.id.appointmentsTextView);

        // Load and display today's appointments
        loadTodaysAppointments();
    }

    private void loadTodaysAppointments() {

            // Fetch all appointments from the database
            Cursor cursor = dbHelper.getAllAppointments();
            StringBuilder appointmentsText = new StringBuilder();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                    String appointmentDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    String appointmentTime = cursor.getString(cursor.getColumnIndexOrThrow("time"));

                    appointmentsText.append("Patient ID: ").append(patientId)
                            .append("\nAppointment Date: ").append(appointmentDate)
                            .append("\nAppointment Time: ").append(appointmentTime)
                            .append("\n\n");
                } while (cursor.moveToNext());
                cursor.close();
            } else {
                appointmentsText.append("No appointments found.");
            }

            // Display appointments in TextView
            appointmentsTextView.setText(appointmentsText.toString());
        }



//        if (appointmentsText.length() == 0) {
//            appointmentsTextView.setText("No appointments scheduled for today.");
//        } else {
//            appointmentsTextView.setText(appointmentsText.toString());
//        }
    }

