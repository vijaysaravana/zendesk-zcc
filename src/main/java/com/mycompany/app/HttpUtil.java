package com.mycompany.app;

import java.io.IOException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

	public static List<Tickets> getAllTickets(){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("https://zcczendeskcodingchallenge3908.zendesk.com/api/v2/tickets");
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials("jvijaysaravana@gatech.edu/token", "ggtI8K4r5okepmPzuFXXVF7O84Y0pGNWiroPZLHc"),
		 "UTF-8", false));
		httpGet.addHeader("Content-Type", "application/json");

		HttpResponse httpResponse = null;
		String json = null;
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();  
		try {
			httpResponse = httpClient.execute(httpGet);
			json = EntityUtils.toString(httpResponse.getEntity());

			
			jsonObject = (JSONObject) parser.parse(json); 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray ticketArray = null;
		try {
			ticketArray = (JSONArray) parser.parse(String.valueOf(jsonObject.get("tickets")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Tickets> ticketList = new ArrayList<Tickets>();
		
		for(Object object : ticketArray) {
			try {
				JSONObject jo = (JSONObject) parser.parse(object.toString());
				ObjectMapper mapper = new ObjectMapper();
				Tickets thisTicket = mapper.readValue(jo.toJSONString(), Tickets.class);
				ticketList.add(thisTicket);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ticketList;
	}
	
	public static Tickets getTicketById(String id){
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("https://zcczendeskcodingchallenge3908.zendesk.com/api/v2/tickets/" + id + ".json");
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials("jvijaysaravana@gatech.edu/token", "ggtI8K4r5okepmPzuFXXVF7O84Y0pGNWiroPZLHc"),
		 "UTF-8", false));
		httpGet.addHeader("Content-Type", "application/json");

		HttpResponse httpResponse = null;
		String json = null;
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();  
		ObjectMapper mapper = new ObjectMapper();
		Tickets thisTicket = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode != 200) {
				return null;
			}
			json = EntityUtils.toString(httpResponse.getEntity());
			JSONObject object = (JSONObject) parser.parse(json);
			thisTicket = mapper.readValue(String.valueOf(object.get("ticket")), Tickets.class);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return thisTicket;
	}
	
	public static void printTicket(Tickets ticket) {
		System.out.println("Ticket ID : " + ticket.id + " | " + "Type : " +  ticket.type + " | " + " Subject : " +  ticket.subject + " | " + " Status : " +  ticket.status + " | " + " Priority : " +  ticket.priority + "\n");
	}
	
	
}
