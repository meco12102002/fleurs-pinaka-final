package com.example.fleursonthego.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.R;
import com.example.fleursonthego.Adapters.ProductsAdapter;
import com.example.fleursonthego.Models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView viewAll;
    private ImageButton customizeBouquetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard); // Set the layout

        initializeViews();
        setupRecyclerView();
        setupListeners();
        fetchProducts();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        viewAll = findViewById(R.id.textViewViewAll);
        customizeBouquetButton = findViewById(R.id.icon_customize_order);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        viewAll.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, AllProductsActivity.class);
            startActivity(intent);
        });

        customizeBouquetButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, BouquetCustomizationActivity.class);
            startActivity(intent);
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(Dashboard.this, Dashboard.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_messages) {
                    startActivity(new Intent(Dashboard.this, MessagesActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    startActivity(new Intent(Dashboard.this, CartActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_account) {
                    startActivity(new Intent(Dashboard.this, AccountActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void fetchProducts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        Log.d("ProductsAdapter", "Binding Product: " + product.getProductName());
                        productList.add(product);
                    }
                }
                updateRecyclerView(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateRecyclerView(List<Product> productList) {
        ProductsAdapter adapter = new ProductsAdapter(productList);
        recyclerView.setAdapter(adapter);
    }
}
