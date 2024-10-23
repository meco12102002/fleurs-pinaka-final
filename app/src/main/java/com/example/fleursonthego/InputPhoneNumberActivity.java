package com.example.fleursonthego;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.views.InputOtpActivity;
import com.hbb20.CountryCodePicker;

public class InputPhoneNumberActivity extends AppCompatActivity {

    private Button sendPhoneNumberButton;
    private EditText txt_mobileNum;
    private CountryCodePicker ccp_countryCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_phone_number_activity);

        // Initialize all UI components
        initializeUIComponents();

        // Set click listener for the button
        sendPhoneNumberButton.setOnClickListener(view -> {
            // Validate the entered phone number
            if (!ccp_countryCode.isValidFullNumber()) {
                txt_mobileNum.setError("Phone number is not valid");
                return;
            }
            // Proceed to the next screen
            navigateToNextActivity(ccp_countryCode.getFullNumberWithPlus());
        });
    }

    /**
     * Method to initialize the UI components like buttons, text fields, and progress bar.
     */
    private void initializeUIComponents() {
        sendPhoneNumberButton = findViewById(R.id.btn_sendOtp); // Can rename this button in your layout if OTP is removed
        txt_mobileNum = findViewById(R.id.txt_mobileNum);
        ccp_countryCode = findViewById(R.id.ccp_countryCode);
        progressBar = findViewById(R.id.pb_phoneNumPage);

        // Initially hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Link the country code picker with the mobile number text field
        ccp_countryCode.registerCarrierNumberEditText(txt_mobileNum);
    }

    /**
     * Method to manage the UI when an action is in progress (like sending phone number).
     * It shows or hides the progress bar based on the progress state.
     * @param inProgress Boolean indicating whether to show or hide progress.
     */
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            // Hide the button and show the progress bar
            sendPhoneNumberButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            // Show the button and hide the progress bar
            sendPhoneNumberButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Method to navigate to the next activity, passing the phone number.
     * @param phoneNumber The phone number entered by the user.
     */
    private void navigateToNextActivity(@NonNull String phoneNumber) {
        // Show a toast for debugging or move to the next activity
        Toast.makeText(InputPhoneNumberActivity.this, "Phone Number: " + phoneNumber, Toast.LENGTH_SHORT).show();

        // Navigate to the next activity (e.g., registration)
        Intent intent = new Intent(InputPhoneNumberActivity.this, InputOtpActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
}
