package Booking;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Booking {

    private int bookingId;
    private int userId;
    private int packageId;
    private int carId;
    private Integer driverId;  // Nullable field for driver_id (it can be null)
    private String pickupLocation;
    private String destination;
    private Date pickupDate;
    private Time pickupTime;
    private double price;
    private String status;  // Enum-like string for status
    private Timestamp createdAt;

    // Constructor
    public Booking(int bookingId, int userId, int packageId, int carId, Integer driverId, 
                   String pickupLocation, String destination, Date pickupDate, 
                   Time pickupTime, double price, String status, Timestamp createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.packageId = packageId;
        this.carId = carId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Default constructor
    public Booking() {}

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Time getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Time pickupTime) {
        this.pickupTime = pickupTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Method to represent booking as a string
    @Override
    public String toString() {
        return "Booking{" +
               "bookingId=" + bookingId +
               ", userId=" + userId +
               ", packageId=" + packageId +
               ", carId=" + carId +
               ", driverId=" + driverId +
               ", pickupLocation='" + pickupLocation + '\'' +
               ", destination='" + destination + '\'' +
               ", pickupDate=" + pickupDate +
               ", pickupTime=" + pickupTime +
               ", price=" + price +
               ", status='" + status + '\'' +
               ", createdAt=" + createdAt +
               '}';
    }
}
