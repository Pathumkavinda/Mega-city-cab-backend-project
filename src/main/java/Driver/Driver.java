/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Driver model class that extends user information with driver-specific fields
 * @author Admin
 */
public class Driver {
    private int id;
    private int user_id;
    private String license_number;
    private LocalDate license_expiry;
    private int experience_years;
    private double rating;
    private boolean is_available;
    
    // For display purposes - from user table
    private String fullName;
    private String phone;
    private String email;
    
    // For display purposes - from car table
    private Integer assigned_car_id;
    private String car_number_plate;
    
    public Driver() {
        // Default constructor
    }
    
    public Driver(int id, int user_id, String license_number, LocalDate license_expiry, 
            int experience_years, double rating, boolean is_available) {
        this.id = id;
        this.user_id = user_id;
        this.license_number = license_number;
        this.license_expiry = license_expiry;
        this.experience_years = experience_years;
        this.rating = rating;
        this.is_available = is_available;
    }
    
    // Constructor with user and car info for display purposes
    public Driver(int id, int user_id, String license_number, LocalDate license_expiry, 
            int experience_years, double rating, boolean is_available, 
            String fullName, String phone, String email, 
            Integer assigned_car_id, String car_number_plate) {
        this.id = id;
        this.user_id = user_id;
        this.license_number = license_number;
        this.license_expiry = license_expiry;
        this.experience_years = experience_years;
        this.rating = rating;
        this.is_available = is_available;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.assigned_car_id = assigned_car_id;
        this.car_number_plate = car_number_plate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public LocalDate getLicense_expiry() {
        return license_expiry;
    }
    
    public String getLicense_expiry_formatted() {
        if (license_expiry != null) {
            return license_expiry.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    public void setLicense_expiry(LocalDate license_expiry) {
        this.license_expiry = license_expiry;
    }
    
    public void setLicense_expiry(String license_expiry) {
        if (license_expiry != null && !license_expiry.isEmpty()) {
            this.license_expiry = LocalDate.parse(license_expiry);
        }
    }

    public int getExperience_years() {
        return experience_years;
    }

    public void setExperience_years(int experience_years) {
        this.experience_years = experience_years;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAssigned_car_id() {
        return assigned_car_id;
    }

    public void setAssigned_car_id(Integer assigned_car_id) {
        this.assigned_car_id = assigned_car_id;
    }

    public String getCar_number_plate() {
        return car_number_plate;
    }

    public void setCar_number_plate(String car_number_plate) {
        this.car_number_plate = car_number_plate;
    }
    
    /**
     * Check if license is expired
     * @return true if expired, false otherwise
     */
    public boolean isLicenseExpired() {
        if (license_expiry == null) {
            return false;
        }
        return license_expiry.isBefore(LocalDate.now());
    }
    
    /**
     * Validate license number format
     * @return true if valid, false otherwise
     */
    public boolean isValidLicense() {
        // License number should be in format DL12345678 (2 letters followed by 8 digits)
        return license_number != null && license_number.matches("^[A-Z]{2}\\d{8}$");
    }
}