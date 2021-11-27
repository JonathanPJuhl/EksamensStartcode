package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import facades.ConnectionsFacade;
//import security.Logging;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("connection")
public class ConnectionsResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static ConnectionsFacade facade = ConnectionsFacade.getConnectionsFacade(EMF);
   /* private static Logging log;

    static {
        try {
            log = Logging.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all/users")
    @RolesAllowed("user")
    public String getAllUsers() {
        String username = securityContext.getUserPrincipal().getName();
        return GSON.toJson(facade.GetListOfAllUsersNotBlocked(username));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("my/connections")
    @RolesAllowed("user")
    public String getAllConnectionsForUser() throws IOException {
        String username = securityContext.getUserPrincipal().getName();
        return GSON.toJson(facade.getAllConnectionsForUser(username));
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("send/request")
    @RolesAllowed("user")
    public Response sendConnectionRequest(String usernames) {
        String thisUser = securityContext.getUserPrincipal().getName();
        String otherUser;
        try {
            JsonObject json = JsonParser.parseString(usernames).getAsJsonObject();
            otherUser = json.get("username").getAsString();
            facade.sendConnectionRequest(thisUser, otherUser);
        } catch (Exception e) {
            return Response.status(403, e.toString()).build();
        }
        return Response.ok().build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unsend/request")
    @RolesAllowed("user")
    public Response unSendConnectionRequest(String usernames) {
        String thisUser = securityContext.getUserPrincipal().getName();
        String otherUser;
        try {
            JsonObject json = JsonParser.parseString(usernames).getAsJsonObject();
            otherUser = json.get("username").getAsString();
            facade.unSendConnectionRequest(thisUser, otherUser);
        } catch (Exception e) {
            return Response.status(403).build();
        }
        return Response.ok().build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("accept/request")
    @RolesAllowed("user")
    public Response acceptConnectionRequest(String usernames) {
        String thisUser = securityContext.getUserPrincipal().getName();
        String otherUser;
        try {
            JsonObject json = JsonParser.parseString(usernames).getAsJsonObject();
            otherUser = json.get("username").getAsString();
            facade.acceptConnectionRequest(thisUser, otherUser);
        } catch (Exception e) {
            return Response.status(403).build();
        }
        return Response.ok().build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deny/request")
    @RolesAllowed("user")
    public Response denyConnectionRequest(String usernames) {
        String thisUser = securityContext.getUserPrincipal().getName();
        String otherUser;
        try {
            JsonObject json = JsonParser.parseString(usernames).getAsJsonObject();
            otherUser = json.get("username").getAsString();
            facade.denyConnectionRequest(thisUser, otherUser);
        } catch (Exception e) {
            return Response.status(403).build();
        }
        return Response.ok().build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pending/requests")
    @RolesAllowed("user")
    public String pendingConnectionRequest() {
        String thisUser = securityContext.getUserPrincipal().getName();
        return GSON.toJson(facade.getPendingConnectionRequests(thisUser));
    }

}
