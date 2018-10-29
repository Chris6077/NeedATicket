/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import pojo.Artist;

/**
 * REST Web Service
 *
 * @author Julian
 */
@Path("artist")
public class ArtistResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ArtistResource
     */
    public ArtistResource() {
    }

    /**
     * Retrieves representation of an instance of routes.ArtistResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return new Gson().toJson(new Artist(11,"judth"));
    }

    /**
     * PUT method for updating or creating an instance of ArtistResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
