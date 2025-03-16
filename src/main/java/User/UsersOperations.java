package User;

import DBConnection.ConnectionHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsersOperations {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Add user
    public static int addUser(Users user) {
        String query = "INSERT INTO users (username, pWord, uRole, fullName, uEmail, nic, address, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, encoder.encode(user.getpWord())); // Encrypting the password
            stmt.setString(3, user.getuRole());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getUEmail());
            stmt.setString(6, user.getNic());
            stmt.setString(7, user.getAddress());
            stmt.setString(8, user.getPhone());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Returning the generated user ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Get all users
    public static List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = ConnectionHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new Users(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("pWord"),
                        rs.getString("uRole"),
                        rs.getString("fullName"),
                        rs.getString("uEmail"),
                        rs.getString("nic"),
                        rs.getString("address"),
                        rs.getString("phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Get user by ID
    public static Users getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("pWord"),
                        rs.getString("uRole"),
                        rs.getString("fullName"),
                        rs.getString("uEmail"),
                        rs.getString("nic"),
                        rs.getString("address"),
                        rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateUser(Users user) {
    String query = "UPDATE users SET username=?, uRole=?, fullName=?, uEmail=?, nic=?, address=?, phone=? WHERE id=?";
    String updatePasswordQuery = "UPDATE users SET pWord=? WHERE id=?";
    
    try (Connection conn = ConnectionHelper.getConnection()) {
        
        // ✅ Check if a new password is provided
        if (user.getpWord() != null && !user.getpWord().isEmpty()) {
            try (PreparedStatement stmt = conn.prepareStatement(updatePasswordQuery)) {
                stmt.setString(1, encoder.encode(user.getpWord())); // Encrypt the new password
                stmt.setInt(2, user.getId());
                stmt.executeUpdate();
            }
        }

        // ✅ Update other user details
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getuRole());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getUEmail());
            stmt.setString(5, user.getNic());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getPhone());
            stmt.setInt(8, user.getId());
            return stmt.executeUpdate();
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}


// ✅ Delete user by ID
    public static boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";  // Database will handle cascading deletes
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();  // Execute delete

            return rowsAffected > 0;  // Return true if user was deleted successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if deletion failed
    }

    // Validate user
    public static Users isValidUser(String uEmail, String password) {
        String query = "SELECT id, pWord, uRole, uEmail FROM users WHERE uEmail = ?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("pWord");
                if (encoder.matches(password, storedHashedPassword)) {
                    return new Users(rs.getInt("id"), "", "", rs.getString("uRole"), "", rs.getString("uEmail"), "", "", "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int updateUserRole(int userId, String newRole) {
    String query = "UPDATE users SET uRole = ? WHERE id = ?";
    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, newRole);
        stmt.setInt(2, userId);
        return stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1;
}
    
    public static boolean isEmailUsedByOtherUser(int userId, String email) {
    String query = "SELECT COUNT(*) FROM users WHERE uEmail = ? AND id != ?";
    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, email);
        stmt.setInt(2, userId); // Exclude the current user's ID
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public static boolean isEmailExists(String email) {
    String query = "SELECT COUNT(*) FROM users WHERE uEmail = ?";
    try (Connection conn = ConnectionHelper.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
// ✅ Update Password in Database
public static boolean updatePassword(int userId, String currentPassword, String newPassword) {
    String query = "SELECT pWord FROM users WHERE id = ?";
    String updateQuery = "UPDATE users SET pWord = ? WHERE id = ?";

    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedHashedPassword = rs.getString("pWord");

            // ✅ Check if entered current password matches the stored hashed password
            if (!encoder.matches(currentPassword, storedHashedPassword)) {
                System.out.println("❌ Current password does not match!");
                return false;
            }

            // ✅ Hash the new password before updating
            String hashedNewPassword = encoder.encode(newPassword);

            // ✅ Execute update query
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, hashedNewPassword);
                updateStmt.setInt(2, userId);
                int rowsUpdated = updateStmt.executeUpdate();
                return rowsUpdated > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


}


