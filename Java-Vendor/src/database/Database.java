/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URLEncoder;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Marcel Judth
 */
public class Database {
    private static final String URL = "https://need-a-ticket-api.herokuapp.com/graphql/";
    private static final Gson GSON = new Gson();
    private static String token = "";

    public static void login(String username, String password) throws Exception {
        Client client = Client.create();
        WebResource resource = client.resource(URL);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, "{\"query\" : \"mutation{login(email:\\\"julian-blaschke@icloud.at\\\", password:\\\"julian\\\")}\"}");

        if (response.getStatus() != 200) {
            throw new Exception("Failed to login!");
        }
        String roomsAsString = response.getEntity(String.class);

        System.out.println(roomsAsString);
    }
}
