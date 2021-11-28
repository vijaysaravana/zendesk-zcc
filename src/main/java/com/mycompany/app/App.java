package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Hello world!
 */
public class App
{

    private final String message = "Hello World!";

    public App() {}

    public static void main(String[] args) throws Exception {
        
    	
    	System.out.println("Welcome to ZCC Zendesk ticket viewer! \n");
        
    	String input = "";
    	while(!input.equalsIgnoreCase("quit")) {
        	try {
        		System.out.println("************** Choose your action : **************\n");
        		
        		System.out.println("1. View All tickets \n");
        		System.out.println("2. View Single ticket \n");
        		System.out.println("Type 'quit' to quit the application! \n");
        		
            	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                input = reader.readLine();
                
                if(input.equalsIgnoreCase("1")) {
                	System.out.println("\n\n Viewing all tickets : \n\n");
                	List<Tickets> ticketList = HttpUtil.getAllTickets();
                	if(ticketList.size() > 25) {
                		int count = 0;
                		while(count < ticketList.size()) {
                			if(count%25 == 0 && count != 0) {
                				System.out.println("Enter 'next' to view next page of tickets \n");
                				input = reader.readLine();
                				while(!input.equalsIgnoreCase("next")) {
                					System.out.println("Please type a valid input");
                					input = reader.readLine();
                				}
                			}
                			HttpUtil.printTicket(ticketList.get(count++));
                		}
                	}
                }
                else if(input.equalsIgnoreCase("2")) {
                	System.out.println("\n\n Viewing a single ticket : \n\n");
                	System.out.println("Enter ticket Id : \n");
                	input = reader.readLine();
                	String id = input.toString();
                	while(!id.matches("-?\\d+(.\\d+)?")) {
                		System.out.println("Enter a valid ID \n");
                		input = reader.readLine();
                		id = input.toString();
                	}
                	Tickets ticket = HttpUtil.getTicketById(id);
                	if(ticket == null) {
                		System.out.println("The requested ticket ID was not found, try another ticket ID! ");
                	}
                	else {
                		HttpUtil.printTicket(ticket);
                	}
                }
                else if(input.equalsIgnoreCase("quit")) {
                	System.out.println("\n\n Goodbye! \n\n");
                }
                else {
                	System.out.println("\n\n Invalid action! Please enter a valid action. \n\n");
                }
        	}catch(Exception e) {
        		throw e;
        	}
    	}


    }

    private final String getMessage() {
        return message;
    }

}
