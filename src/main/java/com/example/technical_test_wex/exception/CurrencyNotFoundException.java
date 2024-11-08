package com.example.technical_test_wex.exception;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException() {
        super("The purchase cannot be converted to the target currency");
    }
}
