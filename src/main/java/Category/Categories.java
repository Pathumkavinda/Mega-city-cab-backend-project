package Category;

public class Categories {
    private int id;
    private String catName;
    private int maxPassengers;
    private double perKm;
    private double perHr;
    private double perDayPrice;
    private int perDayKm;
    private String stat;

    // Constructor
    public Categories(int id, String catName, int maxPassengers, double perKm, double perHr, double perDayPrice, int perDayKm, String stat) {
        this.id = id;
        this.catName = catName;
        this.maxPassengers = maxPassengers;
        this.perKm = perKm;
        this.perHr = perHr;
        this.perDayPrice = perDayPrice;
        this.perDayKm = perDayKm;
        this.stat = stat;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }

    public int getMaxPassengers() { return maxPassengers; }
    public void setMaxPassengers(int maxPassengers) { this.maxPassengers = maxPassengers; }

    public double getPerKm() { return perKm; }
    public void setPerKm(double perKm) { this.perKm = perKm; }

    public double getPerHr() { return perHr; }
    public void setPerHr(double perHr) { this.perHr = perHr; }

    public double getPerDayPrice() { return perDayPrice; }
    public void setPerDayPrice(double perDayPrice) { this.perDayPrice = perDayPrice; }

    public int getPerDayKm() { return perDayKm; }
    public void setPerDayKm(int perDayKm) { this.perDayKm = perDayKm; }

    public String getStat() { return stat; }
    public void setStat(String stat) { this.stat = stat; }
}
