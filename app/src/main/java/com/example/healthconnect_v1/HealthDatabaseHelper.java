package com.example.healthconnect_v1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HealthDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "HealthConnect.db";
    private static final int DATABASE_VERSION = 4;

    // Users Table for Sign In/Sign Up functionality
    public static final String TABLE_USERS = "users"; // Added this line
    public static final String COLUMN_USERNAME = "username"; // Added this line
    public static final String COLUMN_PASSWORD = "password"; // Added this line

    // Table for Patients
    public static final String TABLE_PATIENTS = "patients";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_HISTORY = "history";

    private static final String TABLE_APPOINTMENTS = "Appointments";
    private static final String COLUMN_PATIENT_ID = "patient_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";

    // Consultation History Table
    private static final String TABLE_CONSULTATIONS = "Consultations";
//    private static final String COLUMN_DETAILS = "details";

    // Medication Tracking Table
    private static final String TABLE_MEDICATIONS = "Medications";
    public static String COLUMN_DETAILS = "details";


    public HealthDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        // Create Patients Table
        String createPatientsTable = "CREATE TABLE " + TABLE_PATIENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_CONTACT + " TEXT, " +
                COLUMN_HISTORY + " TEXT)";
        db.execSQL(createPatientsTable);

        String createAppointmentsTable = "CREATE TABLE " + TABLE_APPOINTMENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PATIENT_ID + " INTEGER, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_ID + "))";
        db.execSQL(createAppointmentsTable);

        // Create Consultations Table
        String createConsultationsTable = "CREATE TABLE " + TABLE_CONSULTATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PATIENT_ID + " INTEGER, " +
                COLUMN_DETAILS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_ID + "))";
        db.execSQL(createConsultationsTable);

        // Create Medications Table
        String createMedicationsTable = "CREATE TABLE " + TABLE_MEDICATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PATIENT_ID + " INTEGER, " +
                "diagnosis TEXT, " + // New column
                "medication TEXT, " +
                "dosage TEXT, " +
                "duration TEXT, " +
                "consultation_date TEXT, " +
                "cause_of_visit TEXT, " +
                "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_ID + "))";
        db.execSQL(createMedicationsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSULTATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS); // Added this line
        onCreate(db);
    }


    // **New Sign Up Functionality**
//    public boolean registerUser(String username, String password) { // Added this method
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USERNAME, username);
//        values.put(COLUMN_PASSWORD, password);
//        long result = db.insert(TABLE_USERS, null, values);
//        db.close();
//        return result != -1;
//    }


    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?",
                new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("Users", null, values);
        return result != -1;
    }


    // Method to check if the user exists in the database
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        return cursor != null && cursor.moveToFirst();
    }






    // Insert Patient
    public boolean addPatient(String name, int age, String gender, String contact, String history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_HISTORY, history);
        long result = db.insert(TABLE_PATIENTS, null, values);
        db.close();
        return result != -1;
    }

    // Get All Patients
    public Cursor getAllPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PATIENTS, null);
    }

    // Delete Patient
    public boolean deletePatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PATIENTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Update Patient
    public boolean updatePatient(int id, String name, int age, String gender, String contact, String history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_HISTORY, history);
        int result = db.update(TABLE_PATIENTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean addAppointment(int patientId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        long result = db.insert(TABLE_APPOINTMENTS, null, values);
        return result != -1;
    }
    public Cursor getAppointments(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)});
    }


    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS, null);
    }


    // Add Consultation
    // Add Consultation
    public boolean addConsultation(int patientId, String details, String consultationDate, String causeOfVisit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put("details", details); // Consultation details
        values.put("consultation_date", consultationDate); // Date of consultation
        values.put("cause_of_visit", causeOfVisit); // Reason for the visit
        long result = db.insert("consultations", null, values);
        db.close();
        return result != -1;
    }


    // Get Consultations for a Patient
    public Cursor getConsultations(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CONSULTATIONS + " WHERE " + COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)});
    }



    // Get all consultations
    public Cursor getAllConsultations() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT consultation_id AS _id, details, consultation_date, cause_of_visit FROM consultations";
        return db.rawQuery(query, null);
    }









    // Add Medication
    public boolean addMedicationWithDiagnosis(int patientId, String diagnosis, String medicationName, String dosage, String duration, String consultationDate, String causeOfVisit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put("diagnosis", diagnosis);
        values.put("medication", medicationName);
        values.put("dosage", dosage);
        values.put("duration", duration);
        values.put("consultation_date", consultationDate);
        values.put("cause_of_visit", causeOfVisit);
        long result = db.insert(TABLE_MEDICATIONS, null, values);
        db.close();
        return result != -1;
    }




    // Add Medication Tracking for a Patient
    public boolean addMedication(int patientId, String medicationName, String dosage, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, patientId);
        values.put("medication", medicationName);
        values.put("dosage", dosage);
        values.put("duration", duration);
        long result = db.insert(TABLE_MEDICATIONS, null, values);
        db.close();
        return result != -1;
    }

    // Get Medications for a Patient
    public Cursor getMedications(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT diagnosis, medication, dosage, duration, consultation_date, cause_of_visit FROM " +
                TABLE_MEDICATIONS + " WHERE " + COLUMN_PATIENT_ID + " = ?", new String[]{String.valueOf(patientId)});
    }

    public Cursor getConsultationHistory(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT consultation_date, diagnosis, medication, dosage, duration, cause_of_visit " +
                        "FROM Medications WHERE patient_id = ?",
                new String[]{String.valueOf(patientId)}
        );
    }

    public Cursor getAllMedications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT patient_id, diagnosis, medication, dosage, duration, consultation_date, cause_of_visit FROM " + TABLE_MEDICATIONS, null);
    }


    public Cursor getPatientById(int patientId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT name FROM patients WHERE id = ?", new String[]{String.valueOf(patientId)});
    }
    public Cursor getAppointmentsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT patient_id, time FROM Appointments WHERE date = ?",
                new String[]{date}
        );
    }











}
