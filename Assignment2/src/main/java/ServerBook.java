/*
 **********************************************************************************************************
 * Author:      Md Afsar Uddin Salman
 * Student ID:  12190848
 * File Name:   ServerCoordinator.java
 * Date:        02-May-2024
 * Purpose:     This file contains the ServerCoordinator class, which is used to store Book order details.
 **********************************************************************************************************
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// ServerBook class process the Book orders of the online store
public class ServerBook {
    public static void main(String args[]) throws IOException{
        // create a server socket for Book Server
        ServerSocket serverBookSocket = new ServerSocket(4806);
        // No need to close the socket here as it is closed in OrderClient Class
        
        while (true){ 
            Socket socket = null; 
            try{ 
                // accept incoming server coordinator connection request
                socket = serverBookSocket.accept(); 
                                
                // create a new thread object for each server coordinator connection
                Thread thread = new ServerBookHandler(socket); 
                thread.start();
                  
            } 
            catch (Exception e){ 
                socket.close(); 
            } 
        } 
    } 
} 

// ServerBookHandler class handles the request from each server coordinator
class ServerBookHandler extends Thread  
{ 
    private static int obj=0; //Keeping count of the number of objects being created
    private Socket socket; 
    private ObjectInputStream inFromServerCoordinator; 
    private ObjectOutputStream outToServerCoordinator; 
    
    // constructor
    public ServerBookHandler(Socket socket1) throws IOException{ 
    	//initialize instance variable of class
        socket = socket1; 
        inFromServerCoordinator = new ObjectInputStream(socket.getInputStream());
        outToServerCoordinator = new ObjectOutputStream(socket.getOutputStream()); 
    } 
  
    @Override
    public void run()  //Overriding Thread class's run method
    { 
        try { 
            // repeatedly wait for Book order request from server coordinator
            while (true){               
                // receive book order request from server coordinator
                BookOrder order=(BookOrder)inFromServerCoordinator.readObject();
                
                // increment object number by 1
                ServerBookHandler.obj++;
                
                // display request received with object number
                System.out.println("ServerBook Received Book Object Number: "+ServerBookHandler.obj);
                
                // process the book order
                order.executeTask();
                
                // send response back to server coordinator
                outToServerCoordinator.writeObject(order);
                outToServerCoordinator.flush();
                
                // display the acknowledgement message for sending response 
                System.out.println("Computer Total Bill for Book Order. Sending back to client...");                
            } 
        }              
        catch (Exception e) {  
            System.out.println("Exception encountered: "+e);
        }
    } 
} 