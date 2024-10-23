package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.R;
import com.example.fleursonthego.Adapters.ProductsAdapter; // Make sure to import your ProductsAdapter
import com.example.fleursonthego.Models.Product; // Import your Product model
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView recyclerView; // Declare RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Set the layout

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts); // Ensure this ID matches your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager for RecyclerView

        // Initialize button for customizing bouquet
        Button customizeBouquetButton = findViewById(R.id.btn_customize_bouquet);

        // Fetch products from the database
        fetchProducts();

        // Set an OnClickListener to navigate to CustomizeBouquetActivity
        customizeBouquetButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,BoquetCustomizationActivity.class);
            startActivity(intent);

        });
    }
    private void fetchProducts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class); // Make sure this matches your Product class structure
                    if (product != null) {
                        productList.add(product); // Add product to the list
                    }
                }
                // Update RecyclerView with fetched products
                updateRecyclerView(productList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateRecyclerView(List<Product> productList) {
        // Declare adapter for RecyclerView
        ProductsAdapter adapter = new ProductsAdapter(productList); // Initialize the adapter with the product list
        recyclerView.setAdapter(adapter); // Set the adapter for the RecyclerView
    }
}
