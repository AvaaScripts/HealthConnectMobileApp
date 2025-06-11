package com.example.healthconnect_v1;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;




public class UpdateDeleteActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private int patientId;
    private EditText nameEditText, ageEditText, genderEditText, contactEditText, historyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

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

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        contactEditText = findViewById(R.id.contactEditText);
        historyEditText = findViewById(R.id.historyEditText);
        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        loadPatientDetails();

        // Update Patient
        updateButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            int age = Integer.parseInt(ageEditText.getText().toString());
            String gender = genderEditText.getText().toString();
            String contact = contactEditText.getText().toString();
            String history = historyEditText.getText().toString();

            boolean isUpdated = dbHelper.updatePatient(patientId, name, age, gender, contact, history);
            if (isUpdated) {
                Toast.makeText(this, "Patient Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to Update Patient", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Patient
        deleteButton.setOnClickListener(v -> {
            boolean isDeleted = dbHelper.deletePatient(patientId);
            if (isDeleted) {
                Toast.makeText(this, "Patient Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to Delete Patient", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPatientDetails() {
        Cursor cursor = dbHelper.getAllPatients();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == patientId) {
                    nameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    ageEditText.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("age"))));
                    genderEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
                    contactEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("contact")));
                    historyEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("history")));
                    break;
                }
            } while (cursor.moveToNext());
        }
    }
}
