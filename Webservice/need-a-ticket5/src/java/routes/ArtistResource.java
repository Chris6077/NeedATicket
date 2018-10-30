/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import database.Database;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import logic.Logic;
import pojo.Artist;
import pojo.ResponseObject;

/**
 * REST Web Service
 *
 * @author Julian
 */
@Path("artists")
public class ArtistResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ArtistResource
     */
    public ArtistResource() {
    }

    /**
     * @return all artists
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtists() {
        try {
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getArtists(),null))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }
    
    /**
     *
     * @param artistid id of the artist to return
     * @return
     */
    @GET
    @Path("/{artistid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtist(@PathParam("artistid") int artistid) {
        try {
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getArtist(artistid),null))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }  
    }
    
    
    /**
     * @param name name of the artist to create
     * @return the response wether the operation was sucessful or not
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArtist(@FormParam("name") String name) {
        try {
            Logic.createArtist(name);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null,"artist successfuly created."))).build();
        } catch (NoSuchFieldException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseObject(ex,ex.toString())).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseObject(ex,ex.toString())).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseObject(ex,ex.toString())).build();
        }
    }
    
    /**
     * PUT method for updating or creating an instance of ArtistResource
     * @param artistid id of the artist to update
     * @param name name of the artist to update
     * @return wether the operation was successful or not
     */
    @PUT
    @Path("/{artistid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArtist(@PathParam("artistid") int artistid, @FormParam("name") String name) {
        try {
            Logic.updateArtist(artistid,name);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(null,"successfully updated"))).build();
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
    @Path("/{artistid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteArtist(@PathParam("artistid") int artistid) {
        try {
            Logic.deleteArtist(artistid);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(null,"successfully deleted"))).build();
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
