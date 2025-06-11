package com.example.healthconnect_v1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentActivity extends AppCompatActivity {

    private HealthDatabaseHelper dbHelper;
    private EditText patientIdEditText;
    private Button selectDateButton, selectTimeButton, saveAppointmentButton;
    private TextView selectedDateTextView, selectedTimeTextView, appointmentDetailsTextView;

    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                finish();
            }
        });

        dbHelper = new HealthDatabaseHelper(this);

        patientIdEditText = findViewById(R.id.patientIdEditText);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        saveAppointmentButton = findViewById(R.id.saveAppointmentButton);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        appointmentDetailsTextView = findViewById(R.id.appointmentDetailsTextView);

        // Date Picker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedDateTextView.setText("Selected Date: " + selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker
        selectTimeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        selectedTimeTextView.setText("Selected Time: " + selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // Save Appointment
        saveAppointmentButton.setOnClickListener(v -> {
            int patientId;
            try {
                patientId = Integer.parseInt(patientIdEditText.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Patient ID!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
                boolean result = dbHelper.addAppointment(patientId, selectedDate, selectedTime);
                if (result) {
                    Toast.makeText(AppointmentActivity.this, "Appointment saved successfully!", Toast.LENGTH_SHORT).show();
                    loadAppointments(patientId); // Refresh appointments display
                    selectedDate = "";
                    selectedTime = "";
                    selectedDateTextView.setText("Selected Date: ");
                    selectedTimeTextView.setText("Selected Time: ");
                } else {
                    Toast.makeText(AppointmentActivity.this, "Failed to save appointment!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AppointmentActivity.this, "Please select date and time!", Toast.LENGTH_SHORT).show();
            }
        });

        // Load appointments if Patient ID is passed from another activity
        int patientIdFromIntent = getIntent().getIntExtra("PATIENT_ID", -1);
        if (patientIdFromIntent != -1) {
            patientIdEditText.setText(String.valueOf(patientIdFromIntent));
            loadAppointments(patientIdFromIntent);
        }
    }





    private void loadAppointments(int patientId) {
        Cursor appointmentsCursor = dbHelper.getAppointments(patientId);
        StringBuilder appointmentsText = new StringBuilder();

        if (appointmentsCursor != null) {
            while (appointmentsCursor.moveToNext()) {
                int currentPatientId = appointmentsCursor.getInt(appointmentsCursor.getColumnIndexOrThrow("patient_id"));
                String date = appointmentsCursor.getString(appointmentsCursor.getColumnIndexOrThrow("date"));
                String time = appointmentsCursor.getString(appointmentsCursor.getColumnIndexOrThrow("time"));

                // Fetch patient name from database
                Cursor patientCursor = dbHelper.getAllPatients();
                String patientName = "";
                if (patientCursor != null) {
                    while (patientCursor.moveToNext()) {
                        if (patientCursor.getInt(patientCursor.getColumnIndexOrThrow("id")) == currentPatientId) {
                            patientName = patientCursor.getString(patientCursor.getColumnIndexOrThrow("name"));
                            break;
                        }
                    }
                    patientCursor.close();
                }

                // Check if the appointment is upcoming
                boolean isUpcoming = checkIfUpcoming(date, time);

                // Append appointment details
                appointmentsText.append("Patient ID: ").append(currentPatientId)
                        .append(", Name: ").append(patientName)
                        .append("\nDate: ").append(date)
                        .append(", Time: ").append(time);
                if (isUpcoming) {
                    appointmentsText.append(" (Upcoming)");
                }
                appointmentsText.append("\n\n");
            }
            appointmentsCursor.close();
        }

        if (appointmentsText.length() == 0) {
            appointmentDetailsTextView.setText("No appointments found.");
        } else {
            appointmentDetailsTextView.setText(appointmentsText.toString());
        }
    }

    private boolean checkIfUpcoming(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date appointmentDateTime = sdf.parse(date + " " + time);
            Date currentDateTime = Calendar.getInstance().getTime();

            // Check if the appointment is within the next 24 hours
            long diffInMillis = appointmentDateTime.getTime() - currentDateTime.getTime();
            return diffInMillis > 0 && diffInMillis <= 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
