
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author alyacarina
 */
public class RandomClient extends BasicClient {
    boolean run;
    
    @Override
    public void establishConnection(int portNumber, String hostName){
        try(Socket clientSide = new Socket(hostName, portNumber);
            PrintWriter outBound = new PrintWriter(clientSide.getOutputStream(), true);
            BufferedReader inBound = new BufferedReader(
                    new InputStreamReader(clientSide.getInputStream()));){
            
            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    outBound.println((int) (Math.random() * 50)); 
                }
            }, 0, 80);
            
            
            String next = inBound.readLine();
            
            while(next!=null){
                System.out.println(next);
                next = inBound.readLine();
            }
            
        } catch(IOException e){
            e.printStackTrace();
        } 
    }
    
    public static void main(String[] args) throws IOException {
        RandomClient pc = new RandomClient();
        pc.establishConnection(4454);
    }
}
