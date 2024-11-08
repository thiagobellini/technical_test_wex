package com.example.technical_test_wex.utils;

public class StringManipulation {

    private StringManipulation() {
        throw new IllegalStateException("Utility class");
    }

    public static String capitalizeWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] words = text.split(" ");
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalizedText.append(capitalizedWord).append(" ");
            }
        }

        return capitalizedText.toString().trim();
    }
}
