package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fleursonthego.Models.User;
import com.example.fleursonthego.Models.Cart;
import com.example.fleursonthego.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import utils.FirebaseUtil;

public class RegisterInputActivity extends AppCompatActivity {

    private EditText etFullName;
    String phoneNumber;
    private EditText password;
    private Button btn_register;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get reference to Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Initialize UI elements
        etFullName = findViewById(R.id.txt_fullName);
        password = findViewById(R.id.txt_password);
        EditText confirmPassword = findViewById(R.id.txt_confirmPassword);
        btn_register = findViewById(R.id.btn_register);
        phoneNumber = getIntent().getExtras().getString("PHONE_NUMBER");
        // Register button click listener
        btn_register.setOnClickListener(v -> registerUser());
    }

    void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String phoneNumber = getIntent().getExtras().getString("PHONE_NUMBER");
        String email = ""; // Assuming you have a way to get the email
        String userType = "customer"; // Set based on your logic (e.g., 'customer' or 'admin')

        // Basic validation for full name
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone number is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new User object
        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setUserType(userType); // Set user type

        // Initialize the cart and other fields
        user.setCart(new Cart());
        user.setOrders(new ArrayList<>()); // Initialize orders as an empty list
        user.setShippingAddress(null); // Set shipping address if available
        user.setWishlist(new ArrayList<>()); // Initialize wishlist
        user.setMessages(new ArrayList<>()); // Initialize messages
        user.setCartId("hello"); // Initialize cartId if needed
        user.setProfilePicture(""); // Initialize profile picture URL if available
        user.setPassword(password.getText().toString().trim()); // Set password, ensure it's hashed in production

        // Get a reference to the current user's data in the database
        DatabaseReference userRef = FirebaseUtil.currentUser();

        // Store the user data in the Firebase Database
        if (userRef != null) {
            userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterInputActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        // Optionally redirect to another activity
                        startActivity(new Intent(RegisterInputActivity.this, Dashboard.class)); // Replace with your next activity
                        finish(); // Close this activity
                    } else {
                        Toast.makeText(RegisterInputActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "User reference is null.", Toast.LENGTH_SHORT).show();
        }
    }





}
