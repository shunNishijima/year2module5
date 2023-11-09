package resources;

import database.SaltHashing;
import database.UserDatabase;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

/**
 * Working with URL/Web Interface
 */
@Path("/user")
public class UserResource {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GPS = "gps";

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response signup(@FormParam(USERNAME) String username, @FormParam(PASSWORD) String password) {
        User user = new User(username, password);
        if (UserDatabase.checkPasswordDuplicate(user.getPassword())){
            System.out.println("password is duplicated");
            return Response.status(Response.Status.CONFLICT).entity("Password already exists").build();
        }else{
            System.out.println("password is unique");
            UserDatabase.insertUser(user);
            return Response.ok("Signup successful").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam(USERNAME) String username, @FormParam(PASSWORD) String password) {
        if (isValidUser(username, password)) {
            return Response.ok("Login Successful").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUser(@FormParam("username") String username, @FormParam("newPassword") String newPassword) {
        UserDatabase.updatePassword(username, newPassword);
        return Response.ok("User information updated").build();
    }

    private boolean isValidUser(String username, String password) {
        User user = UserDatabase.selectUser(username);
        String salt = UserDatabase.getSalt(username);
        String hashedpassword = SaltHashing.saltSHA256(SaltHashing.toHex(SaltHashing.toByteArray(salt)),password);
        return user != null && user.getPassword().equals(hashedpassword);
    }
    @POST
    @Path("/gps")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getGPS(@FormParam(USERNAME) String username) {
        String gps = UserDatabase.getGPS(username);
        if (gps != null) {
            return Response.ok(gps).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("GPS not found for the user").build();
        }
    }
    @POST
    @Path("/brute")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBrute() {
        int count = UserDatabase.getBrute();
        return "Brute Force count: " + count;
    }

    @DELETE
    @Path("/brute")
    public Response deleteBrute() {
        UserDatabase.deleteBrute();
        return Response.ok("Brute table cleared").build();
    }
}
