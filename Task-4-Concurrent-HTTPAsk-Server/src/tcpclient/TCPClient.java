package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
        String sentence;
        String modifiedSentence;
        StringBuilder builder = new StringBuilder();
        // set up the connection by creating clientSocket with the passed host name and port
        Socket clientSocket = new Socket(hostname, port);
        // set timeout
        clientSocket.setSoTimeout(5000);
        //set up input and output streams
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        try {
            outToServer.writeBytes(ToServer + '\n');
            int reader = inFromServer.read();
            int i = 10000;
            while (reader != -1 && i > 0) {
                builder.append((char) reader);
                reader = inFromServer.read();
                i--;
            }
            modifiedSentence = builder.toString();
            clientSocket.close();
        }
        catch (SocketTimeoutException SoEX){
            modifiedSentence = builder.toString();
            clientSocket.close();
            return modifiedSentence;
        }
        return modifiedSentence;
    }

    public static String askServer(String hostname, int port) throws  IOException {
        return askServer(hostname, port, "");
    }
}