import java.sql.*;
import java.util.*;

public class AdvancedFlightDispatcher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter database password: ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/FlightDB?allowLoadLocalInfile=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jerusalem&useSSL=false",
                "root", password)) {

            List<Integer> arrivalIds = Arrays.asList(1, 3, 5);
            String newStatus = "Landed - Final";
            updateMultipleDelays(conn, arrivalIds, newStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMultipleDelays(Connection conn, List<Integer> arrivalIds, String newStatus) throws SQLException {
        String sql = "UPDATE Arrivals SET delay_status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);

            for (Integer id : arrivalIds) {
                pstmt.setInt(2, id);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            int totalAffectedRows = 0;
            for (int result : results) {
                totalAffectedRows += result;
            }

            System.out.println(totalAffectedRows + " rows have been updated.");
        } catch (SQLException e) {
            throw new SQLException("Error updating delay_status", e);
        }
    }
}
