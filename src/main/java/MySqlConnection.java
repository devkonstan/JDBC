import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private static String url = "jdbc:mysql://localhost:3306/customers?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static String login = "root";
    private static String password = "pass123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }
}