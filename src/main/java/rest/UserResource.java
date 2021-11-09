package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import entities.ResetPasswordDTO;
import entities.DeveloperDTO;
import facades.UserFacade;
import utils.EMF_Creator;
import utils.MailSystem;
import utils.SetupTestUsers;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static UserFacade facade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

//    @GET
//    @Path("all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getAllDevelopers() {
//        return GSON.toJson(facade.listOfAllDevs());
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create")
    public String createUser(String user) {
        User endUser = GSON.fromJson(user, User.class);
        User endUserForReturn = facade.createUser(endUser);
        return GSON.toJson(endUserForReturn);
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("resetpass")
    public String resetPW(String emailAndAnswer) {
        ResetPasswordDTO reset = GSON.fromJson(emailAndAnswer, ResetPasswordDTO.class);
        MailSystem ms = new MailSystem();
        System.out.println(reset.toString());
        ms.resetPW(reset);
        return "{\"resp\":\"success\"}";
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("newpassword")
    public String createNewPass(String emailAndNewPass) {
        User user = GSON.fromJson(emailAndNewPass, User.class);
        facade.updatePasswordForUser(user);
        return "{\"resp\":\"success\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed({"developer"})
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Welcome " + thisuser + "\"}";
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("account/{username}")
   // @RolesAllowed({"user"})
    public String getAccountInfo(@PathParam("username") String username) {
        User user = facade.findUserByUsername(username);
        DeveloperDTO userDTO = new DeveloperDTO(user.getUsername());
        return GSON.toJson(userDTO);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) Developer: " + thisuser + "\"}";
    }


    @GET
    @Path("populate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String populate() {
        SetupTestUsers s = new SetupTestUsers();
        s.populate();
        return "Success";
    }

}