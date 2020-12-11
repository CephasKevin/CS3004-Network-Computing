
import java.net.*;
import java.io.*;


public class CarParkServerThread extends Thread {

	
  private Socket mySocket = null;
  private SharedCarParkState mySharedCarParkStateObject;
  private String serverThreadOnWhichMyRequestsAreHandled;
  private double mySharedVariable;
   
  //Setup the thread
  	public CarParkServerThread(Socket mySocket, String ServerThreadName, SharedCarParkState SharedObject) {
	
//	  super(ActionServerThreadName);
	  this.mySocket = mySocket;
	  mySharedCarParkStateObject = SharedObject;
	  serverThreadOnWhichMyRequestsAreHandled = ServerThreadName;
	}

  public void run() {
    try {
      System.out.println(serverThreadOnWhichMyRequestsAreHandled + "initialising.");
      PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
      String inputLine, outputLine;

      while ((inputLine = in.readLine()) != null) {
    	  // Get a lock first
    	  try { 
    		  mySharedCarParkStateObject.acquireLock();  
    		  System.out.println("What the user inputted: " + inputLine);// To see what the client requested on the server
    		  outputLine = mySharedCarParkStateObject.processInput(serverThreadOnWhichMyRequestsAreHandled, inputLine);// Process the request
    		  out.println(outputLine);// The response after processing client request
    		  mySharedCarParkStateObject.releaseLock();  
    	  } 
    	  catch(InterruptedException e) {
    		  System.err.println("Failed to get lock when reading:"+e);
    	  }
      }

       out.close();
       in.close();
       mySocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}