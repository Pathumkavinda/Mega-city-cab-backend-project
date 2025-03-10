/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Category;

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
public class CategoryCRUDOperation {
    
    public static int addCategory(Category category) {
        // First validate the max passengers constraint
        if (!category.isValidMaxPassengers()) {
            return -2; // Invalid max passengers for the category
        }
        
        String query = "INSERT INTO categories (category_name, car_id, max_passengers, price_per_km, waiting_charge, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getCategory_name());
            
            // Handle NULL car_id
            if (category.getCar_id() != null) {
                stmt.setInt(2, category.getCar_id());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(3, category.getMax_passengers());
            stmt.setDouble(4, category.getPrice_per_km());
            stmt.setDouble(5, category.getWaiting_charge());
            stmt.setBoolean(6, category.isIs_available());

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

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.*, car.number_plate " +
                     "FROM categories c " +
                     "LEFT JOIN cars car ON c.car_id = car.id";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getObject("car_id") != null ? rs.getInt("car_id") : null,
                        rs.getString("number_plate"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("price_per_km"),
                        rs.getDouble("waiting_charge"),
                        rs.getBoolean("is_available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public static Category getCategoryById(int categoryId) {
        String query = "SELECT c.*, car.number_plate " +
                     "FROM categories c " +
                     "LEFT JOIN cars car ON c.car_id = car.id " +
                     "WHERE c.category_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getObject("car_id") != null ? rs.getInt("car_id") : null,
                        rs.getString("number_plate"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("price_per_km"),
                        rs.getDouble("waiting_charge"),
                        rs.getBoolean("is_available"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateCategory(Category category) {
        // First validate the max passengers constraint
        if (!category.isValidMaxPassengers()) {
            return -2; // Invalid max passengers for the category
        }
        
        String query = "UPDATE categories SET category_name=?, car_id=?, max_passengers=?, price_per_km=?, waiting_charge=?, is_available=? WHERE category_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, category.getCategory_name());
            
            // Handle NULL car_id
            if (category.getCar_id() != null) {
                stmt.setInt(2, category.getCar_id());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(3, category.getMax_passengers());
            stmt.setDouble(4, category.getPrice_per_km());
            stmt.setDouble(5, category.getWaiting_charge());
            stmt.setBoolean(6, category.isIs_available());
            stmt.setInt(7, category.getCategory_id());
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static int deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE category_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static List<Category> getCategoriesByAvailability(boolean isAvailable) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.*, car.number_plate " +
                     "FROM categories c " +
                     "LEFT JOIN cars car ON c.car_id = car.id " +
                     "WHERE c.is_available = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isAvailable);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getObject("car_id") != null ? rs.getInt("car_id") : null,
                        rs.getString("number_plate"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("price_per_km"),
                        rs.getDouble("waiting_charge"),
                        rs.getBoolean("is_available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public static List<Category> getCategoriesByCarId(int carId) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.*, car.number_plate " +
                     "FROM categories c " +
                     "LEFT JOIN cars car ON c.car_id = car.id " +
                     "WHERE c.car_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getObject("car_id") != null ? rs.getInt("car_id") : null,
                        rs.getString("number_plate"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("price_per_km"),
                        rs.getDouble("waiting_charge"),
                        rs.getBoolean("is_available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public static List<Category> getCategoriesByName(String categoryName) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT c.*, car.number_plate " +
                     "FROM categories c " +
                     "LEFT JOIN cars car ON c.car_id = car.id " +
                     "WHERE c.category_name = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getObject("car_id") != null ? rs.getInt("car_id") : null,
                        rs.getString("number_plate"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("price_per_km"),
                        rs.getDouble("waiting_charge"),
                        rs.getBoolean("is_available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Updates the car_id for a category
     * @param categoryId The ID of the category to update
     * @param carId The ID of the car to associate with the category
     * @return Number of rows affected
     */
    public static int updateCategoryCar(int categoryId, Integer carId) {
        String query = "UPDATE categories SET car_id=? WHERE category_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (carId != null) {
                stmt.setInt(1, carId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(2, categoryId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}