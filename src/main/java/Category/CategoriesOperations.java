/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Category;

/**
 *
 * @author Admin
 */
import DBConnection.ConnectionHelper;
import java.sql.*;
import java.util.ArrayList;

public class CategoriesOperations {

    public static int addCategory(Categories category) {
        String query = "INSERT INTO categories (catName, maxPassengers, perKm, perHr, perDayPrice, perDayKm, stat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getCatName());
            stmt.setInt(2, category.getMaxPassengers());
            stmt.setDouble(3, category.getPerKm());
            stmt.setDouble(4, category.getPerHr());
            stmt.setDouble(5, category.getPerDayPrice());
            stmt.setInt(6, category.getPerDayKm());
            stmt.setString(7, category.getStat());

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

    public static ArrayList<Categories> getAllCategories() {
        ArrayList<Categories> categoriesList = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Connection conn = ConnectionHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categoriesList.add(new Categories(
                        rs.getInt("id"),
                        rs.getString("catName"),
                        rs.getInt("maxPassengers"),
                        rs.getDouble("perKm"),
                        rs.getDouble("perHr"),
                        rs.getDouble("perDayPrice"),
                        rs.getInt("perDayKm"),
                        rs.getString("stat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoriesList;
    }

    public static Categories getCategoryById(int categoryId) {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categories(
                        rs.getInt("id"),
                        rs.getString("catName"),
                        rs.getInt("maxPassengers"),
                        rs.getDouble("perKm"),
                        rs.getDouble("perHr"),
                        rs.getDouble("perDayPrice"),
                        rs.getInt("perDayKm"),
                        rs.getString("stat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateCategory(Categories category) {
        String query = "UPDATE categories SET catName=?, maxPassengers=?, perKm=?, perHr=?, perDayPrice=?, perDayKm=?, stat=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getCatName());
            stmt.setInt(2, category.getMaxPassengers());
            stmt.setDouble(3, category.getPerKm());
            stmt.setDouble(4, category.getPerHr());
            stmt.setDouble(5, category.getPerDayPrice());
            stmt.setInt(6, category.getPerDayKm());
            stmt.setString(7, category.getStat());
            stmt.setInt(8, category.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int updateStatus(int categoryId, String status) {
        String query = "UPDATE categories SET stat=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, categoryId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int deleteCategory(int categoryId) {
        String query = "DELETE FROM categories WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
