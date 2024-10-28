package com.example.fleursonthego.Models;

import java.util.List;

public class Cart {
    public List<CartItem> items;  // List of Cart Items

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public Cart() {

    }
}
