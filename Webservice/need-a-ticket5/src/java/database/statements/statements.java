/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.statements;

/**
 *
 * @author Julian
 */
public enum statements {

    //statements related to artists
    SELECT_ARTISTS ("select * from artist"),
    SELECT_ARTIST_BY_ID ("select * from artist where id = ?"),
    INSERT_ARTIST ("insert into artist values (? ,null)"),
    UPDATE_ARTIST ("update artist set name = ? where id = ?"),
    DELETE_ARTIST ("delete from artist where id = ?"),
    
    //statements related to concerts
    SELECT_CONCERTS ("select * from concert"),
    SELECT_CONCERT_BY_ID ("select * from concert where id = ?"), 
    INSERT_CONCERT ("insert into concert values( null, ? , ? , ? , ? , ? )"),
    UPDATE_CONCERT ("update concert set title = ?, cdate = ?, genre = ?, address = ?, id_artist = ? where id LIKE ?");
    
    private final String statement;
    
    private statements(String name){
        this.statement = name;
    }
    
    public String getStatement(){
        return this.statement;
    }
    
}
