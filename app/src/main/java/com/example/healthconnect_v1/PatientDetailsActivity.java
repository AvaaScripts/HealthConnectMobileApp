package com.example.healthconnect_v1;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PatientDetailsActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private int patientId;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                finish();
            }
        });


        dbHelper = new HealthDatabaseHelper(this);

        patientId = getIntent().getIntExtra("PATIENT_ID", -1);
        TextView patientDetailsTextView = findViewById(R.id.patientDetailsTextView);
        Button updateDeleteButton = findViewById(R.id.updateDeleteButton);
        Button bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        Button consultationHistoryButton = findViewById(R.id.consultationHistoryButton);
        Button medicationTrackingButton = findViewById(R.id.medicationTrackingButton);

        // Load Patient Details
        Cursor cursor = dbHelper.getAllPatients();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == patientId) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                    String contact = cursor.getString(cursor.getColumnIndexOrThrow("contact"));
                    String history = cursor.getString(cursor.getColumnIndexOrThrow("history"));

                    patientDetailsTextView.setText(
                            "Name: " + name + "\n" +
                                    "Age: " + age + "\n" +
                                    "Gender: " + gender + "\n" +
                                    "Contact: " + contact + "\n" +
                                    "Medical History: " + history
                    );
                    break;
                }
            } while (cursor.moveToNext());
        }




        // Update/Delete Button
        updateDeleteButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDetailsActivity.this, UpdateDeleteActivity.class);
            intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
        });

        // Book Appointment Button
        bookAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDetailsActivity.this, AppointmentActivity.class);
            intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
        });

        //Consultation History Button
        consultationHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDetailsActivity.this, ConsultationHistoryActivity.class);
            intent.putExtra("PATIENT_ID", patientId); // Ensure patientId is set
            startActivity(intent);


        });

        //Medication Tracking Button
        medicationTrackingButton.setOnClickListener(v -> {
           Intent intent = new Intent(PatientDetailsActivity.this, MedicationTrackingActivity.class);
           intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
        });

//        // Cancel Button Action
//        cancelButton.setOnClickListener(v -> finish());

    }
}
