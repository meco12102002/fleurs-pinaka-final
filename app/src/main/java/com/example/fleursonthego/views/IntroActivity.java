package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.InputPhoneNumberActivity;
import com.example.fleursonthego.R;

public class IntroActivity extends AppCompatActivity {


    //dito yung mga UI components
    Button getStartedButton;
    TextView txt_navigate_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

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
