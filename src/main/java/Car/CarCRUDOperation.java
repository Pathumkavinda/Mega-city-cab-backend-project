/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Car;

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
public class CarCRUDOperation {
    
    /**
     * Add a new car to the database
     * @param car Car object to add
     * @return The new car ID or error code
     */
    public static int addCar(Car car) {
        // Validate car data
        if (!car.isValidNumberPlate()) {
            return -2; // Invalid number plate format
        }
        
        if (!car.isValidChassisNumber()) {
            return -3; // Invalid chassis number
        }
        
        // Check if number plate or chassis number already exists
        if (isNumberPlateExists(car.getNumber_plate())) {
            return -4; // Number plate already exists
        }
        
        if (isChassisNumberExists(car.getChassis_number())) {
            return -5; // Chassis number already exists
        }
        
        String query = "INSERT INTO cars (category_id, number_plate, chassis_number, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, car.getCategory_id());
            stmt.setString(2, car.getNumber_plate());
            stmt.setString(3, car.getChassis_number());
            stmt.setBoolean(4, car.isIs_active());

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
     * Get all cars from the database
     * @return List of Car objects
     */
    public static List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.*, cat.category_name FROM cars c " +
                      "LEFT JOIN categories cat ON c.category_id = cat.category_id";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("number_plate"),
                        rs.getString("chassis_number"),
                        rs.getBoolean("is_active")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    
    /**
     * Get a car by its ID
     * @param carId The ID of the car to retrieve
     * @return Car object or null if not found
     */
    public static Car getCarById(int carId) {
        String query = "SELECT c.*, cat.category_name FROM cars c " +
                      "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                      "WHERE c.id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Car(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("number_plate"),
                        rs.getString("chassis_number"),
                        rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing car in the database
     * @param car Car object with updated data
     * @return Number of rows affected or error code
     */
    public static int updateCar(Car car) {
        // Validate car data
        if (!car.isValidNumberPlate()) {
            return -2; // Invalid number plate format
        }
        
        if (!car.isValidChassisNumber()) {
            return -3; // Invalid chassis number
        }
        
        // Check if number plate or chassis number already exists (excluding this car)
        if (isNumberPlateExistsExcludingCar(car.getNumber_plate(), car.getId())) {
            return -4; // Number plate already exists
        }
        
        if (isChassisNumberExistsExcludingCar(car.getChassis_number(), car.getId())) {
            return -5; // Chassis number already exists
        }
        
        String query = "UPDATE cars SET category_id=?, number_plate=?, chassis_number=?, is_active=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, car.getCategory_id());
            stmt.setString(2, car.getNumber_plate());
            stmt.setString(3, car.getChassis_number());
            stmt.setBoolean(4, car.isIs_active());
            stmt.setInt(5, car.getId());
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Delete a car from the database
     * @param carId The ID of the car to delete
     * @return Number of rows affected
     */
    public static int deleteCar(int carId) {
        // Check if car is used in categories
        if (isCarUsedInCategories(carId)) {
            return -2; // Cannot delete a car that is used in categories
        }
        
        String query = "DELETE FROM cars WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, carId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Update car status (active/inactive)
     * @param carId The ID of the car to update
     * @param isActive New status value
     * @return Number of rows affected
     */
    public static int updateCarStatus(int carId, boolean isActive) {
        String query = "UPDATE cars SET is_active=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, carId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get cars by category ID
     * @param categoryId Category ID to filter by
     * @return List of cars in the specified category
     */
    public static List<Car> getCarsByCategoryId(int categoryId) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.*, cat.category_name FROM cars c " +
                      "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                      "WHERE c.category_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("number_plate"),
                        rs.getString("chassis_number"),
                        rs.getBoolean("is_active")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    
    /**
     * Get cars by category name (joining with categories table)
     * @param categoryName Category name to filter by
     * @return List of cars with the specified category name
     */
    public static List<Car> getCarsByCategoryName(String categoryName) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.*, cat.category_name FROM cars c " +
                      "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                      "WHERE cat.category_name = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("number_plate"),
                        rs.getString("chassis_number"),
                        rs.getBoolean("is_active")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    
    /**
     * Get cars by status
     * @param isActive Status to filter by
     * @return List of cars with the specified status
     */
    public static List<Car> getCarsByStatus(boolean isActive) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.*, cat.category_name FROM cars c " +
                      "LEFT JOIN categories cat ON c.category_id = cat.category_id " +
                      "WHERE c.is_active = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isActive);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("number_plate"),
                        rs.getString("chassis_number"),
                        rs.getBoolean("is_active")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    
    /**
     * Check if a number plate already exists
     * @param numberPlate Number plate to check
     * @return true if exists, false otherwise
     */
    private static boolean isNumberPlateExists(String numberPlate) {
        String query = "SELECT COUNT(*) FROM cars WHERE number_plate = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, numberPlate);
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
     * Check if a chassis number already exists
     * @param chassisNumber Chassis number to check
     * @return true if exists, false otherwise
     */
    private static boolean isChassisNumberExists(String chassisNumber) {
        String query = "SELECT COUNT(*) FROM cars WHERE chassis_number = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, chassisNumber);
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
     * Check if a number plate exists excluding a specific car
     * @param numberPlate Number plate to check
     * @param carId Car ID to exclude
     * @return true if exists, false otherwise
     */
    private static boolean isNumberPlateExistsExcludingCar(String numberPlate, int carId) {
        String query = "SELECT COUNT(*) FROM cars WHERE number_plate = ? AND id != ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, numberPlate);
            stmt.setInt(2, carId);
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
     * Check if a chassis number exists excluding a specific car
     * @param chassisNumber Chassis number to check
     * @param carId Car ID to exclude
     * @return true if exists, false otherwise
     */
    private static boolean isChassisNumberExistsExcludingCar(String chassisNumber, int carId) {
        String query = "SELECT COUNT(*) FROM cars WHERE chassis_number = ? AND id != ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, chassisNumber);
            stmt.setInt(2, carId);
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
     * Check if a car is used in any categories
     * @param carId Car ID to check
     * @return true if used, false otherwise
     */
    private static boolean isCarUsedInCategories(int carId) {
        String query = "SELECT COUNT(*) FROM categories WHERE car_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, carId);
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