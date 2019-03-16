/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import interfaces.RequiresJWT;
import java.io.FileNotFoundException;
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
@Path("concerts")
public class ConcertsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ConcertsResource
     */
    public ConcertsResource() {
    }

    /** Gets all Concerts
     * @return all Concerts
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcerts() {
        try {
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getConcerts(),null))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }

    /**
     *
     * @param concertid id of the concert to return
     * @return
     */
    @GET
    @Path("/{concertid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcert(@PathParam("concertid") int concertid) {
        try {
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getConcert(concertid),null))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }  
    }
    
    /**
     * @param title title of the concert to create
     * @param date date of which the concert takes place
     * @param address address at which the concert takes place
     * @param genre genre of wich the concert belongs to 
     * @param artistid Id of the leading artist at the conncert
     * @return the response wether the operation was sucessful or not
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createConcert(@FormParam("title") String title, @FormParam("date") String date, @FormParam("genre") String genre, @FormParam("address") String address,@FormParam("artistid") Integer artistid) {
        try {
            Logic.createConcert(title, date, genre, address, artistid);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null,"concert sucessfully created."))).build();
        } catch (NoSuchFieldException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }
    
    /**
     * 
     * @param concertid
     * @param title
     * @param genre
     * @param date
     * @param address
     * @param artistid
     * @return wether the operation was successful or not
     */
    @PUT
    @Path("/{concertid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConcert(@PathParam("concertid") int concertid, @FormParam("title") String title, @FormParam("date") String date, @FormParam("genre") String genre, @FormParam("address") String address,@FormParam("artistid") Integer artistid) {
        try {
            Logic.updateConcert(concertid, title, date, genre, address, artistid);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(null,"concert successfully updated."))).build();
        } catch (NoSuchFieldException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }

    @DELETE
    @Path("/{concertid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConcert(@PathParam("concertid") int concertid, @FormParam("title") String title, @FormParam("date") String date, @FormParam("genre") String genre, @FormParam("address") String address,@FormParam("artistid") Integer artistid) {
        try {
            Logic.updateConcert(concertid, title, date, genre, address, artistid);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(null,"concert successfully updated."))).build();
        } catch (NoSuchFieldException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }


}
