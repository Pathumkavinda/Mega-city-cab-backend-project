/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;

import Category.Category;
import Category.CategoryCRUDOperation;
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


@Path("categories")
public class CategoryService {
    
    private final Gson gson = new Gson();
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(String json) {
        Category category = gson.fromJson(json, Category.class);
        int categoryId = CategoryCRUDOperation.addCategory(category);
        
        if (categoryId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Category created successfully\", \"categoryId\": " + categoryId + "}")
                    .build();
        } else if (categoryId == -2) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid maximum passengers for this category\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create category\"}")
                .build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@QueryParam("available") Boolean available,
                                 @QueryParam("car_id") Integer carId,
                                 @QueryParam("name") String categoryName) {
        List<Category> categories;
        
        if (available != null) {
            categories = CategoryCRUDOperation.getCategoriesByAvailability(available);
        } else if (carId != null) {
            categories = CategoryCRUDOperation.getCategoriesByCarId(carId);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            categories = CategoryCRUDOperation.getCategoriesByName(categoryName);
        } else {
            categories = CategoryCRUDOperation.getCategories();
        }
        
        return Response.ok(gson.toJson(categories)).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryById(@PathParam("id") int id) {
        Category category = CategoryCRUDOperation.getCategoryById(id);
        
        if (category != null) {
            return Response.ok(gson.toJson(category)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Category not found\"}")
                .build();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("id") int id, String json) {
        Category category = gson.fromJson(json, Category.class);
        category.setCategory_id(id);
        
        int rowsUpdated = CategoryCRUDOperation.updateCategory(category);
        
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Category updated successfully\"}").build();
        } else if (rowsUpdated == -2) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid maximum passengers for this category\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update category\"}")
                .build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") int id) {
        int rowsDeleted = CategoryCRUDOperation.deleteCategory(id);
        
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Category deleted successfully\"}").build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete category\"}")
                .build();
    }
    
    @PUT
    @Path("/{id}/availability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoryAvailability(@PathParam("id") int id, String json) {
        try {
            // Parse JSON to get the availability status
            boolean isAvailable = gson.fromJson(json, Boolean.class);
            
            // Get the category
            Category category = CategoryCRUDOperation.getCategoryById(id);
            
            if (category == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Category not found\"}")
                        .build();
            }
            
            // Update availability
            category.setIs_available(isAvailable);
            int rowsUpdated = CategoryCRUDOperation.updateCategory(category);
            
            if (rowsUpdated > 0) {
                return Response.ok("{\"message\": \"Category availability updated successfully\"}").build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to update category availability\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}/car")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoryCar(@PathParam("id") int id, String json) {
        try {
            // Parse JSON to get the car ID
            Integer carId = gson.fromJson(json, Integer.class);
            
            // Get the category to check if it exists
            Category category = CategoryCRUDOperation.getCategoryById(id);
            
            if (category == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Category not found\"}")
                        .build();
            }
            
            // Update car ID
            int rowsUpdated = CategoryCRUDOperation.updateCategoryCar(id, carId);
            
            if (rowsUpdated > 0) {
                return Response.ok("{\"message\": \"Category car updated successfully\"}").build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to update category car\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
}