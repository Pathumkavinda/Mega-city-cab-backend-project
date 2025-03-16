package com.mycompany.megacitycabsbackend.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Vehicle.Vehicles;
import Vehicle.VehiclesOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("vehicles")
public class VehiclesService {

    private final VehiclesOperations vehiclesOperations = new VehiclesOperations();
    private final Gson gson = new GsonBuilder().create();

    // 🔹 CREATE VEHICLE
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addVehicle(String json) {
        System.out.println("🚀 Received JSON: " + json);
        Vehicles vehicle = gson.fromJson(json, Vehicles.class);

        System.out.println("✅ Parsed Vehicle: " + vehicle.getVehiNumber());

        int vehicleId = vehiclesOperations.addVehicle(vehicle);

        if (vehicleId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Vehicle created successfully\", \"vehicleId\": " + vehicleId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create vehicle\"}")
                    .build();
        }
    }

    // 🔹 FETCH ALL VEHICLES
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicles() {
        List<Vehicles> vehicles = vehiclesOperations.getAllVehicles();
        return Response.ok(gson.toJson(vehicles)).build();
    }

    // 🔹 GET VEHICLE BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicleById(@PathParam("id") int id) {
        Vehicles vehicle = vehiclesOperations.getVehicleById(id);

        if (vehicle != null) {
            return Response.ok(gson.toJson(vehicle)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 🔹 UPDATE VEHICLE
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVehicle(@PathParam("id") int id, String json) {
        Vehicles vehicle = gson.fromJson(json, Vehicles.class);
        vehicle.setId(id);
        int rowsUpdated = vehiclesOperations.updateVehicle(vehicle);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Vehicle updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update vehicle\"}")
                .build();
    }

    // 🔹 DELETE VEHICLE
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVehicle(@PathParam("id") int id) {
        int rowsDeleted = vehiclesOperations.deleteVehicle(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Vehicle deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete vehicle\"}")
                .build();
    }

    // 🔹 UPDATE VEHICLE STATUS
@PUT
@Path("/{id}/status")
@Consumes(MediaType.TEXT_PLAIN)  // ✅ Ensure it matches the frontend
@Produces(MediaType.APPLICATION_JSON)
public Response updateVehicleStatus(@PathParam("id") int id, String status) {
    if (status == null || status.isEmpty()) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"Status cannot be empty\"}")
                .build();
    }

    int rowsUpdated = vehiclesOperations.updateStatus(id, status);
    if (rowsUpdated > 0) {
        return Response.ok("{\"message\": \"Vehicle status updated successfully\"}").build();
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"message\": \"Failed to update vehicle status\"}")
            .build();
}

@GET
@Path("/category/random/{catId}")
@Produces(MediaType.APPLICATION_JSON)
public String getRandomVehicleByCategory(@PathParam("catId") int catId) {
    Vehicles vehicle = vehiclesOperations.getRandomVehicleByCategory(catId);

    if (vehicle == null) {
        return "{\"error\": \"No available vehicles found\"}";
    }

    return new Gson().toJson(vehicle); // ✅ Convert manually
}

}
