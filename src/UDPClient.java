import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This class represents communication client using the UDP protocol.
 */
public class UDPClient implements CommunicationClient {
    private final static int TIMEOUT = 5000;
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;
    private DatagramPacket packet;

    /**
     * Constructor for a UDP client.
     *
     * @param host IP address or host name
     * @param port server port to send request
     * @throws IOException when failed to create a UDP client
     */
    public UDPClient(String host, int port) throws IOException {
        ip = InetAddress.getByName(host);
        this.port = port;
        socket = new DatagramSocket();
        log("Connected to " + ip.getHostAddress() + ":" + port);
    }

    @Override
    public void start() throws IOException {
        log("Client started...");
        socket.setSoTimeout(TIMEOUT);
    }

    @Override
    public void send(String message) throws IOException {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, ip, port);
        socket.send(packet);
    }

    @Override
    public String receive() throws IOException {
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public void log(String message) {
        Utils.log("UDP Client", message);
    }
}
