package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        StringBuilder s = new StringBuilder();
        Socket socket = new Socket(hostname, port);

        if (ToServer == null)
            ToServer = "NULL";

        try {
            //timeout in 5 secs
            socket.setSoTimeout(5000);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(ToServer);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int outputChar = 0;

            long timeToStop = System.currentTimeMillis()+5000;

            //If the char read from the bufferreader is -1 it is the end of the transmission
            while (outputChar != -1) {
                outputChar = in.read();
                char c = (char)outputChar;
                s.append(c);

                //Second timeout incase the server continously sends data without stopping
                if (System.currentTimeMillis() > timeToStop)
                    throw new SocketTimeoutException();
            }

            socket.close();
            return s.toString();
        } catch (SocketTimeoutException e) {
            socket.close();
            return s.toString();
        }
    }

    public static String askServer(String hostname, int port) throws  IOException {
        return null;
    }
}

