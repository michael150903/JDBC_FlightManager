
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Displays airport arrivals for a selected terminal.
 * Retrieves flight information from the FlightDB database
 * using a secure PreparedStatement to prevent SQL Injection.
 */
public class FlightViewer {

    /**
     * Connects to the database, asks for a terminal number,
     * and displays the matching arrivals in scheduled order.
     *
     * PreparedStatement is used to safely bind user input
     * instead of concatenating it into an SQL query.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/FlightDB?"
                + "allowLoadLocalInfile=true&"
                + "allowPublicKeyRetrieval=true&"
                + "serverTimezone=Asia/Jerusalem&useSSL=false";

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter MySQL root password: ");
            String password = scanner.nextLine();

            try (Connection connection =
                         DriverManager.getConnection(
                                 url, "root", password)) {

                // Validate terminal number
                String terminal;

                while (true) {
                    System.out.print(
                            "Enter terminal number (2, 3, 4, or 5): "
                    );

                    terminal = scanner.nextLine();

                    if (terminal.equals("2") ||
                            terminal.equals("3") ||
                            terminal.equals("4") ||
                            terminal.equals("5")) {
                        break;
                    }

                    System.out.println(
                            "Invalid terminal number. Please try again."
                    );
                }

                // Secure INNER JOIN query
                String sql =
                        "SELECT a.scheduled, a.flight_number, " +
                                "fr.from_city, a.delay_status, a.terminal " +
                                "FROM Arrivals a " +
                                "INNER JOIN FlightRoutes fr " +
                                "ON a.flight_number = fr.flight_number " +
                                "WHERE a.terminal = ? " +
                                "ORDER BY a.scheduled ASC";

                try (PreparedStatement pstmt =
                             connection.prepareStatement(sql)) {

                    pstmt.setString(1, terminal);

                    try (ResultSet rs = pstmt.executeQuery()) {

                        boolean found = false;

                        System.out.printf(
                                "%-12s %-15s %-22s %-20s %-10s%n",
                                "Scheduled",
                                "Flight",
                                "Departure City",
                                "Delay Status",
                                "Terminal"
                        );

                        System.out.println(
                                "------------------------------------------------" +
                                        "--------------------------------"
                        );

                        while (rs.next()) {
                            found = true;

                            String scheduled =
                                    rs.getString("scheduled");

                            String flightNumber =
                                    rs.getString("flight_number");

                            String departureCity =
                                    rs.getString("from_city");

                            String delayStatus =
                                    rs.getString("delay_status");

                            String terminalNum =
                                    rs.getString("terminal");

                            System.out.printf(
                                    "%-12s %-15s %-22s %-20s %-10s%n",
                                    scheduled,
                                    flightNumber,
                                    departureCity,
                                    delayStatus,
                                    terminalNum
                            );
                        }

                        if (!found) {
                            System.out.println(
                                    "No arrivals found for terminal "
                                            + terminal
                            );
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println(
                        "Database error: " + e.getMessage()
                );
            }
        }
    }
}
