package com.example.healthconnect_v1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ConsultationActivity extends AppCompatActivity {

    private ListView consultationsListView;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        consultationsListView = findViewById(R.id.consultationsListView);
        dbHelper = new HealthDatabaseHelper(this);

        displayAllMedications(); // Display all medications
    }

    private void displayAllMedications() {
        // Fetch all medications
        Cursor cursor = dbHelper.getAllMedications();

//        if (cursor == null || cursor.getCount() == 0) {
//            Toast.makeText(this, "No medications found.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Define the columns to be displayed
        String[] fromColumns = {"diagnosis", "medication", "dosage", "duration", "consultation_date", "cause_of_visit"};
        int[] toViews = {
                R.id.diagnosisTextView,
                R.id.medicationTextView,
                R.id.dosageTextView,
                R.id.durationTextView,
                R.id.dateTextView,
                R.id.causeTextView
        };

        // Create the SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.medication_item, // Custom row layout
                cursor,
                fromColumns,
                toViews,
                0
        );

        consultationsListView.setAdapter(adapter);
    }
}




