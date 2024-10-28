package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.InputPhoneNumberActivity;
import com.example.fleursonthego.R;
import com.example.fleursonthego.views.Dashboard; // Import your Dashboard activity
import com.example.fleursonthego.views.LoginActivity; // Import your Login activity
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    // UI components
    Button getStartedButton;
    TextView txt_navigate_to_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        if (mAuth.getCurrentUser() != null) {
            // User is signed in, redirect to the dashboard
            Intent intent = new Intent(IntroActivity.this, Dashboard.class);
            startActivity(intent);
            finish(); // Call finish to prevent going back to this activity
            return; // Exit the method to prevent further execution
        }

        getStartedButton = findViewById(R.id.getStartedButton);
        txt_navigate_to_login = findViewById(R.id.txt_to_login);

        getStartedButton.setOnClickListener(v -> navigateToMain());
        txt_navigate_to_login.setOnClickListener(v -> navigateToLogin());
    }

    // Navigates to MainActivity
    private void navigateToMain() {
        Intent intent = new Intent(IntroActivity.this, InputPhoneNumberActivity.class);
        startActivity(intent);
    }

    // Navigates to LoginActivity
    private void navigateToLogin() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
