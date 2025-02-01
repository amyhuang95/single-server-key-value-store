import java.io.IOException;

/**
 * This interface defines the methods that a communication server should implement. The server is communicating over
 * either TCP or UDP.
 */
public interface CommunicationServer {

    /**
     * Start the service. If the connection is persistent, the server should start listening for client request.
     *
     * @throws IOException when the operation failed
     */
    void start() throws IOException;

    /**
     * Send the provided message to the client.
     *
     * @param message message to be sent to the client.
     * @throws IOException if the operation failed, or no destination address for UDP server
     */
    void send(String message) throws IOException;

    /**
     * Receive message from the client.
     *
     * @return String of the message from the client.
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
     * Get the IP address and port number of the connection to include in the logs.
     *
     * @return a string containing the connections IP address and port number
     */
    String getConnectionAddress();

    /**
     * Format the log message to include the server name.
     *
     * @param message message to be logged
     */
    void log(String message);
}
