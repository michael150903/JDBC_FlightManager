import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * Updates the delay status of a specific flight in FlightDB.
 *
 * The original implementation used a standard Statement and
 * concatenated user input directly into an SQL query.
 * This made the application vulnerable to SQL Injection,
 * allowing malicious input to modify the SQL command.
 *
 * The refactored implementation uses PreparedStatement with
 * parameterized queries. User input is passed as data using
 * setString(), preventing it from being interpreted as
 * executable SQL syntax.
 */

public class SimpleFlightUpdater {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false";
        String user = "root";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter MySQL root password:");
        String password = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Type a new delay status for flight 'LX354':");
            String userInput = scanner.nextLine();

            // Refactored code using PreparedStatement with parameterized queries
            String sql = "UPDATE Arrivals SET delay_status = ? WHERE flight_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userInput);
                preparedStatement.setString(2, "LX354");
                int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) affected.");
        }
        } catch (Exception e) {
            e.printStackTrace();
    }
}
}
