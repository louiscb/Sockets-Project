package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        StringBuilder s = new StringBuilder();

        try {
            Socket socket = new Socket(hostname, port);
            //timeout in 3 secs
            socket.setSoTimeout(3000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(ToServer);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int outputChar = 0;

            //If the char read from the bufferreader is -1 it is the end of the transmission
            while (outputChar != -1) {
                outputChar = in.read();
                char c = (char)outputChar;
                System.out.print(c);
                s.append(c);
            }

            return s.toString();
        } catch (SocketTimeoutException e) {
            return s.toString();
        }
    }

    public static String askServer(String hostname, int port) throws  IOException {
        return null;
    }
}

