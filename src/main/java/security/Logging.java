/*
package security;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Logging {

    Logger logger = Logger.getLogger("log");
    boolean append = true;
    FileHandler handler = new FileHandler("/var/www/html/logs.log", append);
    private static Logging instance;

    private Logging() throws IOException {
    }

    public static Logging getLog( ) throws IOException {
        if (instance == null) {
            instance = new Logging();
        }
        return instance;
    }

    public void severeLog (String message) throws IOException {
        logger.addHandler(handler);
        logger.severe(message);
    }

    public void warningLog (String message) throws IOException {
        logger.addHandler(handler);
        logger.warning(message);
    }

    public void infoLog (String message) throws IOException {
        logger.addHandler(handler);
        logger.info(message);
    }
}
*/
