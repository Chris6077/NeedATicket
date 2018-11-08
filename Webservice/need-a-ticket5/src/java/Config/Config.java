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
    
    SECRET("need-a-ticket");
    
    private final String value;
    
    private Config(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
}
