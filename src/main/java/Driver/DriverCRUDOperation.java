/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

import DBConnection.ConnectionHelper;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class DriverCRUDOperation {
    
    /**
     * Add a new driver to the database
     * @param driver Driver object to add
     * @return The new driver ID or error code
     */
    public static int addDriver(Driver driver) {
        // Validate driver data
        if (!driver.isValidLicense()) {
            return -2; // Invalid license format
        }
        
        if (driver.isLicenseExpired()) {
            return -3; // License is expired
        }
        
        // Check if license number already exists
        if (isLicenseExists(driver.getLicense_number())) {
            return -4; // License already exists
        }
        
        // Check if user is already a driver
        if (isUserAlreadyDriver(driver.getUser_id())) {
            return -5; // User is already a driver
        }
        
        String query = "INSERT INTO drivers (user_id, license_number, license_expiry, experience_years, rating, is_available) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, driver.getUser_id());
            stmt.setString(2, driver.getLicense_number());
            stmt.setDate(3, Date.valueOf(driver.getLicense_expiry()));
            stmt.setInt(4, driver.getExperience_years());
            stmt.setDouble(5, driver.getRating());
            stmt.setBoolean(6, driver.isIs_available());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }

    /**
     * Get all drivers with user and car information
     * @return List of Driver objects
     */
    public static List<Driver> getDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT d.*, u.fullName, u.phone, u.uEmail, " +
                      "c.id as car_id, c.number_plate " +
                      "FROM drivers d " +
                      "JOIN users u ON d.user_id = u.id " +
                      "LEFT JOIN cars c ON c.driver_id = d.id";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                drivers.add(createDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
    
    /**
     * Get a driver by ID
     * @param id Driver ID
     * @return Driver object or null if not found
     */
    public static Driver getDriverById(int id) {
        String query = "SELECT d.*, u.fullName, u.phone, u.uEmail, " +
                      "c.id as car_id, c.number_plate " +
                      "FROM drivers d " +
                      "JOIN users u ON d.user_id = u.id " +
                      "LEFT JOIN cars c ON c.driver_id = d.id " +
                      "WHERE d.id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createDriverFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get a driver by user ID
     * @param userId User ID
     * @return Driver object or null if not found
     */
    public static Driver getDriverByUserId(int userId) {
        String query = "SELECT d.*, u.fullName, u.phone, u.uEmail, " +
                      "c.id as car_id, c.number_plate " +
                      "FROM drivers d " +
                      "JOIN users u ON d.user_id = u.id " +
                      "LEFT JOIN cars c ON c.driver_id = d.id " +
                      "WHERE d.user_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createDriverFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update an existing driver
     * @param driver Driver object with updated data
     * @return Number of rows affected or error code
     */
    public static int updateDriver(Driver driver) {
        // Validate driver data
        if (!driver.isValidLicense()) {
            return -2; // Invalid license format
        }
        
        if (driver.isLicenseExpired()) {
            return -3; // License is expired
        }
        
        // Check if license number already exists (excluding this driver)
        if (isLicenseExistsExcludingDriver(driver.getLicense_number(), driver.getId())) {
            return -4; // License already exists for another driver
        }
        
        String query = "UPDATE drivers SET license_number=?, license_expiry=?, " +
                       "experience_years=?, rating=?, is_available=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, driver.getLicense_number());
            stmt.setDate(2, Date.valueOf(driver.getLicense_expiry()));
            stmt.setInt(3, driver.getExperience_years());
            stmt.setDouble(4, driver.getRating());
            stmt.setBoolean(5, driver.isIs_available());
            stmt.setInt(6, driver.getId());
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Delete a driver
     * @param id Driver ID to delete
     * @return Number of rows affected or error code
     */
    public static int deleteDriver(int id) {
        // Check if driver has assigned cars
        if (hasAssignedCars(id)) {
            return -2; // Cannot delete driver with assigned cars
        }
        
        String query = "DELETE FROM drivers WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Update driver availability
     * @param id Driver ID
     * @param isAvailable New availability status
     * @return Number of rows affected
     */
    public static int updateDriverAvailability(int id, boolean isAvailable) {
        String query = "UPDATE drivers SET is_available=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, id);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Get available drivers (not assigned to cars)
     * @return List of available drivers
     */
    public static List<Driver> getAvailableDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT d.*, u.fullName, u.phone, u.uEmail, " +
                      "c.id as car_id, c.number_plate " +
                      "FROM drivers d " +
                      "JOIN users u ON d.user_id = u.id " +
                      "LEFT JOIN cars c ON c.driver_id = d.id " +
                      "WHERE d.is_available = true AND c.id IS NULL";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                drivers.add(createDriverFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
    
    /**
     * Helper method to create a Driver object from ResultSet
     * @param rs ResultSet with driver data
     * @return Driver object
     * @throws SQLException If a database error occurs
     */
    private static Driver createDriverFromResultSet(ResultSet rs) throws SQLException {
        LocalDate licenseExpiry = null;
        if (rs.getDate("license_expiry") != null) {
            licenseExpiry = rs.getDate("license_expiry").toLocalDate();
        }
        
        Integer carId = null;
        if (rs.getObject("car_id") != null) {
            carId = rs.getInt("car_id");
        }
        
        return new Driver(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("license_number"),
            licenseExpiry,
            rs.getInt("experience_years"),
            rs.getDouble("rating"),
            rs.getBoolean("is_available"),
            rs.getString("fullName"),
            rs.getString("phone"),
            rs.getString("uEmail"),
            carId,
            rs.getString("number_plate")
        );
    }
    
    /**
     * Check if a user is already registered as a driver
     * @param userId User ID to check
     * @return true if user is already a driver, false otherwise
     */
    private static boolean isUserAlreadyDriver(int userId) {
        String query = "SELECT COUNT(*) FROM drivers WHERE user_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if a license number already exists
     * @param licenseNumber License number to check
     * @return true if exists, false otherwise
     */
    private static boolean isLicenseExists(String licenseNumber) {
        String query = "SELECT COUNT(*) FROM drivers WHERE license_number = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, licenseNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if a license number exists excluding a specific driver
     * @param licenseNumber License number to check
     * @param driverId Driver ID to exclude
     * @return true if exists, false otherwise
     */
    private static boolean isLicenseExistsExcludingDriver(String licenseNumber, int driverId) {
        String query = "SELECT COUNT(*) FROM drivers WHERE license_number = ? AND id != ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, licenseNumber);
            stmt.setInt(2, driverId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if driver has any assigned cars
     * @param driverId Driver ID to check
     * @return true if has assigned cars, false otherwise
     */
    private static boolean hasAssignedCars(int driverId) {
        String query = "SELECT COUNT(*) FROM cars WHERE driver_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}