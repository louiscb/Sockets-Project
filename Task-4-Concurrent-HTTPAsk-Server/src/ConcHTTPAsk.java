import tcpclient.TCPClient;

import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    private static int ServerPort;

    public static void main(String[] args) {
        ServerPort = Integer.parseInt(args[0]);
        ConcHTTPAsk server = new ConcHTTPAsk();
        server.start();
    }

    private void start() {
        try {
            ServerSocket server = new ServerSocket(ServerPort);

            while (true) {
                System.out.println("Waiting for client...");
                Socket socket = server.accept();
                Runnable r = new ClientHandler(socket);
                new Thread(r).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class ClientHandler implements Runnable {
        Socket socket;

        ClientHandler (Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                HTTPAsk();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void HTTPAsk() throws IOException {
            String httpResponse;
            String errorMsg = "Error: ";
            String hostname = null;
            String port = null;
            String string = null;
            boolean error400 = false;
            boolean error404 = false;

            // Get the data from the client
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Handling client..." + socket.toString());
            String line = inFromClient.readLine() + "";
            String header = line;

            while (!line.isEmpty()) {
                line = inFromClient.readLine() + "";
            }

            // the string could miss any of the characters used for splitting
            // which may cause runtime errors
            try {
                String[] request = header.split("/");
                String str1 = request[1];
                String[] res1 = str1.split(" ");
                String str2 = res1[0];
                System.out.println(str2);


                if (str2.contains("ask") && str2.contains("hostname") && str2.contains("port")) {
                    String res3 = str2.substring(4); // get rid of ask
                    String[] para = res3.split("&");
                    String[] para0 = para[0].split("=");
                    String[] para1 = para[1].split("=");

                    hostname = para0[1];
                    port = para1[1];
                    System.out.println(hostname);
                    System.out.println(port);

                    if (str2.contains("string")) {
                        String[] para2 = para[2].split("=");
                        string = para2[1];
                        System.out.println("Request from client: " + string);
                    }
                } else {
                    errorMsg += "The requested method does not exist or parameters are missing";
                    error404 = true;
                }
            } catch (RuntimeException e) {
                errorMsg += "The format of the request is fault";
                error400 = true;
            }

            String TCPClientResponse = null;

            try {
                if (!error400 && !error404)
                    TCPClientResponse = TCPClient.askServer(hostname, Integer.parseInt(port), string);
            } catch (IOException e) {
                errorMsg += "Invalid arguments";
                error400 = true;
            }
            // generate the HTTP response
            if (error400) {
                httpResponse = "HTTP/1.1 400 bad request\r\n\r\n" + errorMsg;
            } else if (error404) {
                httpResponse = "HTTP/1.1 404 not found\r\n\r\n" + errorMsg;
            } else {
                httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + TCPClientResponse;
            }

            PrintWriter outToClient = new PrintWriter(socket.getOutputStream(), true);
            outToClient.println(httpResponse);

            //close connection
            socket.close();
            System.out.println("Connection closed");
        }
    }
}