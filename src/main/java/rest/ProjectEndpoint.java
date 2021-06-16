package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Proj;
import entities.Project;
import facades.ProjectFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("project")
public class ProjectEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static UserFacade facade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll(String proj) {
        Proj project = GSON.fromJson(proj, Proj.class);
        ProjectFacade pF = ProjectFacade.geProjectFacade(EMF);
        pF.addNewProj(project);

        return "{\"msg\":\"Hello anonymous\"}";
    }
}
