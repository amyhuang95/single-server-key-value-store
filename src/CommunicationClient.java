import java.io.IOException;

/**
 * This interface defines the methods that a communication client should implement. The client is communicating over
 * either TCP or UDP.
 */
public interface CommunicationClient {

    /**
     * Start the service. Sets timeout to handle server unresponsiveness.
     *
     * @throws IOException when the operation failed
     */
    void start() throws IOException;


    /**
     * Send the provided message to the server.
     *
     * @param message message to be sent to the server.
     * @throws IOException if the operation failed
     */
    void send(String message) throws IOException;


    /**
     * Receive message from the server.
     *
     * @return String of the message from the server
     * @throws IOException if the operation failed
     */
    String receive() throws IOException;

    /**
     * Close the opened connections gracefully.
     *
     * @throws IOException if the operations failed
     */
    void close() throws IOException;


    /**
     * Format the log message to include the client name.
     *
     * @param message message to be logged
     */
    void log(String message);
}
