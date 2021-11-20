//package rest;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import facades.UserFacade;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//import utils.EMF_Creator;
//
//import javax.annotation.security.RolesAllowed;
//import javax.persistence.EntityManagerFactory;
//import javax.ws.rs.*;
//import javax.ws.rs.core.*;
//import java.io.*;
//
//
//
//@Path("file")
//public class FileEndpoint {
//
//    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
//    private static UserFacade facade = UserFacade.getUserFacade(EMF);
//    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
//
//    @Context
//    private UriInfo context;
//
//    @Context
//    SecurityContext securityContext;
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getInfoForAll() {
//        return "{\"msg\":\"Hello anonymous\"}";
//    }
//
//    @POST
//    @Path("/upload")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadFile(
//            @FormDataParam("file") InputStream uploadedInputStream,
//            @FormDataParam("file") FormDataContentDisposition fileDetail) {
//
//        String uploadedFileLocation = "D://" + fileDetail.getFileName();
//        writeToFile(uploadedInputStream, uploadedFileLocation);
//        String output = "File uploaded to : " + uploadedFileLocation;
//        return Response.status(200).entity(output).build();
//    }
//
//    private void writeToFile(InputStream uploadedInputStream,
//                             String uploadedFileLocation) {
//
//        try {
//            OutputStream out = new FileOutputStream(new File(
//                    uploadedFileLocation));
//            int read = 0;
//            byte[] bytes = new byte[1024];
//
//            out = new FileOutputStream(new File(uploadedFileLocation));
//            while ((read = uploadedInputStream.read(bytes)) != -1) {
//                out.write(bytes, 0, read);
//            }
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}