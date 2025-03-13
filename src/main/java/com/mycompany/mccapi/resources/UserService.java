/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mccapi.resources;

import User.UserCRUDOperation;
import User.Users;
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
@Path("users")
public class UserService {
    private final UserCRUDOperation UserCRUDOperation = new UserCRUDOperation();
    private final Gson gson = new Gson();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(String json) {
        Users user = gson.fromJson(json, Users.class);
        int userId = UserCRUDOperation.addUser(user);
        if (userId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully\", \"userId\": " + userId + "}")
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to create user\"}")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<Users> users = UserCRUDOperation.getUsers();
        return Response.ok(gson.toJson(users)).build();
    }
    /**
     * Get a user by ID
     * @param id The user ID to retrieve
     * @return User data in JSON format or 404 error
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        Users user = UserCRUDOperation.getUserById(id);
        
        if (user != null) {
            return Response.ok(gson.toJson(user)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"User not found\"}")
                .build();
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, String json) {
        Users user = gson.fromJson(json, Users.class);
        user.setId(id);
        int rowsUpdated = UserCRUDOperation.updateUser(user);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"User updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update user\"}")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        int rowsDeleted = UserCRUDOperation.deleteUser(id);
        if (rowsDeleted > 0) {
            return Response.ok("{\"message\": \"User deleted successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to delete user\"}")
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUser(String json) {
        Users user = gson.fromJson(json, Users.class);
        Users validUser = UserCRUDOperation.isValidUser(user.getUEmail(), user.getpWord());
        if (validUser != null) {
            return Response.ok("{\"message\": \"Login successful!\", \"userId\": " + validUser.getId() + ", \"uRole\": \"" + validUser.getuRole() + "\"}").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\\\"message\\\": \\\"Invalid email or password!\\\"}")
                .build();
}
}
