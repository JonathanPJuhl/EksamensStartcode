package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.User;
import entities.ResetPasswordDTO;
import entities.UserDTO;
import facades.MultiMediaFacade;
import facades.UserFacade;
import security.Logging;
import utils.EMF_Creator;
import utils.MailSystem;
import utils.SetupTestUsers;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;


@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static UserFacade facade = UserFacade.getUserFacade(EMF);
    private static Logging log;

    static {
        try {
            log = Logging.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() throws IOException {
        log.warningLog("OH NO");
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllDevelopers() {
        return GSON.toJson(facade.listOfAllUsers());
    }

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
    @RolesAllowed({"user"})
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Welcome " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("account")
    @RolesAllowed({"user"})
    public String getAccountInfo() {
        String thisuser = securityContext.getUserPrincipal().getName();
        User user = facade.findUserByUsername(thisuser);
        return GSON.toJson(new UserDTO(user.getUsername(), user.getProfileText()));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("account/update")
    @RolesAllowed({"user"})
    public void updateAccountInfo(String accountInfo) {
        UserDTO user = GSON.fromJson(accountInfo, UserDTO.class);
        facade.updateUserProfile(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) Developer: " + thisuser + "\"}";
    }

    @POST
    @Path("/uploadfile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("user")
    public void upload(InputStream file) throws IOException {
        String thisuser = securityContext.getUserPrincipal().getName();
        MultiMediaFacade mff = new MultiMediaFacade();
        mff.saveFile(file, thisuser + ".JPG");
    }

    @GET
    @Path("/profilePicture")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed("user")
    public Response profilePic() throws IOException {
        String thisuser = securityContext.getUserPrincipal().getName();
        MultiMediaFacade mff = new MultiMediaFacade();
        File file = mff.findFile(thisuser + ".JPG");
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                .build();
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

    @POST
    @Path("/unlock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unlockAcc(String mailAndKey) {
        JsonObject json = JsonParser.parseString(mailAndKey).getAsJsonObject();
        String email = json.get("email").getAsString();
        String key = json.get("passwordForUnlocking").getAsString();
        boolean unlockUser = facade.unlockUser(email, key);
        System.out.println(unlockUser);
        if(unlockUser) {
            return Response.ok("\"resp\": \"Account unlocked\"").build();
        } else {
            return Response.status(401, "\"resp\": \"Key doesn't match\"").build();
        }
    }
}