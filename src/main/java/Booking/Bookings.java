package Booking;

import java.util.Date;

public class Bookings {

    private int id;
    private int userId;
    private int vehicleId;
    private int driverId;
    private String pickupLocation;
    private String destination;
    private Date startDate;
    private Date endDate;
    private String pickupTime; // Changed to String for TIME format
    private double finalPrice;
    private String stat;

    public Bookings(int id, int userId, int vehicleId, int driverId, String pickupLocation, String destination, 
                    Date startDate, Date endDate, String pickupTime, double finalPrice, String stat) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupTime = pickupTime;
        this.finalPrice = finalPrice;
        this.stat = stat;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public String getStat() { return stat; }
    public void setStat(String stat) { this.stat = stat; }
}
