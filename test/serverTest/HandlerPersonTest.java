package serverTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HandlerPersonTest {

    public HandlerPersonTest() {
    }

    @Before
    public void setUp() throws Exception {
        String[] args = {"127.0.0.1", "8080"};
        server.RestServer.main(args);
    }

    @Test
    public void handleGetAllPersonsTest() throws IOException {
        URLConnection connection = new URL("http://localhost:8080/person").openConnection();
        connection.connect();
        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        System.out.println(response);
        ;
    }
}
