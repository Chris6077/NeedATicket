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
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
 * @author Marcel Judth
 */
@Path("wallet")
public class WalletResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of WalletResource
     */
    public WalletResource() {
    }

    /**
     * Retrieves representation of an instance of routes.WalletResource
     * @param walletid
     * @param amount
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormParam("walletid") int walletid, @FormParam("amount") double amount) {
        try {
            Logic.uploadWallet(walletid, amount);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null, "successfully uploaded"))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        }
    }
    
        /**
     * Retrieves representation of an instance of routes.WalletResource
     * @param walletid
     * @param amount
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/cashout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cashout(@FormParam("walletid") int walletid, @FormParam("amount") double amount) {
        try {
            Logic.cashout(walletid, amount);
            return Response.status(Response.Status.CREATED).entity(new Gson().toJson(new ResponseObject(null, "wallet successfully uploaded"))).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (FileNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Gson().toJson(new ResponseObject(ex,ex.toString()))).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(new Gson().toJson(new ResponseObject(null, "wallet balance too low"))).build();
        }
    }
    
    /**
     * PUT method for updating or creating an instance of WalletResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
