/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import interfaces.RequiresJWT;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import pojo.ResponseObject;

/**
 *
 * @author Julian
 */
@Provider
@RequiresJWT
public class JWTFilter implements ContainerRequestFilter{

    private static final String AUTHORIZATION_HEADER_KEY = "x-access-token";
    private static final String AUTHORIZATION_HEADER_PREFIX = "";
    private static final String BEARER_TOKEN = "uP(m#Y_|33[s&/y4N.ek3:mGp0w^R>=BwkX?w97+WD)ah]V/$r$|E11Odq|3:MQ-";
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        List<String> header = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
        if (header!= null && header.size()  > 0){
            String token = header.get(0);
            try{
                Algorithm algorithm = Algorithm.HMAC256(Config.Config.SECRET.getValue());
                JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                return;    
            }catch(Exception ex){
                Response respone = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(new Gson().toJson(new ResponseObject(null,"invalid token supplied")))
                    .build();

                requestContext.abortWith(respone);
                return;
            }
            
        }
        Response respone = Response
                .status(Response.Status.FORBIDDEN)
                .entity(new Gson().toJson(new ResponseObject(null,"no token supplied")))
                .build();
        
        requestContext.abortWith(respone);
    
    }
    
    
}
