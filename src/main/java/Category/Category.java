/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Category;


/**
 *
 * @author Admin
 */
public class Category {
    private int category_id;
    private String category_name;
    private Integer car_id;  // Changed to Integer to allow NULL
    private int max_passengers;
    private double price_per_km;
    private double waiting_charge;
    private boolean is_available;
    
    // For display purposes only (not stored in DB)
    private String car_number_plate;
    
    // Default constructor
    public Category() {
    }
    
    // Constructor with all fields
    public Category(int category_id, String category_name, Integer car_id, int max_passengers, 
                   double price_per_km, double waiting_charge, boolean is_available) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.car_id = car_id;
        this.max_passengers = max_passengers;
        this.price_per_km = price_per_km;
        this.waiting_charge = waiting_charge;
        this.is_available = is_available;
    }
    
    // Constructor with car_number_plate (for display purposes)
    public Category(int category_id, String category_name, Integer car_id, String car_number_plate,
                   int max_passengers, double price_per_km, double waiting_charge, boolean is_available) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.car_id = car_id;
        this.car_number_plate = car_number_plate;
        this.max_passengers = max_passengers;
        this.price_per_km = price_per_km;
        this.waiting_charge = waiting_charge;
        this.is_available = is_available;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public int getMax_passengers() {
        return max_passengers;
    }

    public void setMax_passengers(int max_passengers) {
        this.max_passengers = max_passengers;
    }

    public double getPrice_per_km() {
        return price_per_km;
    }

    public void setPrice_per_km(double price_per_km) {
        this.price_per_km = price_per_km;
    }

    public double getWaiting_charge() {
        return waiting_charge;
    }

    public void setWaiting_charge(double waiting_charge) {
        this.waiting_charge = waiting_charge;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public String getCar_number_plate() {
        return car_number_plate;
    }

    public void setCar_number_plate(String car_number_plate) {
        this.car_number_plate = car_number_plate;
    }
    
     /**
     * Validates the maximum passengers based on category name
     * @return true if valid, false otherwise
     */
    public boolean isValidMaxPassengers() {
        if (category_name == null) {
            return false;
        }
        
        switch (category_name) {
            case "Economy":
                return max_passengers <= 3;
            case "Premium":
            case "Luxury":
                return max_passengers <= 4;
            case "Van":
                return max_passengers <= 8;
            default:
                return false;
        }
    }

}