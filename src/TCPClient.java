import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {

        // Validate that user provided a hostname/ip address and a port number in the command line arguments
        if (args.length < 2) {
            System.out.println("Usage:java TCPClient <host name/IP address> <port number>");
            System.exit(1);
        }
        String host = args[0];
        int port;

        // Validate input port number
        try {
            port = Integer.parseInt(args[1]);
            if (port < 1024 || port > 65535) {
                throw new IllegalArgumentException("Port number must be between 1024 - 65535.");
            }
        } catch (Exception e) {
            System.out.println("Invalid port number: " + e.getMessage());
            System.exit(1);
            return;
        }

        // Set up client
        try (Socket socket = new Socket(host, port)) {
            System.out.println("[Client]" + System.currentTimeMillis() + " Connected to host: " + host + " port number: " + port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String message = "Message from client";
            out.writeUTF(message);
            // Receive server response
            String response = in.readUTF();
            System.out.println("Response from server: " + response);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host:" + host);
        } catch (IOException e) {
            System.out.println("IO Error:" + e.getMessage());
        }
    }

}
