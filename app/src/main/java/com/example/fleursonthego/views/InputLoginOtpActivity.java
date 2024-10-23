package com.example.fleursonthego.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.R;

import java.util.Objects;

public class InputLoginOtpActivity extends AppCompatActivity {
    private Button nextButton;
    private TextView otpCode;
    private TextView phoneNumberPrompt;
    private ProgressBar otpProgressBar;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);

        initializeUIComponents();
        retrievePhoneNumberFromIntent();
        displayPhoneNumberPrompt();
    }

    private void retrievePhoneNumberFromIntent() {
        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phoneNumber");
    }


    private void displayPhoneNumberPrompt() {
        phoneNumberPrompt.setText(getString(R.string.the_otp_has_been_sent_to_your_phone_number) + phoneNumber);
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show(); // Debugging purpose
    }
    private void initializeUIComponents() {
        nextButton = findViewById(R.id.btn_next);
        otpCode = findViewById(R.id.txt_otp_code);
        phoneNumberPrompt = findViewById(R.id.txt_number_prompt);
        otpProgressBar = findViewById(R.id.otp_progressBar);
    }
}