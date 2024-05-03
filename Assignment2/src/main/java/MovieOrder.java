/*
 **********************************************************************************************************
 * Author:      Md Afsar Uddin Salman
 * Student ID:  12190848
 * File Name:   MovieOrder.java
 * Date:        02-May-2024
 * Purpose:     This file contains the MovieOrder class.
 **********************************************************************************************************
 */
import java.io.Serializable;

public class MovieOrder implements Task, Serializable{
    // private variables
    private double quantity;
    private double price;
    private double tax;
    private double bill;
    
    // parameterized constructor of the class to initialize quantity and unit price
    public MovieOrder(double qty, double price1){
        quantity=qty;
        price=price1;
        tax=0.0;
        bill=0.0;
    }
    
    //Getters and Setters for all the private variables
    public double getQuantity(){
        return quantity;
    }
    
    public double getPrice(){
        return price;
    }
    
    public double getTax(){
        return tax;
    }
    
    public double getTotalBill(){
        return bill;
    }
    
    public void setQuantity(double qty){
        quantity=qty;
    }
    
    public void setPrice(double price1){
        price= price1;
    }
    
    public void setTax(double tax1){
        tax= tax1;
    }
    
    public void setTotalBill(double bill1){
        bill= bill1;
    }
    
    
    // executeTask() implements interface Task's executeTask method and calculates total bill of movie order
    @Override
    public void executeTask(){
        tax=price*quantity*0.3;
        bill=(price*quantity)+tax;
    }
    
    // getResult() implements interface Task's getResult() method and returns the order details of the movie order
    @Override
    public Object getResult(){
        return String.format("Number of Movies: %.2f    Price: %.2f    Tax: %.2f    Bill Total for Movie: %.2f", quantity, price, tax, bill);
    }
}