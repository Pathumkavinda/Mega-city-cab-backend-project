/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

/**
 *
 * @author Admin
 */
import DBConnection.ConnectionHelper;
import java.sql.*;
import java.util.ArrayList;

public class DriversOperations {

    public static int addDriver(Drivers driver) {
        String query = "INSERT INTO drivers (userId, dlNumber, stat) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, driver.getUserId());
            stmt.setString(2, driver.getDlNumber());
            stmt.setString(3, driver.getStat());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ArrayList<Drivers> getAllDrivers() {
        ArrayList<Drivers> driversList = new ArrayList<>();
        String query = "SELECT * FROM drivers";
        try (Connection conn = ConnectionHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                driversList.add(new Drivers(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("dlNumber"),
                        rs.getString("stat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driversList;
    }

    public static Drivers getDriverById(int driverId) {
        String query = "SELECT * FROM drivers WHERE id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Drivers(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("dlNumber"),
                        rs.getString("stat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateDriver(Drivers driver) {
        String query = "UPDATE drivers SET userId=?, dlNumber=?, stat=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driver.getUserId());
            stmt.setString(2, driver.getDlNumber());
            stmt.setString(3, driver.getStat());
            stmt.setInt(4, driver.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int updateStatus(int driverId, String status) {
    String query = "UPDATE drivers SET stat=? WHERE id=?";
    try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, status);
        stmt.setInt(2, driverId);
        return stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}
    public static int deleteDriver(int driverId) {
        String query = "DELETE FROM drivers WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;}}