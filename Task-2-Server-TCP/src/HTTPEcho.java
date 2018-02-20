import java.net.*;
import java.io.*;

public class HTTPEcho {
    private static ServerSocket serverSocket;
    private static int PORT = 0;
    private static Socket socket = null;

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
        socket = serverSocket.accept();
        socket.setSoTimeout(3000);
        System.out.println("Receiving Message from Client...");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder response = new StringBuilder();

        String outputLine;

        try {
            do {
                outputLine = in.readLine();
                response.append(outputLine);
                response.append("\r\n");
            } while (!outputLine.isEmpty());
            sendResponse(response);
        } catch (SocketTimeoutException e) {
           sendResponse(response);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private static void sendResponse(StringBuilder response) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Writing Message to Client...");
        out.print("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("\r\n");
        out.println(response.toString());
        socket.close();
    }
}
