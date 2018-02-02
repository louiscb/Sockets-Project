package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        StringBuilder s = new StringBuilder();
        try {
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(ToServer);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.setSoTimeout(3000);
            int FirstChar = 0;
            long time = System.currentTimeMillis();
            long end = time+3000;
            System.out.println("END" + end);

            while (FirstChar != -1) {
                System.out.println("START1");
                FirstChar= in.read();
                System.out.println("END1");
                char c = (char)FirstChar;
                s.append(c);

                System.out.println(System.currentTimeMillis());

                if (System.currentTimeMillis() > end) {
                    System.out.println("hello");
                    break;
                }
            }

            return s.toString();
        } catch (SocketTimeoutException e) {
            return s.toString();
        }
    }

    public static String askServer(String hostname, int port) throws  IOException {
        return "asdf";
    }
}

