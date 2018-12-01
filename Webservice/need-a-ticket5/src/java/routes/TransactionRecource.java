/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
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
 * @author Marcel Judth
 */
@Path("transactions")
public class TransactionRecource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TransactionRecource
     */
    public TransactionRecource() {
    }

    /**
     * Retrieves representation of an instance of routes.TransactionRecource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactions() {
        try {
            //TODO return proper representation object
            return Response.status(Response.Status.OK).entity(new Gson().toJson(new ResponseObject(Logic.getTransactions(),null))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of TransactionRecource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
