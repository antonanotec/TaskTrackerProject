// DatabaseConnection.java
package com.tasktracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    // Percorso al file del database SQLite. Sarà creato nella root dell'applicazione o nella cartella di lavoro.
    // In un ambiente Tomcat, questo potrebbe essere nella cartella bin o nella root del server.
    // Per un'applicazione WAR, è spesso meglio gestirlo come risorsa o in un percorso specifico.
    // Per semplicità, lo mettiamo qui, ma considera un percorso più robusto per la produzione.
    private static final String DATABASE_URL = "jdbc:sqlite:tasktracker.db";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Errore: Driver JDBC SQLite non trovato.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione al database: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
