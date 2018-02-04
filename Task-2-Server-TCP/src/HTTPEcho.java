import java.net.*;
import java.io.*;

public class HTTPEcho {
    private static ServerSocket serverSocket;
    private static int PORT = 0;

    public static void main( String[] args) {
        PORT = Integer.parseInt(args[0]);

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Listening on port: " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                System.out.println("Waiting for client...");
                handler();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handler() throws IOException {
        Socket socket = null;
        socket = serverSocket.accept();
        socket.setSoTimeout(3000);
        System.out.println("Receiving Message from Client...");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        StringBuilder response = new StringBuilder();

        int outputChar = 0;

        try {
            while (true) {
                outputChar = in.read();
                char c = (char) outputChar;
                response.append(c);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Writing Message to Client...");
            response.append("\r\n");
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("\r\n");
            out.println(response.toString());
            socket.close();
        }
    }
}

