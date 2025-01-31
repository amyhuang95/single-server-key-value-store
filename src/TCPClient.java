import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class TCPClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public void start(String host, int port) throws IOException {
        // Set up client
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(5000); // Set time out
            System.out.println("Connected to host: " + host + " port number: " + port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            log("Key-Value Store Started...Usage: PUT key value | GET key | DELETE key");

            // Pre-populate keys and values
            loadData();

            // Get user input from terminal
            String message;
            while (!(message = getUserInput(new Scanner(System.in))).isEmpty()) {
                sendRequest(message);
            }
        } catch (UnknownHostException e) {
            log("Unknown host: " + host);
        } catch (IOException e) {
            log("IO Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     * Helper method to send message to server and receive response from the server
     * @param message
     * @throws IOException
     * @throws SocketTimeoutException
     */
    private void sendRequest(String message) throws IOException, SocketTimeoutException {
        try {
            out.writeUTF(message);
            log("Sent message to server: " + message);
            // Receive server response
            try {
                String response = in.readUTF();
                log("Received response from server: " + response);
            } catch (SocketTimeoutException e) {
                log("Server timed out");
            }
        } catch (IOException e) {
            log("IO Error: " + e.getMessage());
        }
    }

    /**
     * Pre-populate the Key-Value store with data and a set of keys.
     * @throws IOException
     */
    private void loadData() throws IOException {
        log("Loading data...");
        String[] messages = {
                "put apple red",
                "put banana yellow",
                "put grape purple",
                "put orange orange",
                "put strawberry red",
                "put blueberry blue",
                "put kiwi green",
                "put mango yellow",
                "put watermelon green",
                "put pineapple yellow"
        };
        for (String message : messages) {
            sendRequest(message);
        }
    }

    /**
     * Test each operation: 5 PUTs, 5 GETs, 5 DELETEs.
     * @throws IOException
     */
    private void testOperations() throws IOException {
        String[] requests = {
                "PUT mango yellow",
                "PUT kiwi green",
                "PUT blueberry blue",
                "PUT watermelon green",
                "PUT pineapple yellow",
                "GET apple",
                "GET banana",
                "GET grape",
                "GET orange",
                "GET strawberry",
                "DELETE apple",
                "DELETE banana",
                "DELETE grape",
                "DELETE orange",
                "DELETE strawberry"
        };
        for (String request : requests) {
            sendRequest(request);
        }
    }

    /**
     * Helper method to print logs in the terminal with current system timestamp to millisecond precision
     * @param message message to be printed
     */
    private void log(String message) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = df.format(System.currentTimeMillis());
        System.out.println("[Client] " + timestamp + " " + message);
    }

    private String getUserInput(Scanner scanner) {
        String text = null;
        while (text == null || text.isEmpty() || text.length() > 80) {
            System.out.print("Enter text: ");
            text = scanner.nextLine();
        }
        return text;
    }

    public static void main(String[] args) throws IOException {
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
