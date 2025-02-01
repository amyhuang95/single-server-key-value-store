import java.io.IOException;
import java.util.Arrays;

/**
 * This class contains the main code to run the server program, either over TCP or UDP.
 * User must provide the port number and protocol type in the command line in order to execute the program.
 * The server will continue to listen for client's requests until the user manually terminates it.
 * The server manages the key-value pairs using the KVStore class.
 * Once it receives requests from the client, it processes the request, queries data from the key-value store, then
 * sends the result back to the client.
 */
public class Server {
    private static String serviceName = Server.class.getSimpleName();
    private final KVStore store = new KVStore();
    private CommunicationServer server;

    /**
     * Constructor for the server program. Instantiate a TCP or UDP server based on the input.
     * @param port port number to listen on
     * @param protocol protocol type for the communication
     */
    public Server(int port, String protocol) {
        try {
            if (protocol.equals("TCP")) {
                server = new TCPServer(port);
            } else if (protocol.equals("UDP")) {
                server = new UDPServer(port);
            }
        } catch (IOException e) {
            Utils.log(serviceName, "Error starting server.");
        }
    }

    public static void main(String[] args) {
        // Validate command line arguments
        if (args.length != 2) {
            Utils.log(serviceName, "Usage: java Server <port> <protocol>");
            System.exit(1);
            return;
        }
        if (Utils.invalidPort(Integer.parseInt(args[0]))) {
            Utils.log(serviceName, "Port number must be between 1024 - 65535.");
            System.exit(1);
            return;
        }
        if (Utils.invalidProtocol(args[1])) {
            Utils.log(serviceName, "Protocol must be one of TCP, UDP.");
            System.exit(1);
            return;
        }

        int port = Integer.parseInt(args[0]);
        String protocol = args[1].toUpperCase();
        serviceName = protocol + " " + serviceName;

        // Start the service
        Server service = new Server(port, protocol);
        service.run();
    }

    /**
     * Execute the main logic of the server program.
     */
    public void run() {
        // Listening for client request until manually stopped
        while (true) {
            try {
                server.start();
                String data;
                // Continue listen for client requests
                while ((data = server.receive()) != null) {
                    server.log("Received data from " + server.getConnectionAddress() + "\n" + data);
                    // Process client request
                    String response;
                    String requestId = data.split(" ")[0];
                    try {
                        String[] tokens = transformRequest(data);
                        String command = tokens[0];
                        String key = tokens[1];
                        String value = tokens[2];
                        server.log("Tokens" + Arrays.toString(tokens));
                        response = processRequest(command, key, value);
                    } catch (IllegalArgumentException e) {
                        // Send back error message if request is malformed
                        server.log("Received malformed request of length " + data.length() + " from " + server.getConnectionAddress());
                        response = e.getMessage();
                    }
                    // Send back response to client
                    response = requestId + " " + response;
                    server.send(response);
                    server.log("Sent data to " + server.getConnectionAddress() + "\n" + response);
                }
            } catch (IOException e) {
                server.log("Client disconnected");
            }
        }
    }

    /**
     * Helper method to extract the commands from client requests for working with the key-value store.
     * @param request message sent from the client to be processed
     * @return an array of commands following the protocol to communicate with the key-value store
     * @throws IllegalArgumentException if the request is malformed
     */
    private String[] transformRequest(String request) throws IllegalArgumentException {
        String[] tokens = request.split(" ");
        // Validate request conforms with defined protocol
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Invalid request format. Usage: PUT key value | GET key | DELETE key");
        }
        String command = tokens[1].toUpperCase();
        switch (command) {
            case "PUT":
                if (tokens.length == 4) {
                    return new String[]{command, tokens[2], tokens[3]};
                }
                throw new IllegalArgumentException("Invalid request format. Usage: PUT key value");
            case "GET":
                if (tokens.length == 3) {
                    return new String[]{command, tokens[2], null};
                }
                throw new IllegalArgumentException("Invalid request format. Usage: GET key");
            case "DELETE":
                if (tokens.length == 3) {
                    return new String[]{command, tokens[2], null};
                }
                throw new IllegalArgumentException("Invalid request format. Usage: DELETE key");
            default:
                throw new IllegalArgumentException("Invalid command. Usage: PUT key value | GET key | DELETE key");
        }
    }

    /**
     * Helper method to manage the key-value store using the defined protocol: PUT key value | GET key | DELETE key
     * @param cmd either PUT, GET, or DELETE
     * @param key key for the key-value store
     * @param value value associated with the provided key, null if GET/DELETE operations
     * @return
     */
    private String processRequest(String cmd, String key, String value) {
        if (cmd.equals("PUT")) {
            store.put(key, value);
            return "Ok.";
        }
        String val = store.get(key);
        if (val == null) {
            return "Key does not exist";
        }
        if (cmd.equals("GET")) {
            return val;
        } else if (cmd.equals("DELETE")) {
            store.delete(key);
        }
        return "Invalid command";
    }

}
