/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

/**
 *
 * @author Julian
 */
public class ResponseObject {
    public Object data;
    public String message;

    public ResponseObject(Object data, String message) {
        this.data = data;
        this.message = message;
    }    
    
}
