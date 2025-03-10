/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;

import Driver.Driver;
import Driver.DriverCRUDOperation;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 *
 * @author Admin
 */
@Path("drivers")
public class DriverService {
    private final Gson gson = new Gson();
    
    /**
     * Create a new driver
     * @param json JSON representation of driver data
     * @return Response with success message or error
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDriver(String json) {
        Driver driver = gson.fromJson(json, Driver.class);
        int driverId = DriverCRUDOperation.addDriver(driver);
        
        switch (driverId) {
            case -1:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"message\": \"Failed to create driver\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid license number format. Use format XX00000000\"}")
                        .build();
            case -3:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"License is expired\"}")
                        .build();
            case -4:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"License number already exists\"}")
                        .build();
            case -5:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"User is already registered as a driver\"}")
                        .build();
            default:
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Driver created successfully\", \"driverId\": " + driverId + "}")
                        .build();
        }
    }
    
    /**
     * Get all drivers or filter by availability
     * @param available Optional availability filter
     * @return List of drivers in JSON format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrivers(@QueryParam("available") Boolean available) {
        List<Driver> drivers;
        
        if (available != null && available) {
            drivers = DriverCRUDOperation.getAvailableDrivers();
        } else {
            drivers = DriverCRUDOperation.getDrivers();
        }
        
        return Response.ok(gson.toJson(drivers)).build();
    }
    
    /**
     * Get a driver by ID
     * @param id Driver ID
     * @return Driver data in JSON format or 404 error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverById(@PathParam("id") int id) {
        Driver driver = DriverCRUDOperation.getDriverById(id);
        
        if (driver != null) {
            return Response.ok(gson.toJson(driver)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Driver not found\"}")
                .build();
    }
    
    /**
     * Get a driver by user ID
     * @param userId User ID
     * @return Driver data in JSON format or 404 error
     */
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverByUserId(@PathParam("userId") int userId) {
        Driver driver = DriverCRUDOperation.getDriverByUserId(userId);
        
        if (driver != null) {
            return Response.ok(gson.toJson(driver)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"No driver found for this user\"}")
                .build();
    }
    
    /**
     * Update an existing driver
     * @param id Driver ID to update
     * @param json Updated driver data
     * @return Success message or error
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriver(@PathParam("id") int id, String json) {
        Driver driver = gson.fromJson(json, Driver.class);
        driver.setId(id);
        
        int result = DriverCRUDOperation.updateDriver(driver);
        
        switch (result) {
            case -1:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"message\": \"Failed to update driver\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid license number format. Use format XX00000000\"}")
                        .build();
            case -3:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"License is expired\"}")
                        .build();
            case -4:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"License number already exists for another driver\"}")
                        .build();
            case 0:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Driver not found\"}")
                        .build();
            default:
                return Response.ok("{\"message\": \"Driver updated successfully\"}").build();
        }
    }
    
    /**
     * Delete a driver
     * @param id Driver ID to delete
     * @return Success message or error
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@PathParam("id") int id) {
        int result = DriverCRUDOperation.deleteDriver(id);
        
        if (result > 0) {
            return Response.ok("{\"message\": \"Driver deleted successfully\"}").build();
        } else if (result == -2) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Cannot delete driver with assigned cars\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete driver\"}")
                .build();
    }
    
    /**
     * Update driver availability
     * @param id Driver ID
     * @param json Boolean availability value
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/availability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriverAvailability(@PathParam("id") int id, String json) {
        try {
            // Parse JSON to get the availability status
            boolean isAvailable = gson.fromJson(json, Boolean.class);
            
            // Get the driver to check if it exists
            Driver driver = DriverCRUDOperation.getDriverById(id);
            
            if (driver == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Driver not found\"}")
                        .build();
            }
            
            // Update availability
            int rowsUpdated = DriverCRUDOperation.updateDriverAvailability(id, isAvailable);
            
            if (rowsUpdated > 0) {
                return Response.ok(
                        "{\"message\": \"Driver availability updated successfully\", \"status\": " + isAvailable + "}"
                ).build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to update driver availability\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
}