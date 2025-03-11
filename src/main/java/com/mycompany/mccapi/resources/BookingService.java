/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;

import Booking.Booking;
import Booking.BookingCRUDOperation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST API endpoints for booking management
 * @author Admin
 */
@Path("bookings")
public class BookingService {
    
    // Configure Gson with custom adapters for LocalDateTime
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    
    /**
     * Create a new booking
     * @param json JSON representation of booking data
     * @return Response with success message or error
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(String json) {
        try {
            // Parse the request JSON
            // We need to handle the dateTime formatting
            Booking booking = parseBookingFromJson(json);
            
            // Create the booking
            int bookingId = BookingCRUDOperation.createBooking(booking);
            
            if (bookingId > 0) {
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Booking created successfully\", \"booking_id\": " + bookingId + "}")
                        .build();
            } else if (bookingId == -2) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid package ID\"}")
                        .build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create booking\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Get all bookings or filter by user, driver, or status
     * @param userId Optional user ID filter
     * @param driverId Optional driver ID filter
     * @param status Optional status filter
     * @return List of bookings in JSON format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookings(@QueryParam("userId") Integer userId,
                               @QueryParam("driverId") Integer driverId,
                               @QueryParam("status") String status) {
        List<Booking> bookings;
        
        if (userId != null) {
            bookings = BookingCRUDOperation.getBookingsByUserId(userId);
        } else if (driverId != null) {
            bookings = BookingCRUDOperation.getBookingsByDriverId(driverId);
        } else if (status != null && !status.isEmpty()) {
            bookings = BookingCRUDOperation.getBookingsByStatus(status);
        } else {
            bookings = BookingCRUDOperation.getAllBookings();
        }
        
        return Response.ok(gson.toJson(bookings)).build();
    }
    
    /**
     * Get a booking by its ID
     * @param id Booking ID
     * @return Booking data in JSON format or 404 error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingById(@PathParam("id") int id) {
        Booking booking = BookingCRUDOperation.getBookingById(id);
        
        if (booking != null) {
            return Response.ok(gson.toJson(booking)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Booking not found\"}")
                .build();
    }
    
    /**
     * Update booking status
     * @param id Booking ID
     * @param json JSON containing new status
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookingStatus(@PathParam("id") int id, String json) {
        try {
            // Parse the status from JSON
            String status = gson.fromJson(json, String.class);
            
            // Validate status value
            if (!isValidStatus(status)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid status value. Must be one of: Pending, Confirmed, In Progress, Completed, Cancelled\"}")
                        .build();
            }
            
            // Update the status
            int rowsUpdated = BookingCRUDOperation.updateBookingStatus(id, status);
            
            if (rowsUpdated > 0) {
                return Response.ok("{\"message\": \"Booking status updated successfully\"}").build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Booking not found or status not updated\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
    
    /**
     * Assign a driver to a booking
     * @param id Booking ID
     * @param json JSON containing driver ID
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/driver")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignDriver(@PathParam("id") int id, String json) {
        try {
            // Parse the driver ID from JSON
            int driverId = gson.fromJson(json, Integer.class);
            
            // Assign the driver
            int rowsUpdated = BookingCRUDOperation.assignDriver(id, driverId);
            
            if (rowsUpdated > 0) {
                return Response.ok("{\"message\": \"Driver assigned successfully\"}").build();
            }
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Booking not found or driver not assigned\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
    
    /**
     * Complete a booking with trip details
     * @param id Booking ID
     * @param json JSON containing completion details
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeBooking(@PathParam("id") int id, String json) {
        try {
            // Parse the completion details from JSON
            CompletionDetails details = gson.fromJson(json, CompletionDetails.class);
            
            // Parse datetime strings
            LocalDateTime actualPickupDateTime = null;
            if (details.actualPickupDateTime != null && !details.actualPickupDateTime.isEmpty()) {
                actualPickupDateTime = LocalDateTime.parse(details.actualPickupDateTime, 
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
            
            LocalDateTime completionDateTime = LocalDateTime.parse(details.completionDateTime, 
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            // Complete the booking
            int result = BookingCRUDOperation.completeBooking(id, actualPickupDateTime, 
                    completionDateTime, details.distanceTraveled, details.waitingTimeMinutes);
            
            if (result > 0) {
                // Get the updated booking with calculated fare
                Booking booking = BookingCRUDOperation.getBookingById(id);
                
                return Response.ok("{\"message\": \"Booking completed successfully\", " +
                        "\"total_fare\": " + booking.getTotal_fare() + "}").build();
            } else if (result == -2) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Booking not found\"}")
                        .build();
            } else if (result == -3) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"message\": \"Package not found\"}")
                        .build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to complete booking\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * Cancel a booking
     * @param id Booking ID
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/cancel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelBooking(@PathParam("id") int id) {
        int rowsUpdated = BookingCRUDOperation.cancelBooking(id);
        
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Booking cancelled successfully\"}").build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Booking not found or not cancelled\"}")
                .build();
    }
    
    /**
     * Parse a booking object from JSON, handling datetime conversions
     * @param json JSON string
     * @return Booking object
     */
    private Booking parseBookingFromJson(String json) {
        // First parse as a temporary object to handle date format
        BookingRequest request = gson.fromJson(json, BookingRequest.class);
        
        // Convert the pickup_datetime string to LocalDateTime
        LocalDateTime pickupDateTime = LocalDateTime.parse(request.pickup_datetime, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        // Create the real booking object
        return new Booking(
                request.user_id,
                request.package_id,
                request.car_id,
                request.pickup_location,
                request.destination,
                pickupDateTime,
                request.num_passengers,
                request.notes,
                request.status
        );
    }
    
    /**
     * Validate booking status value
     * @param status Status to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidStatus(String status) {
        return status != null && (
                status.equals("Pending") ||
                status.equals("Confirmed") ||
                status.equals("In Progress") ||
                status.equals("Completed") ||
                status.equals("Cancelled")
        );
    }
    
    /**
     * Temporary class for parsing booking JSON
     */
    private static class BookingRequest {
        public int user_id;
        public int package_id;
        public int car_id;
        public String pickup_location;
        public String destination;
        public String pickup_datetime; // String format to parse
        public int num_passengers;
        public String notes;
        public String status;
    }
    
    /**
     * Class for parsing booking completion details
     */
    private static class CompletionDetails {
        public String actualPickupDateTime;
        public String completionDateTime;
        public double distanceTraveled;
        public int waitingTimeMinutes;
    }
    
    /**
     * Adapter for converting LocalDateTime to/from JSON
     */
    private static class LocalDateTimeAdapter implements com.google.gson.JsonSerializer<LocalDateTime>, 
                                                       com.google.gson.JsonDeserializer<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        @Override
        public com.google.gson.JsonElement serialize(LocalDateTime src, 
                                                   java.lang.reflect.Type typeOfSrc, 
                                                   com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(formatter.format(src));
        }
        
        @Override
        public LocalDateTime deserialize(com.google.gson.JsonElement json, 
                                       java.lang.reflect.Type typeOfT, 
                                       com.google.gson.JsonDeserializationContext context) 
                throws com.google.gson.JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}
