import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {

    public void start(String host, int port) {
        // Set up client
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to host: " + host + " port number: " + port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            log("Key-Value Store Started...Usage: PUT key value | GET key | DELETE key");
            while (true) {
                String message = getUserInput(new Scanner(System.in));
                out.writeUTF(message);
                log("Sent message to server: " + message);
                // Receive server response
                String response = in.readUTF();
                log("Received response from server: " + response);
            }
        } catch (UnknownHostException e) {
            log("Unknown host: " + host);
        } catch (IOException e) {
            log("IO Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to print logs in the terminal with current system timestamp to millisecond precision
     * @param message message to be printed
     */
    private void log(String message) {
        System.out.println("[Client]" + System.currentTimeMillis() + " " + message);
    }

    private static String getUserInput(Scanner scanner) {
        String text = null;
        while (text == null || text.isEmpty() || text.length() > 80) {
            System.out.print("Enter text: ");
            text = scanner.nextLine();
        }
        scanner.close();
        return text;
    }

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

        TCPClient client = new TCPClient();
        client.start(host, port);
    }
}
