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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author alyacarina
 */
public class MiddleMan {
    int health = 1000;
    Socket client2, client1;
    BufferedReader inbound2;
    PrintWriter outbound2;
    PrintWriter outbound1 = null;
    
    public MiddleMan(){
        client1 = null;
        client2 = null;
    }
    
    public void setUpAndGo(){
        setUpAndGo(0);
    }
    
    public void setUpAndGo(int portNumber){
        try(ServerSocket motherShip = new ServerSocket(portNumber);){
            client1 = motherShip.accept();
            System.out.println("Client 1 is online");
            client2 = motherShip.accept();
            System.out.println("Client 2 is online");
            
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
                String next = br.readLine();
                while(!next.equals("start")){
                    System.out.println("Try again.");
                    next = br.readLine();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            
            Thread thread2 = new Thread(){
                
                @Override
                public void run(){
                    try{
                        inbound2 = new BufferedReader(
                            new InputStreamReader(client2.getInputStream()));
                        outbound2 = new PrintWriter(client2.getOutputStream(), true);
                        
                        outbound2.println("r");
                        outbound2.println("Connected! Welcome.");
                        System.out.println("Client 2 connected.");
                                
                        String next;
                        
                        while((next = inbound2.readLine())!=null){
                            try{
                                int i = Integer.parseInt(next);
                                health+=i;
                                System.out.println(health);
                                if(health<=0 && outbound2!=null){
                                    outbound2.println("You win!");
                                    outbound1.println("You lose!");
                                    System.out.println("2 wins");
                                    return;
                                } else if(health>=2000 && outbound2!=null){
                                    outbound1.println("You win!");
                                    outbound2.println("You lose!");
                                    System.out.println("1 wins");
                                    return;
                                }
                            } catch(Exception e){
                                System.out.println("NaN");
                            }
                            outbound2.println(health);
                            if(outbound1!=null){
                                outbound1.println(health);
                            }
                        }
                        
                        inbound2.close();
                        outbound2.close();
                        client2.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            };
            thread2.start();
            
            try{
                BufferedReader inbound1 = new BufferedReader(
                        new InputStreamReader(client1.getInputStream()));
                outbound1 = new PrintWriter(client1.getOutputStream(), true);
                
                outbound1.println("l");
                outbound1.println("Connected! Welcome.");
                System.out.println("Client 1 connected.");
                
                
                String next;
                        
                while((next = inbound1.readLine())!=null){
                    try{
                        int i = Integer.parseInt(next);
                        health-=i;
                        System.out.println(health);
                        if(health<=0 && outbound2!=null){
                            outbound1.println("You win!");
                            outbound2.println("You lose!");
                            System.out.println("1 wins");
                            return;
                        } else if(health>=2000 && outbound2!=null){
                            outbound2.println("You win!");
                            outbound1.println("You lose!");
                            System.out.println("2 wins");
                            return;
                        }
                    } catch(Exception e){
                        System.out.println("NaN");
                    }
                        outbound1.println(health);
                    if(outbound2!=null){
                        outbound2.println(health);
                    }
                }   
                
                inbound1.close();
                outbound1.close();
                client1.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException{
        MiddleMan ps = new MiddleMan();
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        ps.setUpAndGo(4454);
    }
}

