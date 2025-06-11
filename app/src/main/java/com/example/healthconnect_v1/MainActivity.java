package com.example.healthconnect_v1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthconnect_v1.HealthDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private ListView patientListView;
    private ArrayList<String> patientList;
    private ArrayList<Integer> patientIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this layout matches your XML


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                finish();
            }
        });

        // Initialize components
        initializeComponents();

        // Load Patients
        loadPatients();

        // Add Patient Button
        setAddPatientButtonListener();

        // On Patient Click
        setPatientClickListener();
    }

    /**
     * Initializes the components used in this activity.
     */
    private void initializeComponents() {
        dbHelper = new HealthDatabaseHelper(this);
        patientListView = findViewById(R.id.patientListView); // Ensure this id matches XML
        if (patientListView == null) {
            Log.e("MainActivity2", "ListView 'patientListView' is null. Check your XML layout.");
            Toast.makeText(this, "Error loading patient list. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Loads the patients from the database and updates the ListView.
     */
    private void loadPatients() {
        if (dbHelper == null) {
            Log.e("MainActivity2", "Database helper is not initialized.");
            return;
        }

        patientList = new ArrayList<>();
        patientIds = new ArrayList<>();

        Cursor cursor = dbHelper.getAllPatients();

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        patientList.add(name);
                        patientIds.add(id);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("MainActivity2", "Error reading patients from database.", e);
            } finally {
                cursor.close();
            }
        }

        // Update ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, patientList);
        if (patientListView != null) {
            patientListView.setAdapter(adapter);
        }
    }

    /**
     * Sets up the listener for the "Add Patient" button.
     */
    private void setAddPatientButtonListener() {
        Button addPatientButton = findViewById(R.id.addPatientButton); // Ensure this id matches XML
        if (addPatientButton != null) {
            addPatientButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddPatientActivity.class)));
        } else {
            Log.e("MainActivity2", "Add Patient Button not found. Check your XML layout.");
        }
    }

    /**
     * Sets the click listener for the patient ListView items.
     */
    private void setPatientClickListener() {
        if (patientListView != null) {
            patientListView.setOnItemClickListener((adapterView, view, position, id) -> {
                int selectedPatientId = patientIds.get(position);
                Intent intent = new Intent(MainActivity.this, PatientDetailsActivity.class);
                intent.putExtra("PATIENT_ID", selectedPatientId);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPatients(); // Refresh the patient list when resuming the activity
    }
}
