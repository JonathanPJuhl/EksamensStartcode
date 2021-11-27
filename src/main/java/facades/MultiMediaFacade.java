package facades;

import java.io.*;

public class MultiMediaFacade {

    private final String UPLOAD_FOLDER = "/var/www/html/uploadedFiles/";

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

    private void createFolderIfNotExists(String folderName) throws SecurityException {
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
