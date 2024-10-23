package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.Objects;

public class InputOtpActivity extends AppCompatActivity {
    private Button nextButton;
    private TextView otpCode;
    private TextView phoneNumberPrompt;
    private TextView resendOtp;
    private ProgressBar otpProgressBar;
    private String phoneNumber;

    private FirebaseAuth mAuth;
    private String verificationId; // Store the verification ID sent by Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otp_register);

        // Initialize UI components
        initializeUIComponents();

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Get the phone number passed from the previous activity
        retrievePhoneNumberFromIntent();

        // Display the prompt with the phone number
        displayPhoneNumberPrompt();

        // Send OTP to the phone number
        sendOtp(phoneNumber);

        // Set up click listener for the next button
        setNextButtonClickListener();
    }

    private void initializeUIComponents() {
        nextButton = findViewById(R.id.btn_next);
        otpCode = findViewById(R.id.txt_otp_code);
        phoneNumberPrompt = findViewById(R.id.txt_number_prompt);
        otpProgressBar = findViewById(R.id.otp_progressBar);
        resendOtp = findViewById(R.id.txt_resend_otp);
    }

    private void retrievePhoneNumberFromIntent() {
        phoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("phoneNumber");
    }

    private void displayPhoneNumberPrompt() {
        phoneNumberPrompt.setText(getString(R.string.the_otp_has_been_sent_to_your_phone_number) + phoneNumber);
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show(); // Debugging purpose
    }

    private void setNextButtonClickListener() {
        nextButton.setOnClickListener(view -> {
            String enteredOtp = otpCode.getText().toString().trim();
            // Handle OTP verification logic here (if needed)
            // For now, we can navigate directly to the next activity

            navigateToRegisterActivity();
        });
    }

    private void navigateToRegisterActivity() {
        Intent intent = new Intent(InputOtpActivity.this, RegisterInputActivity.class);
        intent.putExtra("PHONE_NUMBER", phoneNumber); // Pass the phone number to RegisterActivity
        startActivity(intent);
        finish(); // Close OtpActivity to prevent going back to it
    }

    /**
     * Method to trigger sending an OTP to the provided phone number.
     * Firebase is used to send OTP and manage the verification process.
     */
    private void sendOtp(@NonNull String phoneNumber) {
        // Show the progress bar while sending OTP
        setInProgress(true);

        // Build the PhoneAuthOptions with required parameters like phone number and timeout
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout for OTP verification
                        .setActivity(this)                   // Bind activity for callbacks
                        .setCallbacks(mCallbacks)            // Callbacks for verification
                        .build();
        // Send the OTP using Firebase's verifyPhoneNumber method
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /**
     * Method to manage the UI when an action is in progress (like sending OTP).
     * It shows or hides the progress bar based on the progress state.
     * @param inProgress Boolean indicating whether to show or hide progress.
     */
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            // Hide the send OTP button and show the progress bar
            nextButton.setVisibility(View.GONE);
            otpProgressBar.setVisibility(View.VISIBLE);
        } else {
            // Show the send OTP button and hide the progress bar
            nextButton.setVisibility(View.VISIBLE);
            otpProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Callbacks for phone number verification events like OTP sent, verification completed, or failed.
     * This handles events during the OTP verification process.
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(verificationId, token);
                    // Save the verification ID for later use
                    InputOtpActivity.this.verificationId = verificationId;
                    setInProgress(false);
                    // Log and display a message indicating that OTP was sent
                    Log.d("OTP Verification", "Verification code sent: " + verificationId);
                    Toast.makeText(InputOtpActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // Auto-retrieved OTP code if available
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        // Display the OTP for informational purposes
                        Toast.makeText(InputOtpActivity.this, "OTP received: " + code, Toast.LENGTH_SHORT).show();
                        // Attempt sign-in with the received credential
                        signIn(phoneAuthCredential);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    // Display error message if OTP verification fails
                    Toast.makeText(InputOtpActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Hide the progress bar and show the button again
                    setInProgress(false);
                }
            };

    /**
     * Method to sign in using the phone authentication credential.
     * This is called when OTP verification is successful.
     * @param credential The PhoneAuthCredential obtained after OTP verification.
     */
    private void signIn(@NonNull PhoneAuthCredential credential) {
        // Handle successful sign-in logic here
        navigateToRegisterActivity();
    }
}
