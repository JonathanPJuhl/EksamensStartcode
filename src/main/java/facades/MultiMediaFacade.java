//package facades;
//
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import javax.ws.rs.core.Response;
//import java.io.*;
//
//public class MultiMediaFacade {
//
//    private String UPLOAD_FOLDER = "C:/uploadedFiles/";
//
//    public Response makeUpload(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
//        if (fileDetail == null || uploadedInputStream == null) {
//          return Response.status(400).entity("Invalid data").build();
//        }
//        try {
//            createFolderIfNotExists(UPLOAD_FOLDER);
//        } catch (SecurityException se) {
//            return Response.status(500)
//                    .entity("Can not create destination folder on server")
//                    .build();
//        }
//        String fileLocal = UPLOAD_FOLDER + fileDetail.getFileName();
//        try {
//            saveFile(uploadedInputStream, fileLocal);
//        } catch (IOException e) {
//            return Response.status(500).entity("Cannot save").build();
//        }
//        return Response.status(200).entity("Success uploading file!").build();
//    }
//    private void saveFile(InputStream in, String filename) throws IOException {
//        OutputStream out = null;
//        int read = 0;
//        byte[] bytes = new byte[1024];
//        out = new FileOutputStream(new File(filename));
//        while ((read = in.read(bytes)) != -1) {
//            out.write(bytes, 0, read);
//        }
//        out.flush();
//        out.close();
//    }
//
//    private void createFolderIfNotExists(String folderName) throws SecurityException{
//        File folder = new File(folderName);
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
//    }
//}
