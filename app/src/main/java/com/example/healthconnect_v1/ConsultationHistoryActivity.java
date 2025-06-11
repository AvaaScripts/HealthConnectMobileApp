package com.example.healthconnect_v1;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultationHistoryActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private int patientId;

    private TextView consultationHistoryTextView, patientDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_history);


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

        // Get patient ID from intent
        patientId = getIntent().getIntExtra("PATIENT_ID", -1);
        Log.d("ConsultationHistory", "Received PATIENT_ID: " + patientId);

        // Validate patient ID
        if (patientId == -1) {
            Toast.makeText(this, "Invalid patient ID received. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        patientDetailsTextView = findViewById(R.id.patientDetailsTextView);
        consultationHistoryTextView = findViewById(R.id.consultationHistoryTextView);

        // Fetch and display patient details
        loadPatientDetails();

        // Load and display consultation history
        loadConsultationHistory();
    }

    private void loadPatientDetails() {
        // Fetch patient name from database using patientId
        Cursor cursor = dbHelper.getPatientById(patientId);
        if (cursor != null && cursor.moveToFirst()) {
            String patientName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            patientDetailsTextView.setText("Patient Name: " + patientName + "\nPatient ID: " + patientId);
            cursor.close();
        } else {
            patientDetailsTextView.setText("Patient Name: Unknown\nPatient ID: " + patientId);
        }
    }

    private void loadConsultationHistory() {
        Cursor cursor = dbHelper.getConsultationHistory(patientId);
        StringBuilder historyText = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Fetch consultation details
                String consultationDate = cursor.getString(cursor.getColumnIndexOrThrow("consultation_date"));
                String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
                String medication = cursor.getString(cursor.getColumnIndexOrThrow("medication"));
                String dosage = cursor.getString(cursor.getColumnIndexOrThrow("dosage"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String causeOfVisit = cursor.getString(cursor.getColumnIndexOrThrow("cause_of_visit"));

                // Format consultation details
                historyText.append("Consultation Date: ").append(consultationDate)
                        .append("\nDiagnosis: ").append(diagnosis)
                        .append("\nMedication: ").append(medication)
                        .append("\nDosage: ").append(dosage)
                        .append("\nDuration: ").append(duration)
                        .append("\nCause of Visit: ").append(causeOfVisit)
                        .append("\n\n");
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Display consultation history or default message
        if (historyText.length() == 0) {
            consultationHistoryTextView.setText("No consultations found for this patient.");
        } else {
            consultationHistoryTextView.setText(historyText.toString());
        }
    }
}
