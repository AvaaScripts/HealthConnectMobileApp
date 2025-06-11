package com.example.healthconnect_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView buttonSignUp;
    private HealthDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize database helper
        dbHelper = new HealthDatabaseHelper(this);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Handle login button click
        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Navigate to HomePageActivity
                Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                startActivity(intent);
//                finish(); // Close the SignInActivity


            }

            // Validate user credentials
//            if (dbHelper.validateUser(username, password)) {
//                Toast.makeText(SignInActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
//                return;
//
//            } else {
//                Toast.makeText(SignInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//            }
        });


        // Handle sign-up navigation
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
