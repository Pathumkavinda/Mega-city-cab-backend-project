/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Package;


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
public class PackageCRUDOperation {
    
    /**
     * Add a new package to the database
     * @param pkg Package object to add
     * @return The new package ID or -1 if failed
     */
    public static int addPackage(Package pkg) {
        // First validate the package data
        if (!pkg.isValid()) {
            return -2; // Invalid package data
        }
        
        String query = "INSERT INTO packages (package_name, package_type, category_name, " +
                       "base_price, included_kilometers, per_kilometer_charge, " +
                       "waiting_charge, description, is_active) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pkg.getPackage_name());
            stmt.setString(2, pkg.getPackage_type());
            stmt.setString(3, pkg.getCategory_name());
            stmt.setDouble(4, pkg.getBase_price());
            stmt.setInt(5, pkg.getIncluded_kilometers());
            stmt.setDouble(6, pkg.getPer_kilometer_charge());
            stmt.setDouble(7, pkg.getWaiting_charge());
            stmt.setString(8, pkg.getDescription());
            stmt.setBoolean(9, pkg.isIs_active());

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

    /**
     * Get all packages from the database
     * @return List of Package objects
     */
    public static List<Package> getPackages() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                packages.add(createPackageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    /**
     * Get a package by its ID
     * @param packageId The ID of the package to retrieve
     * @return Package object or null if not found
     */
    public static Package getPackageById(int packageId) {
        String query = "SELECT * FROM packages WHERE package_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createPackageFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update an existing package in the database
     * @param pkg Package object with updated data
     * @return Number of rows affected or error code
     */
    public static int updatePackage(Package pkg) {
        // First validate the package data
        if (!pkg.isValid()) {
            return -2; // Invalid package data
        }
        
        String query = "UPDATE packages SET package_name=?, package_type=?, category_name=?, " +
                       "base_price=?, included_kilometers=?, per_kilometer_charge=?, " +
                       "waiting_charge=?, description=?, is_active=? WHERE package_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, pkg.getPackage_name());
            stmt.setString(2, pkg.getPackage_type());
            stmt.setString(3, pkg.getCategory_name());
            stmt.setDouble(4, pkg.getBase_price());
            stmt.setInt(5, pkg.getIncluded_kilometers());
            stmt.setDouble(6, pkg.getPer_kilometer_charge());
            stmt.setDouble(7, pkg.getWaiting_charge());
            stmt.setString(8, pkg.getDescription());
            stmt.setBoolean(9, pkg.isIs_active());
            stmt.setInt(10, pkg.getPackage_id());
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Delete a package from the database
     * @param packageId The ID of the package to delete
     * @return Number of rows affected
     */
    public static int deletePackage(int packageId) {
        // Check if package is used in bookings
        if (isPackageUsedInBookings(packageId)) {
            return -2; // Cannot delete a package that is used in bookings
        }
        
        String query = "DELETE FROM packages WHERE package_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, packageId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // General error
    }
    
    /**
     * Update package status (active/inactive)
     * @param packageId The ID of the package to update
     * @param isActive New status value
     * @return Number of rows affected
     */
    public static int updatePackageStatus(int packageId, boolean isActive) {
        String query = "UPDATE packages SET is_active=? WHERE package_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, packageId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get packages by category
     * @param categoryName Category to filter by
     * @return List of packages in the specified category
     */
    public static List<Package> getPackagesByCategory(String categoryName) {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages WHERE category_name = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                packages.add(createPackageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    /**
     * Get packages by type
     * @param packageType Type to filter by (Day or Kilometer)
     * @return List of packages with the specified type
     */
    public static List<Package> getPackagesByType(String packageType) {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages WHERE package_type = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, packageType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                packages.add(createPackageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    /**
     * Get active packages
     * @return List of active packages
     */
    public static List<Package> getActivePackages() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages WHERE is_active = true";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                packages.add(createPackageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    /**
     * Get active packages by category
     * @param categoryName Category to filter by
     * @return List of active packages in the specified category
     */
    public static List<Package> getActivePackagesByCategory(String categoryName) {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages WHERE category_name = ? AND is_active = true";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                packages.add(createPackageFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }
    
    /**
     * Helper method to create a Package object from ResultSet
     * @param rs ResultSet with package data
     * @return Package object
     * @throws SQLException If a database error occurs
     */
    private static Package createPackageFromResultSet(ResultSet rs) throws SQLException {
        return new Package(
            rs.getInt("package_id"),
            rs.getString("package_name"),
            rs.getString("package_type"),
            rs.getString("category_name"),
            rs.getDouble("base_price"),
            rs.getInt("included_kilometers"),
            rs.getDouble("per_kilometer_charge"),
            rs.getDouble("waiting_charge"),
            rs.getString("description"),
            rs.getBoolean("is_active")
        );
    }
    
    /**
     * Check if a package is used in any bookings
     * @param packageId Package ID to check
     * @return true if used, false otherwise
     */
    private static boolean isPackageUsedInBookings(int packageId) {
        String query = "SELECT COUNT(*) FROM bookings WHERE package_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, packageId);
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