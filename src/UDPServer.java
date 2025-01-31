import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket aSocket = null;

        // Check if the port number is passed as a command-line argument, exit the program if not.
        if (args.length < 1) {
            System.out.println("Usage: java UDPServer <Port Number>");
            System.exit(1);
        }
        try {
            // Step 1: Socket binding and buffer initialization
            // Get the port number from command-line
            int socket_no = Integer.parseInt(args[0]);
            // Create a socket bound to the port number provided from the argument
            aSocket = new DatagramSocket(socket_no);
            // Initialize a buffer to hold incoming packet data
            byte[] buffer = new byte[1000];

            // Step 2 (main program): Listen for packets continuously
            while (true) {
                // Create a packet with the data to be transferred and its length to be received by the socket
                DatagramPacket request = new DatagramPacket(buffer,
                        buffer.length);
                aSocket.receive(request); // blocks execution until a packet arrives

                // Create a packet using the data from the request and its information, then send it to the socket
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(),
                        request.getPort());
                aSocket.send(reply); // echo back the data
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();