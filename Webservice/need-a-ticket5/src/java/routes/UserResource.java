/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("users")
public class UserResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        try {
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getUsers(),null))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } 
    }

    /**
     * Retrieves representation of an instance of routes.UserResource
     * @param email
     * @param password
     * @param type
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@FormParam("email") String email,@FormParam("password") String password,@FormParam("type") String type) {
        try {
            String token = Logic.register(email, password, type);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(token,null))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }
    
    /**
     *
     * @param email
     * @param password
     * @return
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email,@FormParam("password") String password) {
        try {
            String token = Logic.login(email,password);
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(token,null))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (UnsupportedEncodingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of UserResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
