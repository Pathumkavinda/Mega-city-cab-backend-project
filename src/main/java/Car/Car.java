/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Car;

/**
 *
 * @author Admin
 */
public class Car {
    private int id;
    private int category_id;
    private String number_plate;
    private String chassis_number;
    private boolean is_active;
    
    // For display purposes only (not stored in DB)
    private String category_name;
    
    public Car() {
        // Default constructor
    }
    
    public Car(int id, int category_id, String number_plate, String chassis_number, boolean is_active) {
        this.id = id;
        this.category_id = category_id;
        this.number_plate = number_plate;
        this.chassis_number = chassis_number;
        this.is_active = is_active;
    }
    
    // Constructor with category_name for display purposes
    public Car(int id, int category_id, String category_name, String number_plate, String chassis_number, boolean is_active) {
        this.id = id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.number_plate = number_plate;
        this.chassis_number = chassis_number;
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getChassis_number() {
        return chassis_number;
    }

    public void setChassis_number(String chassis_number) {
        this.chassis_number = chassis_number;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    
    /**
     * Validates the number plate format (ABC-1234)
     * @return true if valid, false otherwise
     */
    public boolean isValidNumberPlate() {
        return number_plate != null && number_plate.matches("^[A-Z]{3}-\\d{4}$");
    }
    
    /**
     * Validates the chassis number (at least 10 alphanumeric characters)
     * @return true if valid, false otherwise
     */
    public boolean isValidChassisNumber() {
        return chassis_number != null && chassis_number.length() >= 10 && chassis_number.matches("^[A-Z0-9]+$");
    }
}