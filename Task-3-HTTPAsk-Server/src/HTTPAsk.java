import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {
    private static ServerSocket serverSocket;
    private static int PORT = 0;
    private static Socket socket = null;

    public static void main( String[] args) {
        // Your code here
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
        System.out.println("Receiving Message from Client...");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder response = new StringBuilder();

        String outputLine;

        try {
            do {
                outputLine = in.readLine();
                response.append(outputLine);
            } while (!outputLine.isEmpty());

            String[] request = extractRequest(response.toString());

            TCPClient tcpClient = new TCPClient();
            String returnValue = tcpClient.askServer(request[0], Integer.parseInt(request[1]), request[2]);
            sendResponse(returnValue);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private static String[] extractRequest(String response) {
        String[] splitResponse = response.split(" ");
        String parameters = splitResponse[1];
        parameters = parameters.split("/ask?")[1];
        splitResponse = parameters.split("&");
        String hostname = splitResponse[0].split("=")[1];
        String port = splitResponse[1].split("=")[1];
        String msg = null;

        if (splitResponse.length > 2)
             msg = splitResponse[2].split("=")[1];

        System.out.println(hostname);
        System.out.println(port);
        System.out.println(msg);

        String[] returnString = new String[3];
        returnString[0] = hostname;
        returnString[1] = port;
        returnString[2] = msg;

        return returnString;
    }

    private static void sendResponse(String response) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Writing Message to Client...");
        out.print("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("\r\n");
        out.println(response);
        socket.close();
    }
}