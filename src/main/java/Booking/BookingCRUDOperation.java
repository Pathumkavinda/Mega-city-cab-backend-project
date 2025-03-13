/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import DBConnection.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingCRUDOperation {

    public static int addBooking(Booking booking) {
        String query = "INSERT INTO bookings (user_id, package_id, car_id, driver_id, pickup_location, destination, " +
                       "pickup_date, pickup_time, price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getPackageId());
            stmt.setInt(3, booking.getCarId());
            stmt.setObject(4, booking.getDriverId(), Types.INTEGER);  // Nullable field
            stmt.setString(5, booking.getPickupLocation());
            stmt.setString(6, booking.getDestination());
            stmt.setDate(7, booking.getPickupDate());
            stmt.setTime(8, booking.getPickupTime());
            stmt.setDouble(9, booking.getPrice());
            stmt.setString(10, booking.getStatus());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // Return the generated booking ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
             
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getInt("package_id"),
                        rs.getInt("car_id"),
                        (rs.getObject("driver_id") != null ? rs.getInt("driver_id") : null), // Handle nullable driver_id
                        rs.getString("pickup_location"),
                        rs.getString("destination"),
                        rs.getDate("pickup_date"),
                        rs.getTime("pickup_time"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getInt("package_id"),
                        rs.getInt("car_id"),
                        (rs.getObject("driver_id") != null ? rs.getInt("driver_id") : null), // Handle nullable driver_id
                        rs.getString("pickup_location"),
                        rs.getString("destination"),
                        rs.getDate("pickup_date"),
                        rs.getTime("pickup_time"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int updateBooking(Booking booking) {
        String query = "UPDATE bookings SET user_id=?, package_id=?, car_id=?, driver_id=?, pickup_location=?, " +
                       "destination=?, pickup_date=?, pickup_time=?, price=?, status=? WHERE booking_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getPackageId());
            stmt.setInt(3, booking.getCarId());
            stmt.setObject(4, booking.getDriverId(), Types.INTEGER);  // Nullable field
            stmt.setString(5, booking.getPickupLocation());
            stmt.setString(6, booking.getDestination());
            stmt.setDate(7, booking.getPickupDate());
            stmt.setTime(8, booking.getPickupTime());
            stmt.setDouble(9, booking.getPrice());
            stmt.setString(10, booking.getStatus());
            stmt.setInt(11, booking.getBookingId());
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int deleteBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE booking_id=?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
