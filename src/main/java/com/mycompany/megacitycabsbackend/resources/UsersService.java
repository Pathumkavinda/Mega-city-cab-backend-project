/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.megacitycabsbackend.resources;

import User.Users;
import User.UsersOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Map;
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
 * @author Janith
 */
@Path("users")
public class UsersService {

    private final UsersOperations usersOperations = new UsersOperations();
    private final Gson gson = new GsonBuilder().create();

    // 🔹 CREATE USER
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(String json) {
        System.out.println("🚀 Received JSON: " + json);
        Users user = gson.fromJson(json, Users.class);

        System.out.println("✅ Parsed User: " + user.getUsername());

        int userId = usersOperations.addUser(user);

        if (userId > 0) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"User created successfully\", \"userId\": " + userId + "}")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to create user\"}")
                    .build();
        }
    }

    // 🔹 FETCH ALL USERS
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<Users> users = usersOperations.getAllUsers();
        return Response.ok(gson.toJson(users)).build();
    }

    // 🔹 GET USER BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        Users user = usersOperations.getUserById(id);

        if (user != null) {
            return Response.ok(gson.toJson(user)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
@Path("/{id}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response updateUser(@PathParam("id") int id, String json) {
    Users user = gson.fromJson(json, Users.class);
    user.setId(id);

    // ✅ Ensure password is not overwritten if not provided
    Users existingUser = usersOperations.getUserById(id);
    if (existingUser == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\": \"User not found\"}")
                .build();
    }

    if (user.getpWord() == null || user.getpWord().isEmpty()) {
        user.setpWord(existingUser.getpWord()); // Keep the existing password
    }

    int rowsUpdated = usersOperations.updateUser(user);
    if (rowsUpdated > 0) {
        return Response.ok("{\"message\": \"User updated successfully\"}").build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("{\"message\": \"Failed to update user\"}")
            .build();
}


// ✅ DELETE USER API
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        boolean isDeleted = UsersOperations.deleteUser(id);

        if (isDeleted) {
            return Response.ok("{\"message\": \"User deleted successfully\"}").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to delete user\"}").build();
        }
    }

    // 🔹 VALIDATE USER LOGIN
    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUser(String json) {
        Users loginData = gson.fromJson(json, Users.class);
        Users user = usersOperations.isValidUser(loginData.getUEmail(), loginData.getpWord());

        if (user != null) {
            return Response.ok(gson.toJson(user)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"message\": \"Invalid email or password\"}")
                .build();
    }
// 🔹 UPDATE USER ROLE

    @PUT
    @Path("/{id}/updateRole/{newRole}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserRole(@PathParam("id") int userId, @PathParam("newRole") String newRole) {
        int rowsUpdated = usersOperations.updateUserRole(userId, newRole);
        if (rowsUpdated > 0) {
            return Response.ok("{\"message\": \"User role updated successfully\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Failed to update user role\"}")
                .build();
    }
    
    @GET
@Path("/check-email")
@Produces(MediaType.APPLICATION_JSON)
public Response checkEmail(@QueryParam("userId") int userId, @QueryParam("email") String email) {
    boolean isUsedByOther = usersOperations.isEmailUsedByOtherUser(userId, email);

    if (isUsedByOther) {
        return Response.ok("{\"exists\": true}").build();
    } else {
        return Response.ok("{\"exists\": false}").build();
    }
}

// 🔹 UPDATE PASSWORD API
@PUT
@Path("/{id}/change-password")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response changePassword(@PathParam("id") int id, String json) {
    Users userData = gson.fromJson(json, Users.class);

    boolean success = usersOperations.updatePassword(id, userData.getCurrentPassword(), userData.getNewPassword());

    if (success) {
        return Response.ok("{\"message\": \"Password updated successfully\"}").build();
    }

    return Response.status(Response.Status.UNAUTHORIZED)
            .entity("{\"message\": \"Invalid current password\"}")
            .build();
}



}
