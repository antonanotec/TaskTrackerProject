// password_utils.java
package com.tasktracker.util;

import org.mindrot.jbcrypt.BCrypt; // Aggiungi questa dipendenza al tuo pom.xml o librerie

public class PasswordUtils {

    // Genera l'hash di una password usando BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verifica una password con il suo hash
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}