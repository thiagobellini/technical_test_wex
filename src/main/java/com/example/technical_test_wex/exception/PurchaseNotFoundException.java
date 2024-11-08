package com.example.technical_test_wex.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(String message) {
        super("Purchase not found with ID: " + message);
    }
}
