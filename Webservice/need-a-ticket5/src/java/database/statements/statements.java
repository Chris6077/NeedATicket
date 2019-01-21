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

    //statements related to users
    SELECT_USERS("select * from account"),
    SELECT_USER_BY_EMAIL ("select * from account where email LIKE ?"),
    SElECT_USER_BY_ID ("select * from account where id = ?"),
    INSERT_USER ("insert into account values (?,?,?,?,1)"),    
    
    //statements realted to wallets
    INSERT_WALLET("insert into wallet values(null,?)"),
    SELECT_WALLET_BY_ID ("select * from wallet where id = ?"),
    UPDATE_WALLET ("update wallet set balance = ? where id = ?"),
    
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
    UPDATE_CONCERT ("update concert set title = ?, cdate = ?, genre = ?, address = ?, id_artist = ? where id = ?"),
    
    //statements related to tickets
    SElECT_TICKETS ("select * from ticket"),
    SELECT_TICKETS_BY_USERID ("select * from ticket_concert\n" +
        "inner join ticket on ticket.ID = ticket_concert.ID_TICKET\n" +
        "inner join account on ticket.ID_SELLER = account.ID\n" +
        "where account.id = 1 and ticket.ID_BUYER is null"),
    SElECT_TICKET_BY_ID ("select * from ticket where id = ?"),
    INSERT_TICKET ("insert into ticket values (null, ? , ?, ?, null)"),
    INSERT_TICKET_CONCERT ("insert into ticket_concert values (? , ?, ?)"),
    DELETE_TICKET ("delete from ticket where id = ?"),
    UPDATE_TICKET ("update ticket set type = ?, price = ?, id_seller = ?, id_buyer = ? where id = ?"),
    
    //statements related to transactions
    SElECT_TRANSACTIONS ("select * from transaction"),
    SElECT_TRANSACTIONS_BY_WALLET ("select * from transaction where ID_PAYER_WALLET = ?"),
    INSERT_TRANSACTION ("insert into transaction values (null, ? , ?, ?, ?, ?)");

    private final String statement;
    
    private statements(String name){
        this.statement = name;
    }
    
    public String getStatement(){
        return this.statement;
    }
    
}
