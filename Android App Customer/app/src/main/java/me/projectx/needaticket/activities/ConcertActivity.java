package me.projectx.needaticket.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.R;
import me.projectx.needaticket.adapter.AdapterListViewConcertTickets;
import me.projectx.needaticket.asynctask.TaskGetConcert;
import me.projectx.needaticket.asynctask.TaskGetConcertTickets;
import me.projectx.needaticket.exceptions.ContentException;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Artist;
import me.projectx.needaticket.pojo.Concert;
import me.projectx.needaticket.pojo.Genre;
import me.projectx.needaticket.pojo.Seller;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;
public class ConcertActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    private String uID;
    private ListView listViewConcertTickets;
    private NavigationView navigation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Concert concert;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        try {
            this.setViews();
            this.setListener();
            this.uID = getIntent().getStringExtra("uID");
            //this.getConcert();
            //this.getTickets();
            Artist a = new Artist("lol", "Martin Garrix");
            ArrayList<Artist> artists = new ArrayList<>();
            artists.add(a);
            ArrayList<Ticket> tickets = new ArrayList<>(0);
            Concert c1 = new Concert("lol", "We are here", new Date(), "Loliweg 3", artists, Genre.DANCE, tickets);
            ArrayList<Concert> c = new ArrayList<>();
            c.add(c1);
            Seller oe = new Seller("iiooo", "OETicket@oe.com", new ArrayList<Ticket>());
            Ticket t1 = new Ticket(1, TicketType.CONCERT, "Day 1 Ticket", (float) 22.99, oe, null, c);
            Ticket t2 = new Ticket(2, TicketType.CONCERT, "Day 2 Ticket", (float) 22.99, oe, null, c);
            Ticket t3 = new Ticket(3, TicketType.FESTIVAL, "Festival Pass", (float) 33.99, oe, null, c);
            tickets.add(t3);
            tickets.add(t1);
            tickets.add(t2);
            tickets.add(t1);
            tickets.add(t2);
            tickets.add(t3);
            tickets.add(t1);
            tickets.add(t2);
            tickets.add(t3);
            oe.setTickets(tickets);
            concert = c1;
            setConcertContent(c1);
            fillList(tickets);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
    }
    private void setViews () {
        setContentView(R.layout.activity_concert);
        this.listViewConcertTickets = findViewById(R.id.listview_concert_tickets);
        this.navigation = findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = findViewById(R.id.list_view_concert_swipe_to_refresh_layout);
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.setListenerNavigationHeader();
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }
    private void setConcertContent (Concert concert) {
        TextView header = findViewById(R.id.list_item_concert_name);
        TextView genre = findViewById(R.id.list_item_concert_genre);
        TextView artist = findViewById(R.id.list_item_concert_artist);
        TextView location = findViewById(R.id.list_item_concert_location);
        TextView date = findViewById(R.id.list_item_concert_date);
        StringBuilder artists = new StringBuilder();
        for (Artist a : concert.getArtists()) {
            artists.append(a.getName()).append(", ");
        }
        artists = new StringBuilder(artists.substring(0, artists.length() - 2));
        location.setText(concert.getAddress());
        @SuppressLint ("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        date.setText(dateFormat.format(concert.getDate()));
        artist.setText(artists.toString());
        genre.setText(concert.getGenre().toString());
        setUpIconCategory(concert.getTickets().get(0).getType());
        header.setText(concert.getTitle());
    }
    private void fillList (ArrayList<Ticket> tickets) throws ContentException {
        if (tickets == null) {
            throw new ContentException("no Content found");
        } else {
            AdapterListViewConcertTickets adapter = new AdapterListViewConcertTickets(this, uID, concert.getId(), R.layout.listview_item_concert_ticket, tickets);
            this.listViewConcertTickets.setAdapter(adapter);
        }
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void setUpIconCategory (TicketType ticketType) {
        ImageView imageviewHeaderImageCategory = findViewById(R.id.category_image_concert_list_item);
        switch (ticketType) {
            case CONCERT:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
            case FESTIVAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_festival);
                break;
        }
    }
    private void getConcert () {
        try {
            Intent intent = getIntent();
            String cID = intent.getStringExtra("cID");
            TaskGetConcert getConcert = new TaskGetConcert(getString(R.string.webservice_get_concert_url) + cID, uID, this);
            getConcert.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    @Override public void onPreExecute (Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            concert = new Gson().fromJson((String) result, Concert.class);
            setConcertContent(concert);
        } catch (Exception error) {
            try {
                ArrayList<Ticket> tickets = (ArrayList<Ticket>) result;
                fillList(tickets);
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        }
    }
    @Override public void onRefresh () {
        this.getTickets();
    }
    private void getTickets () {
        try {
            TaskGetConcertTickets getTickets = new TaskGetConcertTickets(getString(R.string.webservice_get_concert_url) + "/" + concert.getId() + "/tickets", uID, concert.getId(), this);
            getTickets.execute();
        } catch (Exception error) {
            swipeRefreshLayout.setRefreshing(false);
            HandlerState.handle(error, this);
        }
    }
}