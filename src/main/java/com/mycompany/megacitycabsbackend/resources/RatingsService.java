package com.mycompany.megacitycabsbackend.resources;

import Rating.Ratings;
import Rating.RatingsOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ratings")
public class RatingsService {

   private final RatingsOperations ratingsOperations = new RatingsOperations();
    private final Gson gson = new GsonBuilder().create();

 @GET
@Produces(MediaType.APPLICATION_JSON)
public Response getAllRatings() {
    List<Ratings> ratings = ratingsOperations.getAllRatings();

    // ✅ Always return an empty array instead of 404 if no ratings are found
    return Response.ok(gson.toJson(ratings)).build();
}
    // 🔹 ADD RATING
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRating(String json) {
        Ratings rating = gson.fromJson(json, Ratings.class);

        int result = RatingsOperations.addRating(rating);
        
        if (result > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Rating added successfully\"}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to add rating\"}")
                    .build();
        }
    }
}
