/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;


import Booking.Booking;
import Booking.BookingCRUDOperation;
import Package.Package;
import Package.PackageCRUDOperation;
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Booking service to handle booking-related operations.
 */
@Path("bookings")
public class BookingService {

    private final BookingCRUDOperation bookingCRUDOperation = new BookingCRUDOperation();
    private final Gson gson = new Gson();

    /**
     * Create a new booking.
     * @param json JSON representation of booking data
     * @return Response with success message or error
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(String json) {
        Booking booking = gson.fromJson(json, Booking.class);

        // Validate that the package exists in the database
        Package selectedPackage = PackageCRUDOperation.getPackageById(booking.getPackageId());
        if (selectedPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid package ID\"}")
                    .build();
        }

        // Create the booking
        int bookingId = bookingCRUDOperation.addBooking(booking);
        if (bookingId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Booking created successfully\", \"bookingId\": " + bookingId + "}")
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create booking\"}")
                .build();
    }

    /**
     * Get all bookings
     * @return List of bookings
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookings() {
        List<Booking> bookings = bookingCRUDOperation.getBookings();
        return Response.ok(gson.toJson(bookings)).build();
    }

    /**
     * Get a booking by ID
     * @param id Booking ID
     * @return Booking data in JSON format or error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingById(@PathParam("id") int id) {
        Booking booking = bookingCRUDOperation.getBookingById(id);
        
        if (booking != null) {
            return Response.ok(gson.toJson(booking)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Booking not found\"}")
                .build();
    }

    /**
     * Update an existing booking
     * @param id Booking ID
     * @param json Updated booking data
     * @return Success message or error
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id") int id, String json) {
        Booking booking = gson.fromJson(json, Booking.class);
        booking.setBookingId(id);
        
        int rowsUpdated = bookingCRUDOperation.updateBooking(booking);
        
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Booking updated successfully\"}").build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update booking\"}")
                .build();
    }

    /**
     * Delete a booking by ID
     * @param id Booking ID to delete
     * @return Success message or error
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBooking(@PathParam("id") int id) {
        int rowsDeleted = bookingCRUDOperation.deleteBooking(id);
        
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Booking deleted successfully\"}").build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete booking\"}")
                .build();
    }
}

