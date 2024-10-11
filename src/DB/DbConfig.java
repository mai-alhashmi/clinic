package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    public static Connection createdConnection()throws SQLException {
        String dbURL = "jdbc:mysql://localhost:3306/clinic_db";
         return DriverManager.getConnection(dbURL, "root", "Mai-91");

    }
}
