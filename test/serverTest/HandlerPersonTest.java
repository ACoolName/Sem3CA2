package serverTest;

import com.google.gson.Gson;
import entity.Person;
import facades.ServerFacadeMock;
import interfaces.ServerFacade;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import server.RestServer;

public class HandlerPersonTest {

    static ServerFacade facade;
    static RestServer restServer;

    public HandlerPersonTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        String[] args = {"127.0.0.1", "8080"};
        restServer = RestServer.getRestServer(args);
        restServer.run();
    }

    @Before
    public void setUp() {
        facade = new ServerFacadeMock();
        restServer.setFacade(facade);
    }

    public void tearDown() {
        restServer.getServer().stop(0);
    }

    @Test
    public void handleGetAllPersonsTestNoPersons() throws IOException {
        URLConnection connection = new URL("http://localhost:8080/person").openConnection();
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        String expected = "[]";
        assertEquals(expected, response);
    }

    @Test
    public void handleGetAllPersonsTestOnePerson() throws IOException, InterruptedException {
        facade.addPerson("{\"firstName\":\"ddd\",\"lastName\":\"ccc\",\"phone\":\"dasd\",\"email\":\"das\"}");
        URLConnection connection = new URL("http://localhost:8080/person").openConnection();
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        String expected = "[{\"id\":0,\"firstName\":\"ddd\",\"lastName\":\"ccc\",\"phone\":\"dasd\",\"email\":\"das\",\"roles\":[]}]";
        assertEquals(expected, response);
    }

    @Test
    public void handleGetAllPersonsTestMultiplePersons() throws IOException, InterruptedException {
        facade.addPerson("{\"firstName\":\"ddd\",\"lastName\":\"ccc\",\"phone\":\"dasd\",\"email\":\"das\"}");
        facade.addPerson("{\"firstName\":\"aaa\",\"lastName\":\"bbb\",\"phone\":\"asd\",\"email\":\"aaa\"}");
        URLConnection connection = new URL("http://localhost:8080/person").openConnection();
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        String expected = "[{\"id\":0,\"firstName\":\"ddd\",\"lastName\":\"ccc\",\"phone\":\"dasd\",\"email\":\"das\",\"roles\":[]},"
                + "{\"id\":1,\"firstName\":\"aaa\",\"lastName\":\"bbb\",\"phone\":\"asd\",\"email\":\"aaa\",\"roles\":[]}]";
        assertEquals(expected, response);
    }
}
