import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;





public class CarParkServer {
  public static void main(String[] args) throws IOException {

	ServerSocket CarParkServerSocket = null;
    boolean listening = true;
    String CarParkServerName = "CarParkServer";
    int CarParkServerNumber = 4545;
    
    //One server to hold and manage the data for the car park.
    Queue<String> TheSharedEntranceQueue = new LinkedList<>();
    String[] SharedCarParkSpaces = {"empty", "empty", "empty", "empty", "empty"};// Initialise the car park with 5 empty parking spaces 

    //Create the shared object in the global scope...
    
    SharedCarParkState ourSharedCarkParkStateObject = new SharedCarParkState(SharedCarParkSpaces, TheSharedEntranceQueue);
        
    // Make the server socket

    try {
    	CarParkServerSocket = new ServerSocket(CarParkServerNumber);
    } catch (IOException e) {
      System.err.println("Could not start " + CarParkServerName + " specified port.");
      System.exit(-1);
    }
    System.out.println(CarParkServerName + " started");

    //Got to do this in the correct order with only four clients!  Can automate this...
    
    while (listening){
      new CarParkServerThread(CarParkServerSocket.accept(), "EntranceClient1", ourSharedCarkParkStateObject).start();
      new CarParkServerThread(CarParkServerSocket.accept(), "ExitClient1", ourSharedCarkParkStateObject).start();
      new CarParkServerThread(CarParkServerSocket.accept(), "EntranceClient2", ourSharedCarkParkStateObject).start();
      new CarParkServerThread(CarParkServerSocket.accept(), "ExitClient2", ourSharedCarkParkStateObject).start();
      System.out.println("New " + CarParkServerName + " thread started.");
    }
    CarParkServerSocket.close();
  }
}