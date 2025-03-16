/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

/**
 *
 * @author Admin
 */
import DBConnection.ConnectionHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VehiclesOperations {

    public static int addVehicle(Vehicles vehicle) {
        String query = "INSERT INTO vehicles (catId, vehiNumber, stat) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, vehicle.getCatId());
            stmt.setString(2, vehicle.getVehiNumber());
            stmt.setString(3, vehicle.getStat());

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

    public static ArrayList<Vehicles> getAllVehicles() {
        ArrayList<Vehicles> vehiclesList = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try (Connection conn = ConnectionHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehiclesList.add(new Vehicles(
                        rs.getInt("id"),
                        rs.getInt("catId"),
                        rs.getString("vehiNumber"),
                        rs.getString("stat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiclesList;
    }

    public static Vehicles getVehicleById(int vehicleId) {
        String query = "SELECT * FROM vehicles WHERE id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicles(
                        rs.getInt("id"),
                        rs.getInt("catId"),
                        rs.getString("vehiNumber"),
                        rs.getString("stat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateVehicle(Vehicles vehicle) {
        String query = "UPDATE vehicles SET catId=?, vehiNumber=?, stat=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicle.getCatId());
            stmt.setString(2, vehicle.getVehiNumber());
            stmt.setString(3, vehicle.getStat());
            stmt.setInt(4, vehicle.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int updateStatus(int vehicleId, String status) {
        String query = "UPDATE vehicles SET stat=? WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, vehicleId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int deleteVehicle(int vehicleId) {
        String query = "DELETE FROM vehicles WHERE id=?";
        try (Connection conn = ConnectionHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
public static Vehicles getRandomVehicleByCategory(int catId) {
    String query = "SELECT * FROM vehicles WHERE catId = ? AND stat = 'Active'";
    List<Vehicles> vehicleList = new ArrayList<>();
    
    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, catId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            vehicleList.add(new Vehicles(
                rs.getInt("id"),
                rs.getInt("catId"),
                rs.getString("vehiNumber"),
                rs.getString("stat")
            ));
        }

        if (!vehicleList.isEmpty()) {
            // Pick a random vehicle
            Random random = new Random();
            return vehicleList.get(random.nextInt(vehicleList.size()));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return null; // Return null if no vehicle is found
}


}
