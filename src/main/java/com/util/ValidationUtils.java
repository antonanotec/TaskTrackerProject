// ValidationUtils.java
package com.tasktracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");
    // Per la password, il requisito originale era "almeno 6 caratteri".
    // Ho mantenuto una validazione semplice per la lunghezza.
    // Se vuoi una password più robusta (maiuscole, minuscole, numeri, simboli), usa un regex più complesso.
    // Esempio per password robusta: private static final Pattern PASSWORD_STRONG_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6; // Almeno 6 caratteri
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidTaskTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.trim().length() <= 100;
    }

    public static boolean isValidDueDate(String dueDateStr) {
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            return true; // La data di scadenza è opzionale
        }
        try {
            LocalDate.parse(dueDateStr); // Valida il formato YYYY-MM-DD
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
