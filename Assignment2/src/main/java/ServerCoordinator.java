/*
 **********************************************************************************************************
 * Author:      Md Afsar Uddin Salman
 * Student ID:  12190848
 * File Name:   ServerCoordinator.java
 * Date:        02-May-2024
 * Purpose:     This file contains the ServerCoordinator class, which is used to establish connections with BookOrder and Movie Order Servers.
 **********************************************************************************************************
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// ServerCoordinator class simulates the Server of the Online Store
public class ServerCoordinator {
    public static void main(String args[]) throws IOException{
        // create server socket
        ServerSocket serverSocket = new ServerSocket(4804);
        // No need to close the socket here as it is closed in OrderClient Class
        
        while (true){ 
            Socket socket = null;               
            try{ 
            	// accept incoming client connection request
                socket = serverSocket.accept(); 
                // create a new thread object for each client connection
                Thread thread = new ServerCoordinatorHandler(socket); 
                
                // start the thread
                thread.start();
                  
            } 
            catch (Exception e){ 
                e.printStackTrace(); 
            } 
        } 
    } 
} 

// ServerCoordinatorHandler class handles the request from each client
class ServerCoordinatorHandler extends Thread  
{ 
	// static variable for keeping count of the total objects created
    private static int obj=0; 
    final Socket socket;
    final ObjectInputStream inFromClient; 
    final ObjectOutputStream outToClient; 
     
    final Socket bookOrderSocket;
    final ObjectInputStream inFromServerBook; 
    final ObjectOutputStream outToServerBook; 
    
    final Socket movieOrderSocket;
    final ObjectInputStream inFromServerMovie; 
    final ObjectOutputStream outToServerMovie; 
    
    
    // Constructor of class
    public ServerCoordinatorHandler(Socket tsocket) throws IOException{ 
    	// initialize instance variable of class
        socket = tsocket; 
        outToClient = new ObjectOutputStream(socket.getOutputStream());
        inFromClient = new ObjectInputStream(socket.getInputStream());
        
        bookOrderSocket = new Socket(InetAddress.getLocalHost(), 4806);
        outToServerBook = new ObjectOutputStream (bookOrderSocket.getOutputStream());
        inFromServerBook =new ObjectInputStream(bookOrderSocket.getInputStream());
        
        movieOrderSocket = new Socket(InetAddress.getLocalHost(), 4808);
        outToServerMovie = new ObjectOutputStream (movieOrderSocket.getOutputStream());
        inFromServerMovie =new ObjectInputStream(movieOrderSocket.getInputStream());
        
    } 
  
    @Override
    public void run()  
    { 
        try {
        	// repeatedly wait for order request from client
            while (true){
              
            	// receive order request from client
                Task task=(Task)inFromClient.readObject();
                
                // increment object number by 1
                ServerCoordinatorHandler.obj++;
                
                // display request received with object number
                System.out.println("ServerCoordinator Received Client Object Number: "+ServerCoordinatorHandler.obj);
                
                // check if order is for book task
                if(task.getClass().getSimpleName().equals("BookOrder")){
                    // send request to server book
                    System.out.println("Sending to Server for Book ....");
                    outToServerBook.writeObject(task);
                    outToServerBook.flush();
                    
                    // receive response from server book
                    task=(Task)inFromServerBook.readObject();
                }
                else{
                    // send request to server movie
                    System.out.println("Received Client Object. Sending to Server for Movie ....");
                    outToServerMovie.writeObject(task);
                    outToServerMovie.flush();
                    
                    // receive response from server movie
                    task=(Task)inFromServerMovie.readObject();
                }
                
                // send response to client
                outToClient.writeObject(task);
                outToClient.flush();
                
                // display the acknowledgement message for sending response 
                System.out.println("Return Order Back to Original Client ....");             
            } 
        }              
        catch (Exception e) {  
        	System.out.println("Encountered Exception: "+ e);       
        }
    } 
} 