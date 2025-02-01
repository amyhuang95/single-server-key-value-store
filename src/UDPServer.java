import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This class represents communication server using UDP protocol.
 */
public class UDPServer implements CommunicationServer {
    private DatagramSocket socket;
    private InetAddress lastClientIp;
    private int lastClientPort;
    private DatagramPacket packet;

    /**
     * Constructor for a UDP server.
     *
     * @param port port number to listen for requests
     * @throws IOException when failed to create a UDP server
     */
    public UDPServer(int port) throws IOException {
        socket = new DatagramSocket(port);
        log("Listening on port: " + port);
    }

    @Override
    public void start() throws IOException {
        // No persistent connection
    }

    @Override
    public void send(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet;
        if (lastClientIp != null) {
            packet = new DatagramPacket(buffer, buffer.length, lastClientIp, lastClientPort);
        } else {
            throw new IOException("No destination address specified");
        }
        socket.send(packet);
    }

    @Override
    public String receive() throws IOException {
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        // update dest ip and port
        lastClientIp = packet.getAddress();
        lastClientPort = packet.getPort();
        return new String(packet.getData(), 0, packet.getLength());
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public String getConnectionAddress() {
        return lastClientIp.getHostAddress() + ":" + lastClientPort;
    }

    @Override
    public void log(String message) {
        Utils.log("UDP Server", message);
    }
}
