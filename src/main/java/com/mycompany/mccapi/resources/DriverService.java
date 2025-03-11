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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Admin
 */
@Path("drivers")
public class DriverService {
    private final DriverCRUDOperation driverCRUDOperation = new DriverCRUDOperation();
    private final Gson gson = new Gson();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDriver(String json) {
        Driver driver = gson.fromJson(json, Driver.class);
        int driverId = DriverCRUDOperation.addDriver(driver);
        if (driverId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Driver created successfully\", \"driverId\": " + driverId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create driver\"}")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrivers() {
        List<Driver> drivers = DriverCRUDOperation.getDrivers();
        return Response.ok(gson.toJson(drivers)).build();
    }
    
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
    
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverByUserId(@PathParam("userId") int userId) {
        Driver driver = DriverCRUDOperation.getDriverByUserId(userId);
        if (driver != null) {
            return Response.ok(gson.toJson(driver)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Driver not found for this user\"}")
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriver(@PathParam("id") int id, String json) {
        Driver driver = gson.fromJson(json, Driver.class);
        driver.setDriver_id(id);
        int rowsUpdated = DriverCRUDOperation.updateDriver(driver);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Driver updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update driver\"}")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@PathParam("id") int id) {
        int rowsDeleted = DriverCRUDOperation.deleteDriver(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Driver deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete driver\"}")
                .build();
    }
    
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriverStatus(@PathParam("id") int id, String json) {
        // Parse the JSON to get just the status
        boolean status = gson.fromJson(json, com.google.gson.JsonObject.class)
                            .get("active_status").getAsBoolean();
        
        int rowsUpdated = DriverCRUDOperation.updateDriverStatus(id, status);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Driver status updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update driver status\"}")
                .build();
    }
}