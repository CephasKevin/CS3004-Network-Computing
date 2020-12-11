import java.io.*;
import java.net.*;

public class EntranceClient2 {
    public static void main(String[] args) throws IOException {

        // Set up the socket, in and out variables

        Socket ClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int SocketNumber = 4545;
        String ServerName = "localhost";
        String ClientID = "EntranceClient2";

        try {
        	ClientSocket = new Socket(ServerName, SocketNumber);
            out = new PrintWriter(ClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ SocketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + ClientID + " client and IO connections");
        
        // This is modified as it's the client that speaks first

        while (true) {
            
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(ClientID + " sending " + fromUser + " to CarParkServer");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(ClientID + " received " + fromServer + " from CarParkServer");
        }
            
        
       // Tidy up - not really needed due to true condition in while loop
      //  out.close();
       // in.close();
       // stdIn.close();
       // ActionClientSocket.close();
    }
}
