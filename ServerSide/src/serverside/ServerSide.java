package serverside;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.lang.management.*;

public class ServerSide {

    /**
     * @param args the command line arguments
     Authors: Kevin Poon, Khue Nguyen 
     Tasks:
           get/send current date and time
           get/send uptime
           get/send memory use
           get/send Netstat
           get/send current users
           get/send host running processes
           quit
     */
    
    public static void main(String[] args) {
        //declarations
        
        ServerSocket serverSocket = null;
        ArrayList<ServerThread> threads = new ArrayList<ServerThread>();
        
        //Server socket
        try {
           serverSocket = new ServerSocket(9001);
           System.out.println("Socket created.");
        }
        catch (IOException e) {
           System.out.println(e);
        }
        
        try {
            ServerThread tempThread;
            boolean running = true;
            while (running) {
                running = false;
                // Create Server Thread
                tempThread = new ServerThread(serverSocket.accept());
                tempThread.start();
                threads.add(tempThread);
                
                // Iterate through all threads in ArrayList
                for (int i = 0; i < threads.size(); i++) {
                    tempThread = threads.get(i);
                    // Check if it's done executing
                    if (!tempThread.isAlive()) {
                        // Check if the exit command was sent
                        running = tempThread.isKeepServerGoing();
                        // Remove from ArrayList
                        threads.remove(i);
                    }
                }
                
            }
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }//end main 
  
}//end class ServerSide-----------------------------------------------

