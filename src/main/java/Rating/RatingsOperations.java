package Rating;

import DBConnection.ConnectionHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingsOperations {

    // Method to add a new rating
    public static int addRating(Ratings rating) {
        String query = "INSERT INTO ratings (bookingId, rating, comment) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, rating.getBookingId());
            stmt.setInt(2, rating.getRating());
            stmt.setString(3, rating.getComment());

            stmt.executeUpdate();

            return 1; // Return success status
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return failure status
    }
   // ✅ Get All Ratings (Returns an empty list if no records exist)
public static List<Ratings> getAllRatings() {
    List<Ratings> ratingsList = new ArrayList<>();
    String query = "SELECT * FROM ratings";

    try (Connection conn = ConnectionHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            ratingsList.add(new Ratings(
                    rs.getInt("id"),
                    rs.getInt("bookingId"),
                    rs.getInt("rating"),
                    rs.getString("comment")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // ✅ Ensure an empty list is returned if no records are found
    return ratingsList.isEmpty() ? new ArrayList<>() : ratingsList;
}

}
