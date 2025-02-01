import java.text.SimpleDateFormat;

/**
 * This class contains utility methods used across the Server and Client classes.
 */
public class Utils {

    /**
     * Check whether the provided protocol is not valid. Only TCP and UDP are allowed.
     * @param protocol provided protocol
     * @return true if the provided protocol is not valid
     */
    public static boolean invalidProtocol(String protocol) {
        return !protocol.equalsIgnoreCase("UDP") && !protocol.equalsIgnoreCase("TCP");
    }

    /**
     * Check whether the provided port number is not valid. A valid port number should be between 1024 and 65534.
     * @param port provided port number
     * @return true if the provided port number is not valid
     */
    public static boolean invalidPort(int port) {
        return port < 1024 || port > 65535;
    }

    /**
     * Format the log messages in the terminal with time precision to millisecond.
     * @param serviceName the service name to be included at the beginning of the message
     * @param message the message of the log
     */
    public static void log(String serviceName, String message) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = df.format(System.currentTimeMillis());
        System.out.println("[" + serviceName + "] " + timestamp + " " + message);
    }
}
