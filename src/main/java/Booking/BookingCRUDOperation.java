/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import DBConnection.ConnectionHelper;
import Package.Package;
import Package.PackageCRUDOperation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access operations for the Bookings table
 * @author Admin
 */
public class BookingCRUDOperation {
    
    /**
     * Create a new booking in the database
     * @param booking Booking object to add
     * @return The new booking ID or -1 if failed
     */
    public static int createBooking(Booking booking) {
        // Get package information to set the base fare
        Package pkg = PackageCRUDOperation.getPackageById(booking.getPackage_id());
        if (pkg == null) {
            return -2; // Invalid package ID
        }
        
        // Set base fare from package
        booking.setBase_fare(pkg.getBase_price());
        
        // Calculate initial total fare (just base fare for now)
        booking.setTotal_fare(pkg.getBase_price());
        
        String query = "INSERT INTO bookings (user_id, package_id, car_id, pickup_location, " +
                       "destination, pickup_datetime, num_passengers, notes, status, " +
                       "base_fare, total_fare, payment_status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUser_id());
            stmt.setInt(2, booking.getPackage_id());
            stmt.setInt(3, booking.getCar_id());
            stmt.setString(4, booking.getPickup_location());
            stmt.setString(5, booking.getDestination());
            stmt.setTimestamp(6, Timestamp.valueOf(booking.getPickup_datetime()));
            stmt.setInt(7, booking.getNum_passengers());
            stmt.setString(8, booking.getNotes());
            stmt.setString(9, booking.getStatus());
            stmt.setDouble(10, booking.getBase_fare());
            stmt.setDouble(11, booking.getTotal_fare());
            stmt.setString(12, booking.getPayment_status());

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
     * Get all bookings from the database with join info
     * @return List of Booking objects
     */
    public static List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.fullName as user_name, p.package_name, p.package_type, " +
                       "p.category_name, c.number_plate as car_number_plate, " +
                       "d.fullName as driver_name " +
                       "FROM bookings b " +
                       "JOIN users u ON b.user_id = u.id " +
                       "JOIN packages p ON b.package_id = p.package_id " +
                       "JOIN cars c ON b.car_id = c.id " +
                       "LEFT JOIN drivers dr ON b.driver_id = dr.id " +
                       "LEFT JOIN users d ON dr.user_id = d.id " +
                       "ORDER BY b.pickup_datetime DESC";
        try (Connection conn = ConnectionHelper.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                bookings.add(createBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get bookings for a specific user
     * @param userId User ID
     * @return List of Booking objects
     */
    public static List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.fullName as user_name, p.package_name, p.package_type, " +
                       "p.category_name, c.number_plate as car_number_plate, " +
                       "d.fullName as driver_name " +
                       "FROM bookings b " +
                       "JOIN users u ON b.user_id = u.id " +
                       "JOIN packages p ON b.package_id = p.package_id " +
                       "JOIN cars c ON b.car_id = c.id " +
                       "LEFT JOIN drivers dr ON b.driver_id = dr.id " +
                       "LEFT JOIN users d ON dr.user_id = d.id " +
                       "WHERE b.user_id = ? " +
                       "ORDER BY b.pickup_datetime DESC";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(createBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get bookings assigned to a specific driver
     * @param driverId Driver ID
     * @return List of Booking objects
     */
    public static List<Booking> getBookingsByDriverId(int driverId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.fullName as user_name, p.package_name, p.package_type, " +
                       "p.category_name, c.number_plate as car_number_plate, " +
                       "d.fullName as driver_name " +
                       "FROM bookings b " +
                       "JOIN users u ON b.user_id = u.id " +
                       "JOIN packages p ON b.package_id = p.package_id " +
                       "JOIN cars c ON b.car_id = c.id " +
                       "LEFT JOIN drivers dr ON b.driver_id = dr.id " +
                       "LEFT JOIN users d ON dr.user_id = d.id " +
                       "WHERE b.driver_id = ? " +
                       "ORDER BY b.pickup_datetime DESC";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(createBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get a booking by its ID
     * @param bookingId Booking ID
     * @return Booking object or null if not found
     */
    public static Booking getBookingById(int bookingId) {
        String query = "SELECT b.*, u.fullName as user_name, p.package_name, p.package_type, " +
                       "p.category_name, c.number_plate as car_number_plate, " +
                       "d.fullName as driver_name " +
                       "FROM bookings b " +
                       "JOIN users u ON b.user_id = u.id " +
                       "JOIN packages p ON b.package_id = p.package_id " +
                       "JOIN cars c ON b.car_id = c.id " +
                       "LEFT JOIN drivers dr ON b.driver_id = dr.id " +
                       "LEFT JOIN users d ON dr.user_id = d.id " +
                       "WHERE b.booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update booking status
     * @param bookingId Booking ID
     * @param status New status
     * @return Number of rows affected
     */
    public static int updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Update booking payment status
     * @param bookingId Booking ID
     * @param paymentStatus New payment status
     * @param paymentMethod Payment method used
     * @return Number of rows affected
     */
    public static int updatePaymentStatus(int bookingId, String paymentStatus, String paymentMethod) {
        String query = "UPDATE bookings SET payment_status = ?, payment_method = ? WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, paymentStatus);
            stmt.setString(2, paymentMethod);
            stmt.setInt(3, bookingId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Assign a driver to a booking
     * @param bookingId Booking ID
     * @param driverId Driver ID
     * @return Number of rows affected
     */
    public static int assignDriver(int bookingId, int driverId) {
        String query = "UPDATE bookings SET driver_id = ?, status = 'Confirmed' WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, driverId);
            stmt.setInt(2, bookingId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Update booking completion details and calculate final fare
     * @param bookingId Booking ID
     * @param actualPickupDatetime Actual pickup date and time
     * @param completionDatetime Completion date and time
     * @param distanceTraveled Distance traveled in km
     * @param waitingTimeMinutes Waiting time in minutes
     * @return Number of rows affected
     */
    public static int completeBooking(int bookingId, LocalDateTime actualPickupDatetime, 
                                     LocalDateTime completionDatetime, double distanceTraveled, 
                                     int waitingTimeMinutes) {
        // First get the booking and package details
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            return -2; // Booking not found
        }
        
        Package pkg = PackageCRUDOperation.getPackageById(booking.getPackage_id());
        if (pkg == null) {
            return -3; // Package not found
        }
        
        // Calculate fare components
        double extraDistanceCharge = 0.0;
        double waitingCharge = 0.0;
        
        // Calculate extra distance charge for Day packages
        if (pkg.getPackage_type().equals("Day") && distanceTraveled > pkg.getIncluded_kilometers()) {
            extraDistanceCharge = (distanceTraveled - pkg.getIncluded_kilometers()) * pkg.getPer_kilometer_charge();
        } 
        // Calculate distance charge for Kilometer packages
        else if (pkg.getPackage_type().equals("Kilometer")) {
            booking.setBase_fare(distanceTraveled * pkg.getPer_kilometer_charge());
        }
        
        // Calculate waiting charge
        if (waitingTimeMinutes > 0) {
            // Convert minutes to hours and apply rate
            waitingCharge = (waitingTimeMinutes / 60.0) * pkg.getWaiting_charge();
        }
        
        // Calculate total fare
        double totalFare = booking.getBase_fare() + extraDistanceCharge + waitingCharge;
        
        // Update the booking
        String query = "UPDATE bookings SET actual_pickup_datetime = ?, completion_datetime = ?, " +
                      "distance_traveled = ?, waiting_time_minutes = ?, extra_distance_charge = ?, " +
                      "waiting_charge = ?, total_fare = ?, status = 'Completed' " +
                      "WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, actualPickupDatetime != null ? Timestamp.valueOf(actualPickupDatetime) : null);
            stmt.setTimestamp(2, Timestamp.valueOf(completionDatetime));
            stmt.setDouble(3, distanceTraveled);
            stmt.setInt(4, waitingTimeMinutes);
            stmt.setDouble(5, extraDistanceCharge);
            stmt.setDouble(6, waitingCharge);
            stmt.setDouble(7, totalFare);
            stmt.setInt(8, bookingId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Cancel a booking
     * @param bookingId Booking ID
     * @return Number of rows affected
     */
    public static int cancelBooking(int bookingId) {
        String query = "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, bookingId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get bookings by status
     * @param status Booking status
     * @return List of Booking objects
     */
    public static List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, u.fullName as user_name, p.package_name, p.package_type, " +
                       "p.category_name, c.number_plate as car_number_plate, " +
                       "d.fullName as driver_name " +
                       "FROM bookings b " +
                       "JOIN users u ON b.user_id = u.id " +
                       "JOIN packages p ON b.package_id = p.package_id " +
                       "JOIN cars c ON b.car_id = c.id " +
                       "LEFT JOIN drivers dr ON b.driver_id = dr.id " +
                       "LEFT JOIN users d ON dr.user_id = d.id " +
                       "WHERE b.status = ? " +
                       "ORDER BY b.pickup_datetime ASC";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(createBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    /**
     * Get revenue statistics for a given time period
     * @param startDate Start date
     * @param endDate End date
     * @return Object containing revenue statistics
     */
    public static Object getRevenueStats(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT " +
                      "COUNT(*) as total_bookings, " +
                      "SUM(total_fare) as total_revenue, " +
                      "AVG(total_fare) as average_fare, " +
                      "COUNT(CASE WHEN status = 'Completed' THEN 1 END) as completed_bookings, " +
                      "COUNT(CASE WHEN status = 'Cancelled' THEN 1 END) as cancelled_bookings, " +
                      "COUNT(CASE WHEN payment_status = 'Paid' THEN 1 END) as paid_bookings " +
                      "FROM bookings " +
                      "WHERE pickup_datetime BETWEEN ? AND ?";
        try (Connection conn = ConnectionHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Return as a simple object with data
                return new Object() {
                    public final int totalBookings = rs.getInt("total_bookings");
                    public final double totalRevenue = rs.getDouble("total_revenue");
                    public final double averageFare = rs.getDouble("average_fare");
                    public final int completedBookings = rs.getInt("completed_bookings");
                    public final int cancelledBookings = rs.getInt("cancelled_bookings");
                    public final int paidBookings = rs.getInt("paid_bookings");
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create a Booking object from a ResultSet
     * @param rs ResultSet from database query
     * @return Booking object
     * @throws SQLException If an SQL error occurs
     */
    private static Booking createBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        
        booking.setBooking_id(rs.getInt("booking_id"));
        booking.setUser_id(rs.getInt("user_id"));
        booking.setPackage_id(rs.getInt("package_id"));
        booking.setCar_id(rs.getInt("car_id"));
        
        // Handle nullable driver_id
        int driverId = rs.getInt("driver_id");
        booking.setDriver_id(rs.wasNull() ? null : driverId);
        
        booking.setPickup_location(rs.getString("pickup_location"));
        booking.setDestination(rs.getString("destination"));
        
        // Handle date/time fields
        Timestamp pickupTimestamp = rs.getTimestamp("pickup_datetime");
        if (pickupTimestamp != null) {
            booking.setPickup_datetime(pickupTimestamp.toLocalDateTime());
        }
        
        Timestamp actualPickupTimestamp = rs.getTimestamp("actual_pickup_datetime");
        if (actualPickupTimestamp != null) {
            booking.setActual_pickup_datetime(actualPickupTimestamp.toLocalDateTime());
        }
        
        Timestamp completionTimestamp = rs.getTimestamp("completion_datetime");
        if (completionTimestamp != null) {
            booking.setCompletion_datetime(completionTimestamp.toLocalDateTime());
        }
        
        booking.setDistance_traveled(rs.getDouble("distance_traveled"));
        booking.setWaiting_time_minutes(rs.getInt("waiting_time_minutes"));
        booking.setBase_fare(rs.getDouble("base_fare"));
        booking.setExtra_distance_charge(rs.getDouble("extra_distance_charge"));
        booking.setWaiting_charge(rs.getDouble("waiting_charge"));
        booking.setTotal_fare(rs.getDouble("total_fare"));
        booking.setNum_passengers(rs.getInt("num_passengers"));
        booking.setNotes(rs.getString("notes"));
        booking.setStatus(rs.getString("status"));
        booking.setPayment_status(rs.getString("payment_status"));
        booking.setPayment_method(rs.getString("payment_method"));
        booking.setCreated_at(rs.getTimestamp("created_at"));
        booking.setUpdated_at(rs.getTimestamp("updated_at"));
        
        // Set joined fields
        try {
            booking.setUser_name(rs.getString("user_name"));
            booking.setPackage_name(rs.getString("package_name"));
            booking.setPackage_type(rs.getString("package_type"));
            booking.setCategory_name(rs.getString("category_name"));
            booking.setCar_number_plate(rs.getString("car_number_plate"));
            booking.setDriver_name(rs.getString("driver_name"));
        } catch (SQLException e) {
            // These fields might not be available in all queries
            // Silently ignore the error
        }
        
        return booking;
    }
}