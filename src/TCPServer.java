import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class TCPServer {
    private KVStore store = new KVStore();

    public void start(int port) throws IOException {

        // Initialize server socket and connection socket to accept client request
        ServerSocket serverSocket = null;
        Socket connectionSocket = null;

        try {
            // Set up the server socket
            serverSocket = new ServerSocket(port);
            log("Listening on port:" + port);
            // Listen for client requests until manually stopped
            while (true) {
                connectionSocket = serverSocket.accept();
                log("Connected to client at " + connectionSocket.getInetAddress().toString().substring(1));
                // Set up input and output streams
                DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                // Get input from client
                String data;
                while ((data = in.readUTF()) != null) {
                    log("Received request of length " + data.length() + " from " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort() + "\n Data: " + data);
                    // Process client request and send it back
                    try {
                        String output = processRequest(data);
                        out.writeUTF(output);
                    } catch (IllegalArgumentException e) {
                        log("Received malformed request of length " + data.length() + " from " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort());
                        out.writeUTF(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log("Error while listening on port " + port + ": " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (connectionSocket != null) {
                connectionSocket.close();
            }
        }

    }
    /**
     * Helper method to print logs in the terminal with current system timestamp to millisecond precision
     *
     * @param message message to be printed
     */
    private void log(String message) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = df.format(System.currentTimeMillis());
        System.out.println("[Server] " + timestamp + " " + message);
    }

    /**
     * Helper method to process the client request. A valid request should be in the format of PUT key value | GET key | DELETE key'
     *
     * @param request a string of client request
     * @return a string message of the status whether the operation is successful
     * @throws IllegalArgumentException if the request does not comply with the pre-defined format.
     */
    private String processRequest(String request) throws IllegalArgumentException {
        String[] tokens = request.split(" ");
        // Validate request conforms with defined protocol
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Invalid request format. Usage: PUT key value | GET key | DELETE key");
        }
        String command = tokens[0].toUpperCase();
        switch (command) {
            case "PUT":
                if (tokens.length == 3) {
                    store.put(tokens[1], tokens[2]);
                    return "Ok. Added " + tokens[1] + ":" + tokens[2] + " to the store";
                }
                throw new IllegalArgumentException("Invalid request format. Usage: PUT key value");
            case "GET":
                if (tokens.length == 2) {
                    String value = store.get(tokens[1]);
                    if (value != null) {
                        return "Ok. Found " + tokens[1] + ":" + value;
                    }
                    throw new IllegalArgumentException("key does not exist");
                }
                throw new IllegalArgumentException("Invalid request format. Usage: GET key");
            case "DELETE":
                if (tokens.length == 2) {
                    String value = store.get(tokens[1]);
                    if (value != null) {
                        store.delete(tokens[1]);
                        return "Ok. Deleted " + tokens[1] + ":" + value;
                    }
                    throw new IllegalArgumentException("key does not exist");
                }
                throw new IllegalArgumentException("Invalid request format. Usage: DELETE key");
            default:
                throw new IllegalArgumentException("Invalid command. Usage: PUT key value | GET key | DELETE key");
        }

    }

    public static void main(String[] args) throws IOException {
        // Validate that user provided a port number in the command line arguments
        if (args.length < 1) {
            System.out.println("Usage: java TCPServer <port number>");
            System.exit(1);
        }
        int port;

        // Validate input port number
        try {
            port = Integer.parseInt(args[0]);
            if (port < 1024 || port > 65535) {
                throw new IllegalArgumentException("Port number must be between 1024 - 65535.");
            }
        } catch (Exception e) {
            System.out.println("Invalid port number: " + e.getMessage());
            System.exit(1);
            return;
        }
        TCPServer server = new TCPServer();
        server.start(port);
    }
}
