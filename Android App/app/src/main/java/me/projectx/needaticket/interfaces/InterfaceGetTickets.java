package me.projectx.needaticket.interfaces;

import java.util.ArrayList;

import me.projectx.needaticket.pojo.Ticket;

public interface InterfaceGetTickets {
    void onPreExecute();

    void onPostExecute(ArrayList<Ticket> tickets);
}
