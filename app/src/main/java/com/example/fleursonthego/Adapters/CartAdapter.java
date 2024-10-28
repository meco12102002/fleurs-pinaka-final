package com.example.fleursonthego.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleursonthego.Models.Product;
import com.example.fleursonthego.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> productList; // List to hold cart items
    private OnItemRemoveListener onItemRemoveListener; // Listener for item removal

    // Constructor
    public CartAdapter(List<Product> productList, OnItemRemoveListener onItemRemoveListener) {
        this.productList = productList;
        this.onItemRemoveListener = onItemRemoveListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product name and price
        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductPrice.setText(String.format("$%.2f", product.getPrice()));

        // Handle item removal
        holder.buttonRemove.setOnClickListener(v -> {
            onItemRemoveListener.onItemRemove(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class for the cart items
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        TextView textViewProductPrice;
        Button buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textProductName);
            textViewProductPrice = itemView.findViewById(R.id.textProductPrice);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    // Interface for item removal
    public interface OnItemRemoveListener {
        void onItemRemove(Product product);
    }
}
