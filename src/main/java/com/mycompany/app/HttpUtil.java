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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HttpUtil {
	
	public static List<Tickets> getAllTickets() throws IOException{
		HttpClient httpClient = new DefaultHttpClient();
		
		Properties prop = ReadProperties.readPropertiesFile("application.properties");
		String username = prop.getProperty("USERNAME");
		String authtoken = prop.getProperty("TOKEN");

		String next_page = null;
		HttpResponse httpResponse = null;
		String json = null;
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		List<Tickets> ticketList = new ArrayList<Tickets>();
		do {
			try {
				String url;
				if(next_page == null || next_page == "null") {
					url = prop.getProperty("ZCC_URL");
				}
				else {
					url = next_page;
				}
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader(BasicScheme.authenticate(
				 new UsernamePasswordCredentials(username, authtoken),
				 "UTF-8", false));
				httpGet.addHeader("Content-Type", "application/json");
				httpResponse = httpClient.execute(httpGet);
				json = EntityUtils.toString(httpResponse.getEntity());
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if(statusCode != 200) {
					System.out.println("There seems to be an error : \n");
					System.out.println(json + "\n");
					return ticketList;
				}			
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
			next_page = (String) jsonObject.get("next_page");
			JSONArray ticketArray = null;
			try {
				ticketArray = (JSONArray) parser.parse(String.valueOf(jsonObject.get("tickets")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
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
		}while(next_page != null && next_page != "null");
		

		
		return ticketList;
	}
	
	public static Tickets getTicketById(String id) throws IOException{
		HttpClient httpClient = new DefaultHttpClient();
		Properties prop = ReadProperties.readPropertiesFile("application.properties");
		String username = prop.getProperty("USERNAME");
		String authtoken = prop.getProperty("TOKEN");
		HttpGet httpGet = new HttpGet(prop.getProperty("ZCC_URL") + id + ".json");
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, authtoken),
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
			json = EntityUtils.toString(httpResponse.getEntity());
			if(statusCode != 200) {
				System.out.println("There seems to be an error : \n");
				System.out.println(json + "\n");
				return null;
			}
			
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
	
	public static void deleteTicketById(String id) throws IOException{
		HttpClient httpClient = new DefaultHttpClient();
		Properties prop = ReadProperties.readPropertiesFile("application.properties");
		String username = prop.getProperty("USERNAME");
		String authtoken = prop.getProperty("TOKEN");
		HttpDelete httpDelete = new HttpDelete(prop.getProperty("ZCC_URL") + id + ".json");
		httpDelete.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, authtoken),
		 "UTF-8", false));
		httpDelete.addHeader("Content-Type", "application/json");

		HttpResponse httpResponse = null;
		String json = null;
		try {
			httpResponse = httpClient.execute(httpDelete);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode != 204) {
				System.out.println("There seems to be an error : \n");
				System.out.println(json + "\n");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void printTicket(Tickets ticket) {
		System.out.println("Ticket ID : " + ticket.id + " | " + "Type : " +  ticket.type + " | " + " Subject : " +  ticket.subject + " | " + " Status : " +  ticket.status + " | " + " Priority : " +  ticket.priority + "\n");
	}
	
	public static Tickets createTicket(String inputJson) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		
		Properties prop = ReadProperties.readPropertiesFile("application.properties");
		String username = prop.getProperty("USERNAME");
		String authtoken = prop.getProperty("TOKEN");
		HttpPost httpPost = new HttpPost(prop.getProperty("ZCC_URL"));
		httpPost.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, authtoken),
		 "UTF-8", false));
		httpPost.addHeader("Content-Type", "application/json");
		
		StringEntity postEntity = new StringEntity(inputJson);
		httpPost.setEntity(postEntity);
		HttpResponse httpResponse = null;
		String json = null;
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		List<Tickets> ticketList = new ArrayList<Tickets>();
		Tickets thisTicket = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			json = EntityUtils.toString(httpResponse.getEntity());
			int statusCode = httpResponse.getStatusLine().getStatusCode();	
			if(statusCode != 201) {
				System.out.println("There seems to be an error : \n");
				System.out.println(json + "\n");
				return null;
			}
			jsonObject = (JSONObject) parser.parse(json); 
			ObjectMapper mapper = new ObjectMapper();
			thisTicket = mapper.readValue(jsonObject.get("ticket").toString(), Tickets.class);
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
	
}
