package User;

public class Users {

    private int id;
    private String username;
    private String pWord;
    private String uRole;
    private String fullName;
    private String uEmail;
    private String nic;
    private String address;
    private String phone;

    // Constructor
    public Users(int id, String username, String pWord, String uRole, String fullName, String uEmail, String nic, String address, String phone) {
        this.id = id;
        this.username = username;
        this.pWord = pWord;
        this.uRole = uRole;
        this.fullName = fullName;
        this.uEmail = uEmail;
        this.nic = nic;
        this.address = address;
        this.phone = phone;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getpWord() { return pWord; }
    public void setpWord(String pWord) { this.pWord = pWord; }

    public String getuRole() { return uRole; }
    public void setuRole(String uRole) { this.uRole = uRole; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUEmail() { return uEmail; }
    public void setUEmail(String uEmail) { this.uEmail = uEmail; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    // ✅ Add these two fields inside Users.java
private String currentPassword;
private String newPassword;

// ✅ Add Getter & Setter for currentPassword
public String getCurrentPassword() {
    return currentPassword;
}
public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
}

// ✅ Add Getter & Setter for newPassword
public String getNewPassword() {
    return newPassword;
}
public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
}

}
