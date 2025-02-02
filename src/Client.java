import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * This class contains the main code to run the client program over either TCP or UDP.
 * User must provide the hostname/IP address, port number and protocol type in the command line.
 * Once started, the client will pre-populate some data in the key-value store by sending some put requests.
 * Next, the client will do 5 test runs for each of the 3 key-value store operations.
 * Finally, the client will start accepting user inputs from the terminal, and communicate the data with the server
 * until user types 'exit' in the terminal or manually terminates it.
 */
public class Client {
    private static String serviceName = Client.class.getSimpleName();
    private CommunicationClient client;
    private int reqId; // to track unsolicited request

    /**
     * Constructor for the client program. Instantiate a TCP or UDP client based on the input.
     *
     * @param host     hostname or IP address of the client
     * @param port     port number of the client
     * @param protocol protocol to be used, TCP or UDP
     */
    public Client(String host, int port, String protocol) {
        try {
            if (protocol.equals("TCP")) {
                client = new TCPClient(host, port);
            } else if (protocol.equals("UDP")) {
                client = new UDPClient(host, port);
            }
        } catch (IOException e) {
            Utils.log(serviceName, "Error starting client.");
        }
        reqId = 0;
    }

    public static void main(String[] args) {
        // Validate command line arguments
        if (args.length != 3) {
            Utils.log(serviceName, "Usage: java Client <host> <port> <protocol>");
            System.exit(1);
            return;
        }
        if (Utils.invalidPort(Integer.parseInt(args[1]))) {
            Utils.log(serviceName, "Port number must be between 1024 - 65535.");
            System.exit(1);
            return;
        }
        if (Utils.invalidProtocol(args[2])) {
            Utils.log(serviceName, "Protocol must be one of TCP, UDP.");
            System.exit(1);
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String protocol = args[2].toUpperCase();
        serviceName = protocol + " " + serviceName;

        Client service = new Client(host, port, protocol);
        service.run();
    }

    /**
     * Execute the main logic of the client program.
     */
    public void run() {
        try {
            client.start();

            String[] data;
            // Pre-populate keys and values
            client.log("Pre-populating data to key-value store...");
            data = loadData();
            for (String d : data) {
                communicateRequest(d);
            }

            // Run test operations for each operation (PUT, GET, DELETE)
            client.log("Test operations for PUT, GET, and DELETE...");
            data = getTestData();
            for (String d : data) {
                communicateRequest(d);
            }

            // Continue getting user input from terminal until manually stopped
            client.log("Key-Value Store Started...Usage: PUT key value | GET key | DELETE key. Enter \"exit\" to stop.");
            String message;
            while (!(message = getUserInput(new Scanner(System.in))).equalsIgnoreCase("exit")) {
                communicateRequest(message);
            }
            client.close();
        } catch (IOException e) {
            client.log("Error starting server.");
        }
    }

    /**
     * Helper method to send message to server and receive response from the server.
     * Request ID is used to identify unsolicited request.
     *
     * @param message String message to be sent to the server
     */
    private void communicateRequest(String message) {
        try {
            // Add id to request message
            message = "id:" + reqId + " " + message;
            // Send message to server
            client.send(message);
            client.log("Sent to server:\n" + message);
            // Receive response from server
            try {
                String response = client.receive();
                // Validate request id of the response
                if (validRequestId(response)) {
                    client.log("Response from server:\n" + response);
                } else {
                    client.log("Received unsolicited response of length " + response.length() + " from server.");
                }
            } catch (SocketTimeoutException e) {
                client.log("Server response timeout. Moving to next request...");
            }
        } catch (IOException e) {
            client.log("Server connection error. Please restart the client.");
        }
        reqId++;
    }

    /**
     * Helper method to validate that the response contains a request id which match with current request.
     *
     * @param response message to be validated.
     * @return true if the response contains original request id, false otherwise
     */
    private boolean validRequestId(String response) {
        String[] responseParts = response.split(" ");
        return responseParts.length > 1 && responseParts[0].equals("id:" + reqId);
    }

    /**
     * Helper method to pre-populate the Key-Value store with data and a set of keys.
     */
    private String[] loadData() {

        return new String[]{
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
    }


    /**
     * Helper method to generate data to test each operation: 5 PUTs, 5 GETs, 5 DELETEs.
     **/
    private String[] getTestData() {
        return new String[]{
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
    }

    /**
     * Helper method to get user's input from the terminal. Continue to re-prompt user if the provided input is not
     * valid (empty).
     *
     * @param scanner scanner object
     * @return User's input
     */
    private String getUserInput(Scanner scanner) {
        String text = null;
        while (text == null || text.isEmpty()) {
            System.out.print("Enter text: ");
            text = scanner.nextLine();
        }
        return text;
    }
}
