package facades;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.*;

public class MultiMediaFacade {

    private final String UPLOAD_FOLDER = "D:/uploadedFiles/";

    public Response makeUpload(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
        if (fileDetail == null || uploadedInputStream == null) {
          return Response.status(400).entity("Invalid data").build();
        }
        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            return Response.status(500)
                    .entity("Can not create destination folder on server")
                    .build();
        }
        String fileLocal = UPLOAD_FOLDER + fileDetail.getFileName();
        try {
            saveFile(uploadedInputStream, fileLocal);
        } catch (IOException e) {
            return Response.status(500).entity("Cannot save").build();
        }
        return Response.status(200).entity("Success uploading file!").build();
    }

    public void saveFile(InputStream in, String filename) throws IOException {
        createFolderIfNotExists(UPLOAD_FOLDER);
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(UPLOAD_FOLDER + filename);
        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    private void createFolderIfNotExists(String folderName) throws SecurityException{
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public File findFile(String filename) {
        File file = new File(UPLOAD_FOLDER + filename);
        return file;
    }
}
