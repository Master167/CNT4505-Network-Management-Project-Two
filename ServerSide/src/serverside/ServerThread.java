package serverside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class ServerThread extends Thread {
    
    private Socket clientSocket;
    private boolean keepServerGoing;
    
    public ServerThread(Socket socket) {
        this.clientSocket = socket;
        this.keepServerGoing = true;
    }
    
    /**
     * Get the value of keepServerGoing
     *
     * @return the value of keepServerGoing
     */
    public boolean isKeepServerGoing() {
        return keepServerGoing;
    }
    
    public void run() {
        try {
            System.out.println("Client Accepted");
            String command = null;
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //listen for client commands

            while(!clientSocket.isClosed() && ((command = in.readLine()) != null)) {
                //System.out.println(command);

                if (command.equals("exit")) {
                   System.out.println("Exiting.");
                   this.keepServerGoing = false;
                   break;
                }
                else {
                   try {
                      Runtime rt = Runtime.getRuntime();
                      Process pr = rt.exec(command);

                      BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                      String line = null;
                      while ((line = input.readLine()) != null) {
                         System.out.println(line);
                         out.println(line);
                      }

                      out.close();
                      System.out.println("Done with process");         
                      pr.waitFor();
                   } 
                   catch (Exception e) {
                      System.out.println("Runtime Catch");
                      System.out.println(e.toString());
                   }                  
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
