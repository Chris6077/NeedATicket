/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

/**
 *
 * @author Julian
 */
public enum Config {
   
    //Auth
    AUTHORIZATION_HEADER_KEY("Authorization"),
    AUTHORIZATION_HEADER_PREFIX("Bearer"),
    BEARER_TOKEN("uP(m#Y_|33[s&/y4N.ek3:mGp0w^R>=BwkX?w97+WD)ah]V/$r$|E11Odq|3:MQ-"),
    SECRET("need-a-ticket");

    
    private final String value;
    
    private Config(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
}
