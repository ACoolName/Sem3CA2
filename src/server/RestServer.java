package server;

import auxiliary.RoleSchoolAndPersonId;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import entity.Person;
import entity.RoleSchool;
import exceptions.InvalidRequestException;
import exceptions.InvalidRole;
import exceptions.NotFoundException;
import facades.ServerFacadeDB;
import facades.ServerFacadeMock;
import interfaces.ServerFacade;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestServer {

    static int port = 8080;
    static String ip = "127.0.0.1";
    static String publicFolder = "src/htmlFiles/";
    static String startFile = "index.html";
    static String filesUri = "/pages";
    private static final boolean DEVELOPMENT_MODE = true;
    private ServerFacade facade;
    private Gson gson;
    private HttpServer server;

    public void run() throws IOException {
        server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        //REST Routes
        server.createContext("/person", new HandlerPerson());
        server.createContext("/role", new HandlerRole());
        //HTTP Server Routes
        server.createContext(filesUri, new HandlerFileServer());
        facade = new ServerFacadeDB();
        gson = new Gson();
        server.start();
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 3) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
            publicFolder = args[2];
        }
        new RestServer().run();
    }

    //used for testing
    public HttpServer getServer() {
        return server;
    }

    //method used for testing
    public static RestServer getRestServer(String[] args) throws Exception {
        if (args.length >= 3) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
            publicFolder = args[2];
        }
        return new RestServer();
    }

    public void setFacade(ServerFacade facade) {
        this.facade = facade;
    }

    class HandlerPerson implements HttpHandler {

        public HandlerPerson() {
        }

        @Override
        public void handle(HttpExchange he) throws IOException {
            int status = 200;
            String response = "";
            String method = he.getRequestMethod().toUpperCase();
            switch (method) {
                case "GET":
                    try {
                        response = handleGet(he);
                    } catch (NumberFormatException nfe) {
                        response = "Id is not a number";
                        status = 404;
                    } catch (NotFoundException nfe) {
                        System.out.println("NotFound thrown");
                        response = nfe.getMessage();
                        status = 404;
                    }
                    break;
                case "POST":
                    try {
                        response = handlePost(he);
                    } catch (IllegalArgumentException iae) {
                        status = 400;
                        response = iae.getMessage();
                    } catch (IOException e) {
                        status = 500;
                        response = "Internal Server Problem";
                    }
                    break;
                case "PUT":
                    break;
                case "DELETE":
                    try {
                        response = handleDelete(he);
                    } catch (NotFoundException ex) {
                        response = ex.getMessage();
                        status = 404;
                    } catch (InvalidRequestException ex) {
                        response = ex.getMessage();
                        status = 400;
                    }
                    break;
            }
            if (status == 200) {
                he.getResponseHeaders().add("Content-Type", "application/json");
                System.out.println("content json");
            } else {
                he.getResponseHeaders().add("Content-Type", "text/plain");
                System.out.println("content text");
            }
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private String handleGet(HttpExchange he) throws NotFoundException {
            String response = "";
            String path = he.getRequestURI().getPath();
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex > 0) {  //person/id
                String idStr = path.substring(lastIndex + 1);
                int id = Integer.parseInt(idStr);
                response = facade.getPerson(id);
            } else { // person
                response = facade.getPersons();
            }
            return response;
        }

        private String handlePost(HttpExchange he) throws UnsupportedEncodingException, IOException {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String jsonQuery = br.readLine();
            String response = "";
            if (jsonQuery.contains("<") || jsonQuery.contains(">")) {
                throw new IllegalArgumentException("Illegal characters in input");
            }
            Person p = gson.fromJson(jsonQuery, Person.class);
            if (p.getPhone().length() > 50 || p.getFirstName().length() > 50 || p.getLastName().length() > 70 || p.getEmail().length() > 70) {
                throw new IllegalArgumentException("Input contains to many characters");
            }
            p = facade.addPerson(jsonQuery);
            response = new Gson().toJson(p);
            return response;
        }

        private String handleDelete(HttpExchange he) throws NotFoundException, InvalidRequestException {
            String response = "";
            String path = he.getRequestURI().getPath();
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex > 0) {  //person/id
                int id = Integer.parseInt(path.substring(lastIndex + 1));
                Person pDeleted = facade.deletePerson(id);
                response = new Gson().toJson(pDeleted);
            } else {
                throw new InvalidRequestException("Request is missing ID.");
            }
            return response;
        }

    }

    class HandlerFileServer implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            int responseCode = 500;
            //Set initial error values if an un expected problem occurs
            String errorMsg = null;
            byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
            String mime = null;

            String requestedFile = he.getRequestURI().toString();
            String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
            try {
                String extension = f.substring(f.lastIndexOf("."));
                mime = getMime(extension);
                File file = new File(publicFolder + f);
                System.out.println(publicFolder + f);
                bytesToSend = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
                responseCode = 200;
            } catch (Exception e) {
                responseCode = 404;
                errorMsg = "<h1>404 Not Found</h1>No context found for request";
            }
            if (responseCode == 200) {
                Headers h = he.getResponseHeaders();
                h.set("Content-Type", mime);
            } else {
                bytesToSend = errorMsg.getBytes();
            }
            he.sendResponseHeaders(responseCode, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }

        private String getMime(String extension) {
            String mime = "";
            switch (extension) {
                case ".pdf":
                    mime = "application/pdf";
                    break;
                case ".png":
                    mime = "image/png";
                case ".css":
                    mime = "text/css";
                    break;
                case ".js":
                    mime = "text/javascript";
                    break;
                case ".html":
                    mime = "text/html";
                    break;
                case ".jar":
                    mime = "application/java-archive";
                    break;
            }
            return mime;
        }
    }

    class HandlerRole implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            int status = 200;
            String response = "";
            String method = he.getRequestMethod().toUpperCase();
            switch (method) {
                case "GET":
                    break;
                case "POST":
                    try {
                        response = handlePost(he);
                    } catch (UnsupportedEncodingException ex) {
                        response = ex.getMessage();
                        status = 400;
                    } catch (NotFoundException ex) {
                        response = ex.getMessage();
                        status = 404;
                    } catch (InvalidRole ex) {
                        status = 404;
                        response = ex.getMessage();
                    }
                    break;
                case "PUT":
                    break;
                case "DELETE":
                    break;
            }
            he.getResponseHeaders().add("Content-Type", "application/json");
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private String handlePost(HttpExchange he) throws UnsupportedEncodingException, IOException, NotFoundException, InvalidRole {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String jsonQuery = br.readLine();
            String response = "";
            if (jsonQuery.contains("<") || jsonQuery.contains(">")) {
                throw new IllegalArgumentException("Illegal characters in input");
            }
            RoleSchoolAndPersonId roleAndId = gson.fromJson(jsonQuery, RoleSchoolAndPersonId.class);
            String jsonRole = gson.toJson(roleAndId.getRole());
            RoleSchool role = facade.addRoleToPerson(jsonRole, roleAndId.getPersonId());
            response = gson.toJson(role, RoleSchool.class);
            return response;
        }
    }

}
