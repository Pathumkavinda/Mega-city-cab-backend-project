/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Booking model class for Mega City Cab services
 * @author Admin
 */
public class Booking {
    private int booking_id;
    private int user_id;
    private int package_id;
    private int car_id;
    private Integer driver_id; // Nullable
    private String pickup_location;
    private String destination;
    private LocalDateTime pickup_datetime;
    private LocalDateTime actual_pickup_datetime;
    private LocalDateTime completion_datetime;
    private double distance_traveled;
    private int waiting_time_minutes;
    private double base_fare;
    private double extra_distance_charge;
    private double waiting_charge;
    private double total_fare;
    private int num_passengers;
    private String notes;
    private String status;
    private String payment_status;
    private String payment_method;
    private Timestamp created_at;
    private Timestamp updated_at;
    
    // Additional fields for joins
    private String user_name;
    private String package_name;
    private String package_type;
    private String category_name;
    private String car_number_plate;
    private String driver_name;
    
    // Constructor with essential fields
    public Booking(int user_id, int package_id, int car_id, String pickup_location, 
                  String destination, LocalDateTime pickup_datetime, 
                  int num_passengers, String notes, String status) {
        this.user_id = user_id;
        this.package_id = package_id;
        this.car_id = car_id;
        this.pickup_location = pickup_location;
        this.destination = destination;
        this.pickup_datetime = pickup_datetime;
        this.num_passengers = num_passengers;
        this.notes = notes;
        this.status = status;
        
        // Default values
        this.distance_traveled = 0.0;
        this.waiting_time_minutes = 0;
        this.base_fare = 0.0;
        this.extra_distance_charge = 0.0;
        this.waiting_charge = 0.0;
        this.total_fare = 0.0;
        this.payment_status = "Pending";
    }
    
    // Full constructor
    public Booking(int booking_id, int user_id, int package_id, int car_id, Integer driver_id,
                  String pickup_location, String destination, LocalDateTime pickup_datetime,
                  LocalDateTime actual_pickup_datetime, LocalDateTime completion_datetime,
                  double distance_traveled, int waiting_time_minutes, double base_fare,
                  double extra_distance_charge, double waiting_charge, double total_fare,
                  int num_passengers, String notes, String status, String payment_status,
                  String payment_method, Timestamp created_at, Timestamp updated_at) {
        this.booking_id = booking_id;
        this.user_id = user_id;
        this.package_id = package_id;
        this.car_id = car_id;
        this.driver_id = driver_id;
        this.pickup_location = pickup_location;
        this.destination = destination;
        this.pickup_datetime = pickup_datetime;
        this.actual_pickup_datetime = actual_pickup_datetime;
        this.completion_datetime = completion_datetime;
        this.distance_traveled = distance_traveled;
        this.waiting_time_minutes = waiting_time_minutes;
        this.base_fare = base_fare;
        this.extra_distance_charge = extra_distance_charge;
        this.waiting_charge = waiting_charge;
        this.total_fare = total_fare;
        this.num_passengers = num_passengers;
        this.notes = notes;
        this.status = status;
        this.payment_status = payment_status;
        this.payment_method = payment_method;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    
    // Default constructor
    public Booking() {
    }
    
    // Getters and Setters
    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(LocalDateTime pickup_datetime) {
        this.pickup_datetime = pickup_datetime;
    }

    public LocalDateTime getActual_pickup_datetime() {
        return actual_pickup_datetime;
    }

    public void setActual_pickup_datetime(LocalDateTime actual_pickup_datetime) {
        this.actual_pickup_datetime = actual_pickup_datetime;
    }

    public LocalDateTime getCompletion_datetime() {
        return completion_datetime;
    }

    public void setCompletion_datetime(LocalDateTime completion_datetime) {
        this.completion_datetime = completion_datetime;
    }

    public double getDistance_traveled() {
        return distance_traveled;
    }

    public void setDistance_traveled(double distance_traveled) {
        this.distance_traveled = distance_traveled;
    }

    public int getWaiting_time_minutes() {
        return waiting_time_minutes;
    }

    public void setWaiting_time_minutes(int waiting_time_minutes) {
        this.waiting_time_minutes = waiting_time_minutes;
    }

    public double getBase_fare() {
        return base_fare;
    }

    public void setBase_fare(double base_fare) {
        this.base_fare = base_fare;
    }

    public double getExtra_distance_charge() {
        return extra_distance_charge;
    }

    public void setExtra_distance_charge(double extra_distance_charge) {
        this.extra_distance_charge = extra_distance_charge;
    }

    public double getWaiting_charge() {
        return waiting_charge;
    }

    public void setWaiting_charge(double waiting_charge) {
        this.waiting_charge = waiting_charge;
    }

    public double getTotal_fare() {
        return total_fare;
    }

    public void setTotal_fare(double total_fare) {
        this.total_fare = total_fare;
    }

    public int getNum_passengers() {
        return num_passengers;
    }

    public void setNum_passengers(int num_passengers) {
        this.num_passengers = num_passengers;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCar_number_plate() {
        return car_number_plate;
    }

    public void setCar_number_plate(String car_number_plate) {
        this.car_number_plate = car_number_plate;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
    
    /**
     * Calculate final fare based on package type, distance traveled, and waiting time
     * @param packageFare Base fare from the package
     * @param packageType Type of package (Day or Kilometer)
     * @param includedKm Included kilometers (for Day packages)
     * @param perKmCharge Per kilometer charge
     * @param waitingChargeRate Waiting charge rate
     * @param distanceTraveled Actual distance traveled
     * @param waitingMinutes Waiting time in minutes
     * @return Total calculated fare
     */
    public static double calculateFare(double packageFare, String packageType, int includedKm, 
                                    double perKmCharge, double waitingChargeRate, 
                                    double distanceTraveled, int waitingMinutes) {
        double baseFare = packageFare;
        double extraDistanceCharge = 0.0;
        double waitingCharge = 0.0;
        
        // Calculate extra distance charge for Day packages
        if (packageType.equals("Day") && distanceTraveled > includedKm) {
            extraDistanceCharge = (distanceTraveled - includedKm) * perKmCharge;
        } 
        // Calculate distance charge for Kilometer packages
        else if (packageType.equals("Kilometer")) {
            baseFare = distanceTraveled * perKmCharge;
        }
        
        // Calculate waiting charge
        if (waitingMinutes > 0) {
            // Convert minutes to hours and apply rate
            waitingCharge = (waitingMinutes / 60.0) * waitingChargeRate;
        }
        
        // Return total fare
        return baseFare + extraDistanceCharge + waitingCharge;
    }
}