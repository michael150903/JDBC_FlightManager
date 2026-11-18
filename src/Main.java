
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("MySQL Driver loaded!");
    } catch (ClassNotFoundException e) {
      System.out.println("MySQL Driver NOT found!");
    }
    String url = "jdbc:mysql://localhost:3306/";
    String user = "root";

    // Replace with your MySQL password
    String password = System.getenv("MYSQL_PASSWORD");

    try (Connection conn = DriverManager.getConnection(
            url, user, password)) {
      System.out.println("Connected to MySQL successfully!");
    } catch (SQLException e) {
      System.out.println("Connection failed: " + e.getMessage());
    }
  }
}
