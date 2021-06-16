package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Developer;
import entities.HourRecorderDTO;
import entities.Proj;
import entities.ProjDTO;
import facades.ProjectFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("project")
public class ProjectEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    ProjectFacade pF = ProjectFacade.geProjectFacade(EMF);
    UserFacade uF = UserFacade.getUserFacade(EMF);


    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createProject(String proj) {
        Proj project = GSON.fromJson(proj, Proj.class);

        pF.addNewProj(project);

        return "{\"msg\":\"Hello anonymous\"}";
    }
    @GET
    @Path("all")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllProjects() {
    List<ProjDTO> dtoList = pF.listAsDTO(pF.listOfAllProjects());

        return GSON.toJson(dtoList);
    }
    @PUT
    @Path("assign/{developerandproj}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String addDevToProject(@PathParam("developerandproj") String developerAndProj){
        String[] arr = developerAndProj.split(",");
        String dev = arr[0];
        String proj = arr[1];

        pF.assignDevToProject(dev, proj);
        return "";
    }
    @PUT
    @Path("record")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("developer")
    public String addHoursToProject(String developerProjAndTimeSpent){
       HourRecorderDTO dto = GSON.fromJson(developerProjAndTimeSpent, HourRecorderDTO.class);

       pF.addHoursToProj(dto);
        return "";
    }
}