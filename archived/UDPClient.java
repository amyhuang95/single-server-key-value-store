import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPClient {
    private final static int TIMEOUT = 5000;
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;
    private DatagramPacket packet;

    public void start(String host, int port) {
        try {
            ip = InetAddress.getByName(host);
            this.port = port;
            socket = new DatagramSocket();
            log("Connected to " + ip + ":" + port);
            socket.setSoTimeout(TIMEOUT);

            String message;
            while (!(message = Utils.getUserInput(new Scanner(System.in))).isEmpty()) {

                sendRequest(message);
            }

        } catch (IOException e) {
            log("Error connecting to " + ip + ":" + port);
        }

        try {
            // Create a socket and prepare the message to be sent to the server
            aSocket = new DatagramSocket();
            byte[] msg = args[0].getBytes();
            InetAddress aHost = InetAddress.getByName(args[1]);
            int serverPort = Integer.parseInt(args[2]);
            DatagramPacket request =
                    new DatagramPacket(msg, args[0].length(), aHost, serverPort);
            aSocket.send(request);

            // Receive the response from the server
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData(), 0, reply.getLength()));
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    private void send(String message) throws IOException {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, ip, port);
        socket.send(packet);
    }

    private String receive() throws IOException {
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    /**
     * Helper method to print logs in the terminal with current system timestamp to millisecond precision
     * @param message message to be printed
     */
    private void log(String message) {
        Utils.log(getClass().getName(), message);
    }

    public static void main(String[] args) { // args give message contents and server hostname
        DatagramSocket aSocket = null;

        // Check if the message, host name and port number are provided as the command-line arguments
        if (args.length < 3) {
            System.out.println(
                    "Usage:java UDPClient <message> <Host name> <Port number>");
            System.exit(1);
        }

    }
}