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

            // Test 5 of each operation (PUT, GET, DELETE)
            testOperations();

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
                "put dragon_fruit yellow",
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
                "GET apple", // get existing key
                "GET banana", // get existing key
                "get mango", // get non-existing key
                "PUT mango yellow", // add new key
                "get mango", // get newly added key
                "put kiwi green", // add new key
                "get kiwi green", // wrong command
                "geT kiwi", // get newly added key
                "xxx", // malformed request
                "put", // malformed request
                "get", // malformed request
                "PUT kiwi yellow", // overwrite existing key
                "get kiwi", // get modified key
                "DELETE apple", // delete existing key
                "get apple", // get deleted key
                "DELETE apple", // delete non-existing key
                "DELETE grape", // delete existing key
                "DELETE orange", // delete existing key
                "DELETE kiwi", // delete newly added key
                "PUT watermelon green red", // malformed request
                "PUT lemon yellow", // add new key
                "put watermelon blue", // modified existing key
                "GET strawberry", // get existing key
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
