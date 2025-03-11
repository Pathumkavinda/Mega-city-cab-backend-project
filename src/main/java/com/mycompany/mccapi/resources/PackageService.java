/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.mccapi.resources;

import Package.Package;
import Package.PackageCRUDOperation;
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
 * REST API endpoints for package management
 * @author Admin
 */
@Path("packages")
public class PackageService {
    
    private final Gson gson = new Gson();
    
    /**
     * Create a new package
     * @param json JSON representation of package data
     * @return Response with success message or error
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPackage(String json) {
        Package pkg = gson.fromJson(json, Package.class);
        int packageId = PackageCRUDOperation.addPackage(pkg);
        
        if (packageId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Package created successfully\", \"packageId\": " + packageId + "}")
                    .build();
        } else if (packageId == -2) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid package data\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create package\"}")
                .build();
    }
    
    /**
     * Get all packages or filter by various criteria
     * @param active Optional active status filter
     * @param category Optional category filter
     * @param type Optional package type filter
     * @return List of packages in JSON format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPackages(@QueryParam("active") Boolean active,
                              @QueryParam("category") String category,
                              @QueryParam("type") String type) {
        List<Package> packages;
        
        if (active != null && active && category != null) {
            packages = PackageCRUDOperation.getActivePackagesByCategory(category);
        } else if (active != null && active) {
            packages = PackageCRUDOperation.getActivePackages();
        } else if (category != null) {
            packages = PackageCRUDOperation.getPackagesByCategory(category);
        } else if (type != null) {
            packages = PackageCRUDOperation.getPackagesByType(type);
        } else {
            packages = PackageCRUDOperation.getPackages();
        }
        
        return Response.ok(gson.toJson(packages)).build();
    }
    
    /**
     * Get a package by its ID
     * @param id Package ID
     * @return Package data in JSON format or 404 error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPackageById(@PathParam("id") int id) {
        Package pkg = PackageCRUDOperation.getPackageById(id);
        
        if (pkg != null) {
            return Response.ok(gson.toJson(pkg)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"Package not found\"}")
                .build();
    }
    
    /**
     * Update an existing package
     * @param id Package ID to update
     * @param json Updated package data
     * @return Success message or error
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePackage(@PathParam("id") int id, String json) {
        Package pkg = gson.fromJson(json, Package.class);
        pkg.setPackage_id(id);
        
        int rowsUpdated = PackageCRUDOperation.updatePackage(pkg);
        
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Package updated successfully\"}").build();
        } else if (rowsUpdated == -2) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid package data\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update package\"}")
                .build();
    }
    
    /**
     * Delete a package
     * @param id Package ID to delete
     * @return Success message or error
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePackage(@PathParam("id") int id) {
        int rowsDeleted = PackageCRUDOperation.deletePackage(id);
        
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Package deleted successfully\"}").build();
        } else if (rowsDeleted == -2) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Cannot delete package because it is used in bookings\"}")
                    .build();
        }
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete package\"}")
                .build();
    }
    
    /**
     * Update package status (active/inactive)
     * @param id Package ID
     * @param json Boolean status value
     * @return Success message or error
     */
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePackageStatus(@PathParam("id") int id, String json) {
        try {
            // Parse JSON to get the status value
            boolean isActive = gson.fromJson(json, Boolean.class);
            
            // Get the package to check if it exists
            Package pkg = PackageCRUDOperation.getPackageById(id);
            
            if (pkg == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\": \"Package not found\"}")
                        .build();
            }
            
            // Update status
            int rowsUpdated = PackageCRUDOperation.updatePackageStatus(id, isActive);
            
            if (rowsUpdated > 0) {
                return Response.ok(
                        "{\"message\": \"Package status updated successfully\", \"status\": " + isActive + "}"
                ).build();
            }
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to update package status\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Invalid request format\"}")
                    .build();
        }
    }
}