/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Package;

/**
 * Package model class for Mega City Cab services
 * @author Admin
 */
public class Package {
    private int package_id;
    private String package_name;
    private String package_type; // "Day" or "Kilometer"
    private String category_name; // "Economy", "Premium", "Luxury", "Van"
    private double base_price;
    private int included_kilometers; // For day packages
    private double per_kilometer_charge; // Additional km charge
    private double waiting_charge;
    private String description;
    private boolean is_active;
    
    // Default constructor
    public Package() {
        // Default constructor
    }
    
    // Full constructor
    public Package(int package_id, String package_name, String package_type, 
                   String category_name, double base_price, int included_kilometers, 
                   double per_kilometer_charge, double waiting_charge, 
                   String description, boolean is_active) {
        this.package_id = package_id;
        this.package_name = package_name;
        this.package_type = package_type;
        this.category_name = category_name;
        this.base_price = base_price;
        this.included_kilometers = included_kilometers;
        this.per_kilometer_charge = per_kilometer_charge;
        this.waiting_charge = waiting_charge;
        this.description = description;
        this.is_active = is_active;
    }

    // Getters and Setters
    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
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

    public double getBase_price() {
        return base_price;
    }

    public void setBase_price(double base_price) {
        this.base_price = base_price;
    }

    public int getIncluded_kilometers() {
        return included_kilometers;
    }

    public void setIncluded_kilometers(int included_kilometers) {
        this.included_kilometers = included_kilometers;
    }

    public double getPer_kilometer_charge() {
        return per_kilometer_charge;
    }

    public void setPer_kilometer_charge(double per_kilometer_charge) {
        this.per_kilometer_charge = per_kilometer_charge;
    }

    public double getWaiting_charge() {
        return waiting_charge;
    }

    public void setWaiting_charge(double waiting_charge) {
        this.waiting_charge = waiting_charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    
    /**
     * Validate basic package information
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        // Basic validation rules
        return package_name != null && !package_name.isEmpty() &&
               package_type != null && 
               (package_type.equals("Day") || package_type.equals("Kilometer")) &&
               category_name != null && 
               (category_name.equals("Economy") || 
                category_name.equals("Premium") || 
                category_name.equals("Luxury") || 
                category_name.equals("Van")) &&
               base_price >= 0 &&
               per_kilometer_charge >= 0 &&
               waiting_charge >= 0;
    }
    
    /**
     * Formats the base price for display with Rs. prefix and .00 suffix
     * @return Formatted price string
     */
    public String getFormattedBasePrice() {
        return String.format("Rs. %.2f", base_price);
    }
    
    /**
     * Calculate additional price for extra kilometers beyond included kilometers
     * @param totalKilometers Total kilometers traveled
     * @return Additional charge for extra kilometers
     */
    public double calculateExtraKilometerCharge(int totalKilometers) {
        if (totalKilometers <= included_kilometers) {
            return 0.0;
        }
        int extraKilometers = totalKilometers - included_kilometers;
        return extraKilometers * per_kilometer_charge;
    }
}