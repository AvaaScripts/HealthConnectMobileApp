package com.example.healthconnect_v1;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MedicationTrackingActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private int patientId;
    private EditText medicationNameEditText, dosageEditText, durationEditText, causeOfVisitEditText;
    private Button addMedicationButton, selectConsultationDateButton;
    private TextView medicationsTextView, selectedConsultationDateTextView;
    private EditText diagnosisEditText;

    private String consultationDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_tracking); // Set layout once



        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                finish();
            }
        });


        dbHelper = new HealthDatabaseHelper(this);

        // Get patient ID from the intent
        patientId = getIntent().getIntExtra("PATIENT_ID", -1);
        Log.d("MedicationTracking", "Received PATIENT_ID = " + patientId);

        if (patientId == -1) {
            Toast.makeText(this, "Invalid patient ID!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if patient ID is invalid
            return;
        }

        // Initialize views
        medicationNameEditText = findViewById(R.id.medicationNameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        durationEditText = findViewById(R.id.durationEditText);
        causeOfVisitEditText = findViewById(R.id.causeOfVisitEditText);
        addMedicationButton = findViewById(R.id.addMedicationButton);
        selectConsultationDateButton = findViewById(R.id.selectConsultationDateButton);
        medicationsTextView = findViewById(R.id.medicationsTextView);
        selectedConsultationDateTextView = findViewById(R.id.selectedConsultationDateTextView);
        diagnosisEditText = findViewById(R.id.diagnosisEditText);

        // Consultation Date Picker
        selectConsultationDateButton.setOnClickListener(v -> {
            Log.d("MedicationTracking", "Select Consultation Date button clicked");

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MedicationTrackingActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        consultationDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedConsultationDateTextView.setText("Consultation Date: " + consultationDate);
                        Log.d("MedicationTracking", "Selected date: " + consultationDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Add Medication
        addMedicationButton.setOnClickListener(v -> {
            String diagnosis = diagnosisEditText.getText().toString();
            String medicationName = medicationNameEditText.getText().toString();
            String dosage = dosageEditText.getText().toString();
            String duration = durationEditText.getText().toString();
            String causeOfVisit = causeOfVisitEditText.getText().toString();

            if (!diagnosis.isEmpty() && !medicationName.isEmpty() && !dosage.isEmpty() &&
                    !duration.isEmpty() && !consultationDate.isEmpty() && !causeOfVisit.isEmpty()) {
                boolean result = dbHelper.addMedicationWithDiagnosis(patientId, diagnosis, medicationName, dosage, duration, consultationDate, causeOfVisit);
                if (result) {
                    Toast.makeText(MedicationTrackingActivity.this, "Medication added successfully!", Toast.LENGTH_SHORT).show();
                    loadMedications(); // Refresh the displayed medication list
                    clearFields();
                } else {
                    Toast.makeText(MedicationTrackingActivity.this, "Failed to add medication!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MedicationTrackingActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            }
        });



        // Load existing medications for the patient
        loadMedications();
    }
    private void clearFields() {
        diagnosisEditText.setText("");
        medicationNameEditText.setText("");
        dosageEditText.setText("");
        durationEditText.setText("");
        causeOfVisitEditText.setText("");
        consultationDate = "";
        selectedConsultationDateTextView.setText("Consultation Date:");
    }

    private void loadMedications() {
        // Retrieve medications for the given patient ID
        Cursor cursor = dbHelper.getMedications(patientId);
        StringBuilder medicationsText = new StringBuilder();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Fetch the new Diagnosis field and existing fields
                String diagnosis = cursor.getString(cursor.getColumnIndexOrThrow("diagnosis"));
                String medication = cursor.getString(cursor.getColumnIndexOrThrow("medication"));
                String dosage = cursor.getString(cursor.getColumnIndexOrThrow("dosage"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String consultationDate = cursor.getString(cursor.getColumnIndexOrThrow("consultation_date"));
                String causeOfVisit = cursor.getString(cursor.getColumnIndexOrThrow("cause_of_visit"));

                // Append all the details to the display string
                medicationsText.append("Diagnosis: ").append(diagnosis)
                        .append("\nMedication: ").append(medication)
                        .append("\nDosage: ").append(dosage)
                        .append("\nDuration: ").append(duration)
                        .append("\nConsultation Date: ").append(consultationDate)
                        .append("\nCause of Visit: ").append(causeOfVisit)
                        .append("\n\n");
            }
            cursor.close();
        }

        // Update the TextView or show a default message if no medications found
        if (medicationsText.length() == 0) {
            medicationsTextView.setText("No medications found.");
        } else {
            medicationsTextView.setText(medicationsText.toString());
        }
    }

}
