import java.net.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class SharedCarParkState{
	
	private SharedCarParkState mySharedObj;
	private String myThreadName;
	private String[] carPark;
	private Queue<String> SharedEntranceQueue = new LinkedList<>();
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	SharedCarParkState(String[] SharedVariable, Queue<String> SharedEntranceQueueIn) {
		carPark = SharedVariable;
		SharedEntranceQueue = SharedEntranceQueueIn;
	}

//Attempt to acquire a lock	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */
	public synchronized String processInput(String myThreadName, String theInput) {
    		System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		// Check what the client said
    		if (theInput.matches("New Car\\:.+")) {
    			//Correct request
    			if (myThreadName.equals("EntranceClient1") || myThreadName.equals("EntranceClient2")) {// EntranceClient1 or 2

    				String carToAdd = theInput.split(":")[1];
    				boolean anyParkingSpace = false;
    				// Check for parking space
    				for(int i = 0; i < carPark.length; i++) {
    					if (carPark[i].equals("empty")) {
    						anyParkingSpace = true;
    						carPark[i] = carToAdd;
    						break;
    					}

    				}
    				
    				if (anyParkingSpace == false) {
//    					System.out.println("----NO PARKING SPACE AVAILABLE--> Added car to entrance queue");
    					theOutput = "----NO PARKING SPACE AVAILABLE--> Added car to entrance queue";
    					SharedEntranceQueue.add(carToAdd);
    				}
    				else {
    					theOutput = "Action completed.  Car Park now = " + Arrays.toString(carPark);
    				}

    				
    			}
       			else {
       				theOutput = myThreadName + " received incorrect request";
       				System.out.println("Error - thread call not recognised.");
       			}
    		}
    		else if(theInput.equalsIgnoreCase("Get Cars Waiting")) {
    			
    			theOutput = "CARS WAITING IN ENTRANCE QUEUE: " + SharedEntranceQueue;
    			
    		}
    		else if(theInput.equalsIgnoreCase("Get Car Park state")) {
    			
    			theOutput = "Current parking state: " + Arrays.toString(carPark);
    			
    		}
    		else if(theInput.matches("Car Left\\:.+") && (myThreadName.equals("ExitClient1") || myThreadName.equals("ExitClient2"))) {// Exit Client 1 or 2
    			
    			String NameOfCarThatLeft = theInput.split(":")[1];//Car Left:Honda Civic
    			
    			boolean doesCarExistInCarPark = false;
    			
    			// Clear one space in the Parking lot
				for(int i = 0; i < carPark.length; i++) {
					if (carPark[i].equals(NameOfCarThatLeft)) {
						
						doesCarExistInCarPark = true;
						
						// Either set the parking space to empty or add the car waiting next in line from the corresponding EntranceClient
						if(SharedEntranceQueue.peek() == null) {// if both queues are empty then set the car park space(that the car left from) to empty
							carPark[i] = "empty";
						}
						else  {// if one the queue is not empty then move the next car waiting in the queue to the car park space
							carPark[i] = SharedEntranceQueue.poll();
						}
						
						break;
					}
				}
				
				System.out.println(myThreadName + " current parking space state " + Arrays.toString(carPark));
				if(doesCarExistInCarPark == true) {theOutput = "Car Left action completed.  NameOfCarThatLeft: " + NameOfCarThatLeft;}
				else {theOutput = "OOPS CANNOT FIND CAR:" + NameOfCarThatLeft;}
				
    			
    		}
    		else { //incorrect request
    			theOutput = myThreadName + " received incorrect request";
    		}
 
     		//Return the output message to the CarParkServer
    		System.out.println(theOutput);
    		return theOutput;
    	}	
}

