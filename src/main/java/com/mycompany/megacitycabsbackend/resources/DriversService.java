package com.mycompany.megacitycabsbackend.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Driver.Drivers;
import Driver.DriversOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drivers")
public class DriversService {

    private final DriversOperations driversOperations = new DriversOperations();
    private final Gson gson = new GsonBuilder().create();

    // 🔹 CREATE DRIVER
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDriver(String json) {
        System.out.println("🚀 Received JSON: " + json);
        Drivers driver = gson.fromJson(json, Drivers.class);

        System.out.println("✅ Parsed Driver: " + driver.getDlNumber());

        int driverId = driversOperations.addDriver(driver);

        if (driverId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Driver created successfully\", \"driverId\": " + driverId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create driver\"}")
                    .build();
        }
    }

    // 🔹 FETCH ALL DRIVERS
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrivers() {
        List<Drivers> drivers = driversOperations.getAllDrivers();
        return Response.ok(gson.toJson(drivers)).build();
    }

    // 🔹 GET DRIVER BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverById(@PathParam("id") int id) {
        Drivers driver = driversOperations.getDriverById(id);

        if (driver != null) {
            return Response.ok(gson.toJson(driver)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 🔹 UPDATE DRIVER
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriver(@PathParam("id") int id, String json) {
        Drivers driver = gson.fromJson(json, Drivers.class);
        driver.setId(id);
        int rowsUpdated = driversOperations.updateDriver(driver);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Driver updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update driver\"}")
                .build();
    }

    // 🔹 DELETE DRIVER
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@PathParam("id") int id) {
        int rowsDeleted = driversOperations.deleteDriver(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Driver deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete driver\"}")
                .build();
    }

    @PUT
@Path("/{id}/status")
@Consumes(MediaType.TEXT_PLAIN) // ✅ Expect plain text instead of JSON
@Produces(MediaType.APPLICATION_JSON)
public Response updateDriverStatus(@PathParam("id") int id, String status) {
    int rowsUpdated = driversOperations.updateStatus(id, status);
    if (rowsUpdated > 0) {
        return Response.ok("{\"message\": \"Driver status updated successfully\"}").build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"message\": \"Failed to update driver status\"}")
            .build();
}

}
