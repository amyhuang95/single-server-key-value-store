import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
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

        ServerSocket serverSocket = null;
        Socket connectionSocket = null;

        try {
            // Set up the server socket
            serverSocket = new ServerSocket(port);
            log("Server listening on port: " + port);
            // Listen for client request
            while (true) {
                connectionSocket = serverSocket.accept();
                log("Client connected: " + connectionSocket.getInetAddress());
                DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                // Get input from client
                String data = in.readUTF();
                log("Received query from client at" + connectionSocket.getInetAddress() + ":" + port + " " + data);
                // Prepare response to client
                out.writeUTF("Message from server: " + data);
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
     * @param message message to be printed
     */
    private static void log(String message) {
        System.out.println("[Server]" + System.currentTimeMillis() + " " + message);
    }

}
