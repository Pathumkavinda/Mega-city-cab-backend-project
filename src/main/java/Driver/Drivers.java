package Driver;

public class Drivers {
    private int id;
    private int userId;
    private String dlNumber;
    private String stat;

    public Drivers(int id, int userId, String dlNumber, String stat) {
        this.id = id;
        this.userId = userId;
        this.dlNumber = dlNumber;
        this.stat = stat;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDlNumber() { return dlNumber; }
    public void setDlNumber(String dlNumber) { this.dlNumber = dlNumber; }

    public String getStat() { return stat; }
    public void setStat(String stat) { this.stat = stat; }
}
