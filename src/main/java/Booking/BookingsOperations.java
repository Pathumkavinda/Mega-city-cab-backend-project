package Booking;

import DBConnection.ConnectionHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingsOperations {

    // ✅ Add a new booking
    public static int addBooking(Bookings booking) {
        String query = "INSERT INTO bookings (userId, startDate, endDate, vehicleId, driverId, pickupLocation, destination, pickupTime, finalPrice, stat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getUserId());
            stmt.setDate(2, new java.sql.Date(booking.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(booking.getEndDate().getTime()));
            stmt.setInt(4, booking.getVehicleId());
            stmt.setInt(5, booking.getDriverId());
            stmt.setString(6, booking.getPickupLocation());
            stmt.setString(7, booking.getDestination());
            stmt.setString(8, booking.getPickupTime());
            stmt.setDouble(9, booking.getFinalPrice());
            stmt.setString(10, booking.getStat());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return generated booking ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to insert
    }

    // ✅ Retrieve a single booking by ID
public static Bookings getBookingById(int bookingId) {
    String query = "SELECT * FROM bookings WHERE id = ?";
    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, bookingId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Bookings(
                rs.getInt("id"),
                rs.getInt("userId"),
                rs.getInt("vehicleId"),
                rs.getInt("driverId"),
                rs.getString("pickupLocation"),
                rs.getString("destination"),
                rs.getDate("startDate"),
                rs.getDate("endDate"),
                rs.getString("pickupTime"),
                rs.getDouble("finalPrice"),
                rs.getString("stat")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // No booking found
}


    // ✅ Retrieve all bookings
    public static List<Bookings> getAllBookings() {
        List<Bookings> bookingsList = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        
        try (Connection conn = ConnectionHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                bookingsList.add(new Bookings(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getInt("vehicleId"),
                    rs.getInt("driverId"),
                    rs.getString("pickupLocation"),
                    rs.getString("destination"),
                    rs.getDate("startDate"),
                    rs.getDate("endDate"),
                    rs.getString("pickupTime"),
                    rs.getDouble("finalPrice"),
                    rs.getString("stat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }

    // ✅ Retrieve bookings by user ID
    public static List<Bookings> getBookingsByUserId(int userId) {
        List<Bookings> bookingsList = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE userId = ?";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingsList.add(new Bookings(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getInt("vehicleId"),
                    rs.getInt("driverId"),
                    rs.getString("pickupLocation"),
                    rs.getString("destination"),
                    rs.getDate("startDate"),
                    rs.getDate("endDate"),
                    rs.getString("pickupTime"),
                    rs.getDouble("finalPrice"),
                    rs.getString("stat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingsList;
    }

    // ✅ Update a booking (all fields)
    public static int updateBooking(Bookings booking) {
        String query = "UPDATE bookings SET userId=?, startDate=?, endDate=?, vehicleId=?, driverId=?, pickupLocation=?, destination=?, pickupTime=?, finalPrice=?, stat=? WHERE id=?";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, booking.getUserId());
            stmt.setDate(2, new java.sql.Date(booking.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(booking.getEndDate().getTime()));
            stmt.setInt(4, booking.getVehicleId());
            stmt.setInt(5, booking.getDriverId());
            stmt.setString(6, booking.getPickupLocation());
            stmt.setString(7, booking.getDestination());
            stmt.setString(8, booking.getPickupTime());  
            stmt.setDouble(9, booking.getFinalPrice());
            stmt.setString(10, booking.getStat());
            stmt.setInt(11, booking.getId());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to update
    }

    // ✅ Update only the status of a booking
    public static int updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET stat=? WHERE id=?";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, bookingId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to update status
    }

    // ✅ Delete a booking
    public static int deleteBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE id=?";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Failed to delete
    }
}
