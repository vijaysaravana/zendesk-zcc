package com.mycompany.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import static org.junit.Assert.*;

public class AppTest
{
	
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testSingleTicket() throws IOException {
    	String Json = "{\n"
    			+ "  \"ticket\": {\n"
    			+ "    \"comment\": {\n"
    			+ "      \"body\": \"The smoke is very colorful.\"\n"
    			+ "    },\n"
    			+ "    \"priority\": \"urgent\",\n"
    			+ "    \"subject\": \"My printer is on fire!\"\n"
    			+ "  }\n"
    			+ "}";
    	Tickets ticket = HttpUtil.createTicket(Json);
    	String ticketId = ticket.getId();
    	
    	Tickets getTicket = HttpUtil.getTicketById(ticketId);
    	assertEquals(ticketId, getTicket.getId());
    	HttpUtil.deleteTicketById(ticketId);
    }

    @Test
    public void testAllTickets() throws Exception
    {
    	List<Tickets> ticketList = HttpUtil.getAllTickets();
    	int listSize = ticketList.size();
		HttpClient httpClient = new DefaultHttpClient();
		Properties prop = ReadProperties.readPropertiesFile("application.properties");
		String username = prop.getProperty("USERNAME");
		String authtoken = prop.getProperty("TOKEN");
		HttpGet httpGet = new HttpGet(prop.getProperty("ZCC_URL"));
		httpGet.addHeader(BasicScheme.authenticate(
		 new UsernamePasswordCredentials(username, authtoken),
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
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		Object count = jsonObject.get("count");
		assertEquals(String.valueOf(count), String.valueOf(listSize));
    }
    
    @Test
    public void testInvalidTicketId() throws Exception{
    	Tickets ticket = HttpUtil.getTicketById("xxx");
    	assertEquals(ticket, null);
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

}
