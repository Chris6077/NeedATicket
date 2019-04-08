/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import application.data.Concert;
import application.data.Ticket;
import application.data.Wallet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Marcel Judth
 */
public class Database {

    private static final String URL = "https://need-a-ticket-api.herokuapp.com/graphql/";
    private static final Gson GSON = new Gson();
    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjODQ0ZjA0MDQ5MTZjNDBmMDBlMDg0NCIsImVtYWlsIjoianVsaWFuLWJsYXNjaGtlQGljbG91ZC5hdCIsImlhdCI6MTU1Mjk0MzMzMCwiZXhwIjoxNTg0NTAwOTMwfQ.x26zJn-NkP4UgqNLV_FPkumK5aCFZbLdSq_pp_DtFhY";
    private static Client client;
    private static Database database = null;

    public Database() {
        client = Client.create();
    }

    public static Database getInstance() throws Exception {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public void login(String username, String password) throws Exception {
        Client client = Client.create();
        WebResource resource = client.resource(URL);
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, "{\"query\" : \"mutation{login(email:\\\"julian-blaschke@icloud.at\\\", password:\\\"julian\\\")}\"}");

        if (response.getStatus() != 200) {
            throw new Exception("Failed to login!");
        }
        String roomsAsString = response.getEntity(String.class);

        System.out.println(roomsAsString);
    }

    public ArrayList<Ticket> getTickets() throws Exception {
        ClientResponse response = client.resource(URL
        ).queryParam("query", "{tickets{_id,type,price,seller{_id,email,username}concert{_id,title,totalTickets,address,artist{name},genre,date}}}").header("Authorization", "Bearer " + this.token).
                accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new Exception("Failed to load tickets!");
        }
        String roomsAsString = response.getEntity(String.class); //server sends gson.toJson which is a string and we receive it here
        JsonObject obj = new JsonParser().parse(roomsAsString).getAsJsonObject();
        JsonElement data = obj.get("data");
        JsonObject asJsonObject = data.getAsJsonObject();
        JsonElement json_concerts = asJsonObject.get("tickets");
        JsonArray asJsonArray = json_concerts.getAsJsonArray();
        return GSON.fromJson(asJsonArray.toString(), new TypeToken<ArrayList<Ticket>>() {
        }.getType());
    }
    
    public void insert(Ticket ticket) throws Exception{
        Client client = Client.create();
        WebResource resource = client.resource(URL);
        System.out.println("{\"query\" : \"mutation{createTickets(amount:" + ticket.getAmount() + ",type:\\\"" + ticket.getType() +"\\\",price:" + ticket.getPrice() +",concertId:\\\"" + ticket.getConcert().getId() + "\\\"){_id}}\"}");
        ClientResponse response = resource.type(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + this.token).post(ClientResponse.class, "{\"query\" : \"mutation{createTickets(amount:" + ticket.getAmount() + ",type:\\\"" + ticket.getType() +"\\\",price:" + ticket.getPrice() +",concertId:\\\"" + ticket.getConcert().getId() + "\\\"){_id}}\"}");

        if (response.getStatus() != 200) {
            throw new Exception("Failed to insert ticket!");
        }
        String roomsAsString = response.getEntity(String.class);

        System.out.println(roomsAsString);
    }

    /**
     *
     * @return @throws Exception
     */
    public Wallet getWallet() throws Exception {
        ClientResponse response = client.resource(URL
        ).queryParam("query", "{tickets{_id,type,price,seller{_id,email,}concert{_id,title,totalTickets,artists{name},date,genre}}}").header("Authorization", "Bearer " + this.token).
                accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new Exception("Failed to load tickets!");
        }
        String roomsAsString = response.getEntity(String.class); //server sends gson.toJson which is a string and we receive it here
        System.out.println(roomsAsString);

        return new Wallet();
    }

    public ArrayList<Concert> getConcerts() throws Exception {
        ClientResponse response = client.resource(URL
        ).queryParam("query", "query{concerts{_id, title, address, genre, type, date,totalTickets, artist{name}}}").header("Authorization", "Bearer " + this.token).
                accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new Exception("Failed to load concerts!");
        }
        String roomsAsString = response.getEntity(String.class); //server sends gson.toJson which is a string and we receive it here
        JsonObject obj = new JsonParser().parse(roomsAsString).getAsJsonObject();
        JsonElement data = obj.get("data");
        JsonObject asJsonObject = data.getAsJsonObject();
        JsonElement json_concerts = asJsonObject.get("concerts");
        JsonArray asJsonArray = json_concerts.getAsJsonArray();
        return GSON.fromJson(asJsonArray.toString(), new TypeToken<ArrayList<Concert>>() {
        }.getType());
    }
}
