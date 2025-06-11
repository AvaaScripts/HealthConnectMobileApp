package com.example.healthconnect_v1;




import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPatientActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;

    private EditText nameEditText, ageEditText, genderEditText, contactEditText, historyEditText;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        dbHelper = new HealthDatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        contactEditText = findViewById(R.id.contactEditText);
        historyEditText = findViewById(R.id.historyEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Save Button Action
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String ageStr = ageEditText.getText().toString();
            String gender = genderEditText.getText().toString();
            String contact = contactEditText.getText().toString();
            String history = historyEditText.getText().toString();

            if (validateInputs(name, ageStr, gender, contact)) {
                int age = Integer.parseInt(ageStr);
                boolean isInserted = dbHelper.addPatient(name, age, gender, contact, history);

                if (isInserted) {
                    Toast.makeText(AddPatientActivity.this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Return to previous activity
                } else {
                    Toast.makeText(AddPatientActivity.this, "Failed to add patient", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel Button Action
        cancelButton.setOnClickListener(v -> finish());
    }

    // Validate Inputs
    private boolean validateInputs(String name, String ageStr, String gender, String contact) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.isDigitsOnly(ageStr)) {
            Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(contact)) {
            Toast.makeText(this, "Contact is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
