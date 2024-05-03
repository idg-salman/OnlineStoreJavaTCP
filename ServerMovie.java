/*
 **********************************************************************************************************
 * Author:      Md Afsar Uddin Salman
 * Student ID:  12190848
 * File Name:   ServerMovie.java
 * Date:        02-May-2024
 * Purpose:     This file contains the ServerMovie class, which is used to store Movie order details.
 **********************************************************************************************************
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// ServerMovie class process the Movie orders of the online store
public class ServerMovie {
    public static void main(String args[]) throws IOException{
        // create a server socket for Movie Server
        ServerSocket serverMovieSocket = new ServerSocket(4808);
        // No need to close the socket here as it is closed in OrderClient Class
        
        while (true){ 
            Socket socket = null; 
            try{ 
                // accept incoming server coordinator connection request
                socket = serverMovieSocket.accept(); 
                
                // creating and starting a new thread object for each server coordinator connection
                Thread thread = new ServerMovieHandler(socket); 
                thread.start();
                  
            } 
            catch (Exception e){ 
                socket.close(); 
            } 
        } 
    } 
} 

// ServerMovieHandler class handles the request from each server coordinator
class ServerMovieHandler extends Thread  
{ 
    private static int obj=0; //Keeping count of the number of objects being created
    private Socket socket; 
    private ObjectInputStream inFromServerCoordinator; 
    private ObjectOutputStream outToServerCoordinator; 
    
    // constructor
    public ServerMovieHandler(Socket socket1) throws IOException{ 
    	//initialize instance variable of class
        socket = socket1; 
        inFromServerCoordinator = new ObjectInputStream(socket.getInputStream());
        outToServerCoordinator = new ObjectOutputStream(socket.getOutputStream()); 
    } 
  
    @Override //Overriding Thread class's run method
    public void run()  
    { 
        try { 
            // repeatedly wait for Movie order request from server coordinator
            while (true){               
                // receive movie order request from server coordinator
                MovieOrder order=(MovieOrder)inFromServerCoordinator.readObject();
                
                // increment object number by 1
                ServerMovieHandler.obj++;
                
                // display request received with object number
                System.out.println("ServerMovie Received Movie Object Number: "+ServerMovieHandler.obj);
                
                // process the movie order
                order.executeTask();
                
                // send response back to server coordinator
                outToServerCoordinator.writeObject(order);
                outToServerCoordinator.flush();
                
                // display the acknowledgement message for sending response 
                System.out.println("Computer Total Bill for Movie Order. Sending back to client...");                
            } 
        }              
        catch (Exception e) { 
            System.out.println("Exception encountered: "+e);
        }
    } 
} 