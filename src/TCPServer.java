import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class represents communication server using TCP protocol.
 */
public class TCPServer implements CommunicationServer {
    private ServerSocket serverSocket;
    private Socket connectionSocket;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * Constructor for a TCP server.
     *
     * @param port port number to listen for requests
     * @throws IOException when failed to create TCP server
     */
    public TCPServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        log("Listening on port: " + port);
    }

    @Override
    public void start() throws IOException {
        connectionSocket = serverSocket.accept();
        log("Connected to client at " + getConnectionAddress());
        in = new DataInputStream(connectionSocket.getInputStream());
        out = new DataOutputStream(connectionSocket.getOutputStream());
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
        if (serverSocket != null) {
            serverSocket.close();
        }
        if (connectionSocket != null) {
            connectionSocket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }

    @Override
    public String getConnectionAddress() {
        return connectionSocket.getInetAddress().getHostAddress() + ":" + connectionSocket.getPort();
    }

    @Override
    public void log(String message) {
        Utils.log("TCP Server", message);
    }

}
