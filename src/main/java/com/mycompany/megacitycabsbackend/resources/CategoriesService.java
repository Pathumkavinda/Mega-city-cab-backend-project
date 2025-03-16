package com.mycompany.megacitycabsbackend.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Category.Categories;
import Category.CategoriesOperations;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("categories")
public class CategoriesService {

    private final CategoriesOperations categoriesOperations = new CategoriesOperations();
    private final Gson gson = new GsonBuilder().create();

    // 🔹 CREATE CATEGORY
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategory(String json) {
        System.out.println("🚀 Received JSON: " + json);
        Categories category = gson.fromJson(json, Categories.class);

        System.out.println("✅ Parsed Category: " + category.getCatName());

        int categoryId = categoriesOperations.addCategory(category);

        if (categoryId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Category created successfully\", \"categoryId\": " + categoryId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create category\"}")
                    .build();
        }
    }

    // 🔹 FETCH ALL CATEGORIES
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
        List<Categories> categories = categoriesOperations.getAllCategories();
        return Response.ok(gson.toJson(categories)).build();
    }

    // 🔹 GET CATEGORY BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryById(@PathParam("id") int id) {
        Categories category = categoriesOperations.getCategoryById(id);

        if (category != null) {
            return Response.ok(gson.toJson(category)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 🔹 UPDATE CATEGORY
@PUT
@Path("/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response updateCategory(@PathParam("id") int id, String json) {
    try {
        Categories category = gson.fromJson(json, Categories.class);
        category.setId(id);
        int rowsUpdated = categoriesOperations.updateCategory(category);
        
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Category updated successfully\"}").build();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"message\": \"Failed to update category\"}")
            .build();
}

    // 🔹 DELETE CATEGORY
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") int id) {
        int rowsDeleted = categoriesOperations.deleteCategory(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"Category deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete category\"}")
                .build();
    }

    // 🔹 UPDATE CATEGORY STATUS
    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoryStatus(@PathParam("id") int id, String status) {
        int rowsUpdated = categoriesOperations.updateStatus(id, status);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"Category status updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update category status\"}")
                .build();
    }
}
