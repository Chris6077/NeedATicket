package me.projectx.needaticket.interfaces;

import java.util.ArrayList;

import me.projectx.needaticket.pojo.Ticket;

public interface InterfaceGetTickets {
    public void onPreExecute();
    public void onPostExecute(ArrayList<Ticket> tickets);
}
