/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;


import Car.Car;
import Car.CarCRUDOperation;
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
@Path("cars")
public class CarService {
    private final Gson gson = new Gson();
    
    /**
     * Create a new car
     * @param json JSON representation of car data
     * @return Response with success message or error
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCar(String json) {
        Car car = gson.fromJson(json, Car.class);
        int carId = CarCRUDOperation.addCar(car);
        
        switch (carId) {
            case -1:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"message\": \"Failed to create car\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid number plate format. Use format ABC-1234\"}")
                        .build();
            case -3:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid chassis number. Must be at least 10 uppercase alphanumeric characters\"}")
                        .build();
            case -4:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"Number plate already exists\"}")
                        .build();
            case -5:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"Chassis number already exists\"}")
                        .build();
            default:
                return Response.status(Response.Status.CREATED)
                        .entity("{\"message\": \"Car created successfully\", \"carId\": " + carId + "}")
                        .build();
        }
    }
    
    /**
     * Get all cars or filter by status or category
     * @param status Optional status filter (true/false)
     * @param categoryId Optional category ID filter
     * @param categoryName Optional category name filter
     * @return List of cars in JSON format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCars(@QueryParam("status") Boolean status,
                           @QueryParam("category_id") Integer categoryId,
                           @QueryParam("category_name") String categoryName) {
        List<Car> cars;
        
        if (status != null) {
            cars = CarCRUDOperation.getCarsByStatus(status);
        } else if (categoryId != null) {
            cars = CarCRUDOperation.getCarsByCategoryId(categoryId);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            cars = CarCRUDOperation.getCarsByCategoryName(categoryName);
        } else {
            cars = CarCRUDOperation.getCars();
        }
        
        return Response.ok(gson.toJson(cars)).build();
    }
    
    /**
     * Get a car by its ID
     * @param id Car ID
     * @return Car data in JSON format or 404 error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCarById(@PathParam("id") int id) {
        Car car = CarCRUDOperation.getCarById(id);
        
        if (car != null) {
            return Response.ok(gson.toJson(car)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Car not found\"}")
                .build();
    }
    
    /**
     * Update an existing car
     * @param id Car ID to update
     * @param json Updated car data
     * @return Success message or error
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCar(@PathParam("id") int id, String json) {
        Car car = gson.fromJson(json, Car.class);
        car.setId(id);
        
        int result = CarCRUDOperation.updateCar(car);
        
        switch (result) {
            case -1:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"message\": \"Failed to update car\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid number plate format. Use format ABC-1234\"}")
                        .build();
            case -3:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"Invalid chassis number. Must be at least 10 uppercase alphanumeric characters\"}")
                        .build();
            case -4:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"Number plate already exists on another car\"}")
                        .build();
            case -5:
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\": \"Chassis number already exists on another car\"}")
                        .build();
            case 0:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Car not found\"}")
                        .build();
            default:
                return Response.ok("{\"message\": \"Car updated successfully\"}").build();
        }
    }
    
    /**
     * Delete a car
     * @param id Car ID to delete
     * @return Success message or error
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCar(@PathParam("id") int id) {
        int result = CarCRUDOperation.deleteCar(id);
        
        if (result > 0) {
            return Response.ok("{\"message\": \"Car deleted successfully\"}").build();
        } else if (result == -2) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Cannot delete car because it is referenced by one or more categories\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete car\"}")
                .build();
    }
    
    /**
     * Update car status (active/inactive)
     * @param id Car ID
     * @param json Boolean status value
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarStatus(@PathParam("id") int id, String json) {
        try {
            // Parse JSON to get the status value
            boolean isActive = gson.fromJson(json, Boolean.class);
            
            // Get the car to check if it exists
            Car car = CarCRUDOperation.getCarById(id);
            
            if (car == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Car not found\"}")
                        .build();
            }
            
            // Update status
            int rowsUpdated = CarCRUDOperation.updateCarStatus(id, isActive);
            
            if (rowsUpdated > 0) {
                return Response.ok(
                        "{\"message\": \"Car status updated successfully\", \"status\": " + isActive + "}"
                ).build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to update car status\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
}