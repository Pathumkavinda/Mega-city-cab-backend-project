/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

import DBConnection.ConnectionHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Admin
 */
public class DriverCRUDOperation {

    public static int addDriver(Driver driver) {
        String query = "INSERT INTO drivers (user_id, car_id, license_number, active_status) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, driver.getUser_id());
            stmt.setInt(2, driver.getCar_id());
            stmt.setString(3, driver.getLicense_number());
            stmt.setBoolean(4, driver.isActive_status());

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

    public static List<Driver> getDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT * FROM drivers";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                drivers.add(new Driver(
                        rs.getInt("driver_id"),
                        rs.getInt("user_id"),
                        rs.getInt("car_id"),
                        rs.getString("license_number"),
                        rs.getBoolean("active_status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
    
    public static Driver getDriverById(int driver_id) {
        String query = "SELECT * FROM drivers WHERE driver_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driver_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"),
                        rs.getInt("user_id"),
                        rs.getInt("car_id"),
                        rs.getString("license_number"),
                        rs.getBoolean("active_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Driver getDriverByUserId(int user_id) {
        String query = "SELECT * FROM drivers WHERE user_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(
                        rs.getInt("driver_id"),
                        rs.getInt("user_id"),
                        rs.getInt("car_id"),
                        rs.getString("license_number"),
                        rs.getBoolean("active_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateDriver(Driver driver) {
        String query = "UPDATE drivers SET user_id=?, car_id=?, license_number=?, active_status=? WHERE driver_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driver.getUser_id());
            stmt.setInt(2, driver.getCar_id());
            stmt.setString(3, driver.getLicense_number());
            stmt.setBoolean(4, driver.isActive_status());
            stmt.setInt(5, driver.getDriver_id());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static int deleteDriver(int driver_id) {
        String query = "DELETE FROM drivers WHERE driver_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driver_id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static int updateDriverStatus(int driver_id, boolean active_status) {
        String query = "UPDATE drivers SET active_status=? WHERE driver_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, active_status);
            stmt.setInt(2, driver_id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}