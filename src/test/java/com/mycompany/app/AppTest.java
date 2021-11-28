package com.mycompany.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
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
    public void testSingleTicket() {
    }

    @Test
    public void testAllTickets() throws Exception
    {
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

}
