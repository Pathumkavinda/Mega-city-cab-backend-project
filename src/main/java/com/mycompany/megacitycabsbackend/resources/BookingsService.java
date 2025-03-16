package com.mycompany.megacitycabsbackend.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Booking.Bookings;
import Booking.BookingsOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("bookings")
public class BookingsService {

    private final BookingsOperations bookingsOperations = new BookingsOperations();
    private final Gson gson = new GsonBuilder().create();

    // 🔹 CREATE BOOKING
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBooking(String json) {
        System.out.println("🚀 Received JSON: " + json);
        Bookings booking = gson.fromJson(json, Bookings.class);

        System.out.println("✅ Parsed Booking: " + booking.getPickupLocation());

        int bookingId = bookingsOperations.addBooking(booking);

        if (bookingId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\", \"bookingId\": " + bookingId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create booking\"}")
                    .build();
        }
    }

    // 🔹 FETCH ALL BOOKINGS
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookings() {
        List<Bookings> bookings = bookingsOperations.getAllBookings();
        return Response.ok(gson.toJson(bookings)).build();
    }

    // 🔹 GET BOOKING BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingById(@PathParam("id") int id) {
        Bookings booking = bookingsOperations.getBookingById(id);

        if (booking != null) {
            return Response.ok(gson.toJson(booking)).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Booking not found\"}")
                .build();
    }

   // 🔹 GET BOOKINGS BY USER ID
@GET
@Path("/user/{userId}")
@Produces(MediaType.APPLICATION_JSON)
public Response getBookingsByUserId(@PathParam("userId") int userId) {
    List<Bookings> bookings = bookingsOperations.getBookingsByUserId(userId);

    return Response.ok(gson.toJson(bookings)).build(); // ✅ Always returns an array (empty if no records)
}


    // 🔹 UPDATE BOOKING (Full Update)
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id") int id, String json) {
        Bookings booking = gson.fromJson(json, Bookings.class);
        booking.setId(id);
        int rowsUpdated = bookingsOperations.updateBooking(booking);

        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Booking updated successfully\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update booking\"}")
                .build();
    }

    // 🔹 DELETE BOOKING
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooking(@PathParam("id") int id) {
        int rowsDeleted = bookingsOperations.deleteBooking(id);

        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Booking deleted successfully\"}").build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete booking\"}")
                .build();
    }

    // 🔹 UPDATE BOOKING STATUS (Partial Update)
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookingStatus(@PathParam("id") int id, String json) {
        try {
            // Parse JSON { "status": "confirmed" }
            String status = gson.fromJson(json, StatusUpdate.class).status;

            int rowsUpdated = bookingsOperations.updateBookingStatus(id, status);
            if (rowsUpdated > 0) {
                return Response.ok("{\"message\": \"Booking status updated successfully\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update booking status\"}")
                .build();
    }

    // 🔹 Helper Class for JSON Parsing
    private static class StatusUpdate {
        String status;
    }
}
