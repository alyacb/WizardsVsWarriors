
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author alyacarina
 */
public class BasicClient {
    private static final String CONNECTION_ESTABLISHED = "Client is online.";
    private static final String DONE = "Connection closed.";
    private static final String IDENTITY = "CLIENT: ";
    
    private BufferedReader systemin;
    private boolean run;
    
    public void establishConnection(int portNumber) throws UnknownHostException{
        establishConnection(portNumber, InetAddress.getLocalHost().getHostAddress());
    }
    
    public void establishConnection(int portNumber, String hostName){
        try(Socket clientSide = new Socket(hostName, portNumber);
            PrintWriter outBound = new PrintWriter(clientSide.getOutputStream(), true);
            BufferedReader inBound = new BufferedReader(
                    new InputStreamReader(clientSide.getInputStream()));){
            
            outBound.println(CONNECTION_ESTABLISHED);
            
            BufferedReader systemin = new BufferedReader(
                        new InputStreamReader(System.in));
            run = true;
            
            Thread clientOutputThread = new Thread() {
                @Override
                public void run() {
                    while(run) {
                        try {
                            outBound.println(IDENTITY + systemin.readLine());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };
            clientOutputThread.start();
            
            String next = inBound.readLine();
            
            while(next!=null){
                System.out.println(next);
                next = inBound.readLine();
            }
            
            System.out.println(DONE);
            run = false;
            systemin.close();
            
        } catch(IOException e){
            e.printStackTrace();
        } 
    }
    
    public static void main(String[] args) throws IOException {
        BasicClient pc = new BasicClient();
        pc.establishConnection(4444);
    }
}
