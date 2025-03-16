package Vehicle;

public class Vehicles {

    private int id;
    private int catId;
    private String vehiNumber;
    private String stat;

    public Vehicles(int id, int catId, String vehiNumber, String stat) {
        this.id = id;
        this.catId = catId;
        this.vehiNumber = vehiNumber;
        this.stat = stat;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getVehiNumber() {
        return vehiNumber;
    }

    public void setVehiNumber(String vehiNumber) {
        this.vehiNumber = vehiNumber;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
