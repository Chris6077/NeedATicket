/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Julian
 */
@Provider
public class BearerTokenFilter implements ContainerRequestFilter{

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private static final String BEARER_TOKEN = "uP(m#Y_|33[s&/y4N.ek3:mGp0w^R>=BwkX?w97+WD)ah]V/$r$|E11Odq|3:MQ-";
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        List<String> header = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
        if (header!= null && header.size() > 0){
            String token = header.get(0);
            token = token.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
            if(token.equals(BEARER_TOKEN))
                return;
        }
        Response respone = Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("invalid api key supplied")
                .build();
        
        requestContext.abortWith(respone);
    
    }
    
    
}
