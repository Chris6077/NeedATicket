package me.projectx.needaticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.projectx.needaticket.adapter.AdapterListViewConcertTickets;
import me.projectx.needaticket.adapter.AdapterListViewTicket;
import me.projectx.needaticket.asynctask.TaskGetConcert;
import me.projectx.needaticket.asynctask.TaskGetConcertTickets;
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
import me.projectx.needaticket.pojo.User;
import me.projectx.needaticket.pojo.Wallet;

public class ConcertActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout mdl;
    private ActionBarDrawerToggle toggle;
    private String uID;
    private ListView listView_concert_tickets;
    private NavigationView navigation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Concert concert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            ArrayList<Concert> c = new ArrayList<Concert>();
            c.add(c1);
            Seller oe = new Seller("iiooo", "OETicket@oe.com", new ArrayList<Ticket>());
            Ticket t1 = new Ticket(1, TicketType.CONCERT, "Day 1 Ticket", (float)22.99, oe, null, c);
            Ticket t2 = new Ticket(2, TicketType.CONCERT, "Day 2 Ticket", (float)22.99, oe, null, c);
            Ticket t3 = new Ticket(3, TicketType.FESTIVAL, "Festival Pass", (float)33.99, oe, null, c);
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
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(this.toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getConcert(){
        try{
            Intent intent = getIntent();
            String cID = intent.getStringExtra("cID");
            TaskGetConcert getConcert = new TaskGetConcert(getString(R.string.webservice_get_concert_url) + cID, uID,this);
            getConcert.execute();
        }catch(Exception error){
            HandlerState.handle(error,this);
        }
    }

    @Override
    public void onPreExecute(Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostExecute(Object result, Class resource) {
        try{
            swipeRefreshLayout.setRefreshing(false);
            try {
                concert = new Gson().fromJson((String) result, Concert.class);
                setConcertContent(concert);
            }
            catch(Exception e){
                ArrayList<Ticket> tickets = (ArrayList<Ticket>) result;
                fillList(tickets);
            }
        }catch(Exception error){
            HandlerState.handle(error,getApplicationContext());
        }
    }

    private void setConcertContent(Concert concert) {
        TextView header = findViewById(R.id.list_item_concert_name);
        TextView genre = findViewById(R.id.list_item_concert_genre);
        TextView artist = findViewById(R.id.list_item_concert_artist);
        TextView location = findViewById(R.id.list_item_concert_location);
        TextView date = findViewById(R.id.list_item_concert_date);
        String artists = "";
        for(Artist a : concert.getArtists()){
            artists += a.getName() + ", ";
        }
        artists = artists.substring(0, artists.length()-2);
        location.setText(concert.getAddress());
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yy");
        date.setText(dateFormat.format(concert.getDate()));
        artist.setText(artists);
        genre.setText(concert.getGenre().toString());
        setUpIconCategory(concert.getTickets().get(0).getType());
        header.setText(concert.getTitle());
    }

    private void setUpIconCategory(TicketType ticketType){
        ImageView imageview_header_image_category = findViewById(R.id.category_image_concert_list_item);
        switch (ticketType){
            case CONCERT:
                imageview_header_image_category.setImageResource(R.drawable.category_concert);
                break;
            case FESTIVAL:
                imageview_header_image_category.setImageResource(R.drawable.category_festival);
                break;
        }
    }

    @Override
    public void onRefresh() {
        this.getTickets();
    }

    private void setViews() {
        setContentView(R.layout.activity_concert);
        this.mdl = findViewById(R.id.content_ticket_list);
        this.listView_concert_tickets = findViewById(R.id.listview_concert_tickets);
        this.navigation = findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = findViewById(R.id.list_view_concert_swipe_to_refresh_layout);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.setListenerNavigationHeader();
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setListenerNavigationHeader(){
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }

    private void fillList(ArrayList<Ticket> tickets) throws Exception {
        if(tickets == null) {
            throw new Exception("no Content found");
        } else {
            AdapterListViewConcertTickets adapter = new AdapterListViewConcertTickets(this, uID, concert.getId(), R.layout.listview_item_concert_ticket, tickets);
            this.listView_concert_tickets.setAdapter(adapter);
        }
    }

    private void getTickets(){
        try {
            TaskGetConcertTickets get_tickets = new TaskGetConcertTickets(getString(R.string.webservice_get_concert_url) + "/" + concert.getId() + "/tickets", uID, concert.getId(),this);
            get_tickets.execute();
        } catch(Exception error){
            swipeRefreshLayout.setRefreshing(false);
            HandlerState.handle(error,this);
        }
    }
}