/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import interfaces.RequiresJWT;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
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
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } 
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketsByUserid() {
        //TODO return proper representation object
        try{
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getTickets(),null))).build();
        }catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } 
    }
    
    @GET
    @Path("/{ticketid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(@PathParam("ticketid") int ticketid) {
        try {
            return Response.status(Response.Status.FOUND).entity(new Gson().toJson(new ResponseObject(Logic.getTicket(ticketid),null))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }  
    }
    
    @RequiresJWT
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(@FormParam("type") String type, @FormParam("price") int price, @FormParam("idSeller") int idSeller, @FormParam("seat") Double seat,@FormParam("concert_id") int concert_id) {
        try {
            Logic.createTicket(type, price, idSeller,seat,concert_id);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null,"ticket successfuly created."))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }
    
    @RequiresJWT
    @POST
    @Path("/{ticketid}/buy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyTicket(@PathParam("ticketid") int ticketid, @FormParam("buyerid") int buyerid, @FormParam("amount") double amount) {
        try {
            Logic.buyTicket(ticketid, buyerid, amount);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null,"ticket successfuly bought."))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }

    @DELETE
    @Path("/{ticketid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTicket(@PathParam("ticketid") int ticketid) {
        try {
            Logic.deleteTicket(ticketid);
            return Response.status(Response.Status.FOUND).entity(new Gson().toJson(new ResponseObject(null,"successfully deleted"))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (IOException ex) {
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
