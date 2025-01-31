import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {
    public static void main(String[] args) { // args give message contents and server hostname
        DatagramSocket aSocket = null;

        // Check if the message, host name and port number are provided as the command-line arguments
        if (args.length < 3) {
            System.out.println(
                    "Usage:java UDPClient <message> <Host name> <Port number>");
            System.exit(1);
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
}