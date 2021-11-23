package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.ResetPasswordDTO;
import entities.User;
import entities.UserDTO;
import facades.AdminFacade;
import facades.MultiMediaFacade;
import facades.UserFacade;
import security.Logging;
import utils.EMF_Creator;
import utils.MailSystem;
import utils.SetupTestUsers;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@Path("admin")
@RolesAllowed({"admin"})
public class AdminResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static AdminFacade facade = AdminFacade.getAdminFacade(EMF);
    private static Logging log;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    static {
        try {
            log = Logging.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    public String getAllDevelopers() {
        return GSON.toJson(facade.listOfAllUsers());
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("user/deactivate")
    @RolesAllowed({"admin"})
    public Response deActivateUser(String user) {
        String username;
        try {
            JsonObject json = JsonParser.parseString(user).getAsJsonObject();
            username = json.get("username").getAsString();
            facade.deActivateUser(username);
        } catch (NoResultException nr) {
            return Response.status(401,(new Gson().toJson("User not found"))).build();
        }
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("user/deactivate")
    @RolesAllowed({"admin"})
    public Response reActivateUser(String user) {
        String username;
        try {
            JsonObject json = JsonParser.parseString(user).getAsJsonObject();
            username = json.get("username").getAsString();
            facade.reActivateUser(username);
        } catch (NoResultException nr) {
            return Response.status(401,(new Gson().toJson("User not found"))).build();
        }
        return Response.ok().build();
    }

}