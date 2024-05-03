/*
 **********************************************************************************************************
 * Author:      Md Afsar Uddin Salman
 * Student ID:  12190848
 * File Name:   OrderClient.java
 * Date:        02-May-2024
 * Purpose:     This file contains the OrderClient class, which is used to place orders based on user input.
 **********************************************************************************************************
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

// OrderClient class simulates the order client of the online store
public class OrderClient {
    
    public static void main(String args[]){
        // creating a scanner object for taking inputs
        Scanner inputScanner = new Scanner(System.in);
        try
        { 
            // creating a socket
            Socket socket = new Socket("localhost", 4804);
            // creating input and output stream to cummunicate among the client and the servers
            ObjectOutputStream  outToServerCoordinator = new ObjectOutputStream (socket.getOutputStream());
            ObjectInputStream inFromServerCoordinator = new ObjectInputStream(socket.getInputStream()); 
            

            // Infinite loop to keep the process running
            while(true){
                // display menu
                System.out.println("PLEASE PLACE YOUR ORDER BY SELECTING A NUMBER");
                System.out.println("**********************");
                System.out.println("1) Purchase Book\n2) Purchase Movie\n3) Exit");
                System.out.println("**********************");
                System.out.print("Enter your option: ");
                String inp=inputScanner.nextLine();
                Task task=null;
                double quantity= -1;
                double price= -1;

                if(inp.equals("1") || inp.equals("2")|| inp.equals("3")){

                    if(inp.equals("1") || inp.equals("2")){
                        String item;
                        if(inp.equals("1")){
                            item= "book";
                        }else{
                            item= "movie";
                        }
                        // loop until valid number of books/movies is entered 
                        while(quantity<=0){
                            System.out.print("Enter the number of "+item+"(s): ");
                            quantity=Double.parseDouble(inputScanner.nextLine());
                        
                            // checking valid input
                            if(quantity<=0){
                                System.out.println("Please order atleast one "+ item+".");
                            }
                        }

                        // loop until valid book price is entered
                        while(price<0){
                            System.out.print("Enter the "+ item +" price: ");
                            price=Double.parseDouble(inputScanner.nextLine());
                            
                            // checking valid input
                            if(price<0){
                                System.out.println(item+" Price cannot be negative.");
                            }                           
                        }
                    }

                    // At this point all inputs are valid!
                    // creating an object of MovieOrder
                    if (inp.equals("2")){
                        task=new MovieOrder(quantity,price);
                    }else if (inp.equals("1")){
                        task=new BookOrder(quantity,price);
                    }
                    //Exit option
                    else {
                        //Closing the socket
                        socket.close();
                        //Closing the scanner
                        inputScanner.close();
                        break;
                    }

                    System.out.println("SENDING OBJECT TO SERVER..........");
                    // send request to server coordinator
                    outToServerCoordinator.writeObject(task);
                    outToServerCoordinator.flush();
                    
                    System.out.println("RECEIVING COMPUTED OBJECT FROM SERVER..........");
                    // receive response from server coordinator
                    task=(Task)inFromServerCoordinator.readObject();
                    
                    // display order detail
                    System.out.println(task.getResult());
                }else{
                    //Checking invalid option input
                    System.out.println("Invalid Option! Pick an option from 1, 2 or 3.");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}