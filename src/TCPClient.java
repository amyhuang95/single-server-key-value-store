import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class represents communication client using the TCP protocol.
 */

public class TCPClient implements CommunicationClient {
    private final static int TIMEOUT = 5000;
    private Socket socket; // client socket or connection socket of a server
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Constructor for a TCP client. Sets up input and output stream during instantiation.
     *
     * @param host host name or IP address of the client
     * @param port port number to use to communicate with the server
     * @throws IOException when failed to create TCP client
     */
    public TCPClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        log("Connected to " + host + ":" + port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start() throws IOException {
        log("Client started...");
        socket.setSoTimeout(TIMEOUT);
    }

    @Override
    public void send(String message) throws IOException {
        if (out != null) {
            out.writeUTF(message);
        }
    }

    @Override
    public String receive() throws IOException {
        return in.readUTF();
    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }

    @Override
    public void log(String message) {
        Utils.log("TCP Client", message);
    }
}
