package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.LoginAttempts;
import facades.MaliciousIntentFacade;
import facades.UserFacade;
import security.Logging;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("malicious")
public class RegisterMaliciousIntent {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static MaliciousIntentFacade facade = MaliciousIntentFacade.getMaliciousIntentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Logging log;

    static {
        try {
            log = Logging.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("injection")
    @Produces(MediaType.APPLICATION_JSON)
    public void registerInjectionAttempt(String info) throws IOException {
        System.out.println(info);
        log.warningLog(info);
        facade.logAttempt(GSON.fromJson(info, LoginAttempts.class));
    }
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public void registerLoginAttempt(String info) throws IOException {
        System.out.println(info);
        log.warningLog(info);
        facade.logAttempt(GSON.fromJson(info, LoginAttempts.class));
    }
}
