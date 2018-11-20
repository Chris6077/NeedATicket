/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import logic.Logic;
import pojo.ResponseObject;

/**
 * REST Web Service
 *
 * @author Julian
 */
@Path("tickets")
public class TicketsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TicketsResource
     */
    public TicketsResource() {
    }

    /**
     * Retrieves representation of an instance of routes.TicketsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTickets() {
        //TODO return proper representation object
        try{
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getTickets(),null))).build();
        }catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } 
    }

    /**
     * PUT method for updating or creating an instance of TicketsResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
