package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.InputPhoneNumberActivity;
import com.example.fleursonthego.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import utils.AndroidUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText txtPhoneNum, txtPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private CountryCodePicker ccpCountryCode;
    private FirebaseAuth mAuth;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initializeUI();
        ccpCountryCode.registerCarrierNumberEditText(txtPhoneNum);

        findViewById(R.id.signUpTextView).setOnClickListener(v -> navigateToSignUp());
        btnLogin.setOnClickListener(v -> {
            if (ccpCountryCode.isValidFullNumber()) {
                sendOtp(ccpCountryCode.getFullNumberWithPlus());
                login();
            } else {
                txtPhoneNum.setError("Phone number is not valid");
            }
        });
    }

    private void initializeUI() {
        txtPhoneNum = findViewById(R.id.txt_phone_num);
        txtPassword = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        ccpCountryCode = findViewById(R.id.ccp_countryCode_login);
        mAuth = FirebaseAuth.getInstance();
    }

    private void setInProgress(boolean inProgress) {
        btnLogin.setVisibility(inProgress ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
    }

    private void sendOtp(String phoneNumber) {
        setInProgress(true);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void login() {
        String phoneNumber = txtPhoneNum.getText().toString();
        String password = txtPassword.getText().toString();
        // Add login logic here
    }

    private void navigateToSignUp() {
        startActivity(new Intent(this, InputPhoneNumberActivity.class));
    }

    private void navigateToOtpActivity(String phoneNumber) {
        Intent intent = new Intent(this, InputLoginOtpActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("verificationId", otp);
        startActivity(intent);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    otp = verificationId;
                    navigateToOtpActivity(ccpCountryCode.getFullNumberWithPlus());
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    String code = credential.getSmsCode();
                    if (code != null) {
                        AndroidUtils.showToast(LoginActivity.this, "OTP received");
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setInProgress(false);
                }
            };
}
