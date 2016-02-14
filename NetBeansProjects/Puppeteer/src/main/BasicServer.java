/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author alyacarina
 */
public class BasicServer {
    private static final String CONNECTION_ESTABLISHED = 
            "Connection with Server has been established.";
    private static final String DONE = "Connection closed.";
    private static final String IDENTITY = "SERVER: ";
    
    private BufferedReader systemin;
    private boolean run;
    
    public void setUpAndGo(){
        setUpAndGo(0);
    }
    
    public void setUpAndGo(int portNumber){
        try(ServerSocket motherShip = new ServerSocket(portNumber);
            Socket currentClient = motherShip.accept();
            PrintWriter outBound = new PrintWriter(currentClient.getOutputStream(), true);
            BufferedReader inBound = new BufferedReader(
                    new InputStreamReader(currentClient.getInputStream()));){
            
            outBound.println(CONNECTION_ESTABLISHED);
            
            systemin = new BufferedReader(
                    new InputStreamReader(System.in));
            run = true;
            
            Thread serverOutputThread = new Thread() {
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
            serverOutputThread.start();
            
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
    
    public static void main(String[] args) throws IOException{
        BasicServer ps = new BasicServer();
        ps.setUpAndGo(4444);
    }
}
