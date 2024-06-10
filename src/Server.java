import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Port invalid!");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Asteapta conexiuni client la portul " +
                    serverSocket.getLocalPort() + "...");

            while (true) {

                new ServerThread(serverSocket.accept()).start();

            }
        } catch (IOException e) {
            System.err.println("Conexiune esuata pe portul " + port);
            System.exit(-2);
        }
    }
}
