package DatabaseConnection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/VendingMachineDB";
    private static final String user="root";
    private static final String password="1234";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }
}
