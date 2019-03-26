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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.R;
import me.projectx.needaticket.adapter.AdapterListViewConcertTickets;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLQuery;
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
    private ArrayList<Ticket> tickets;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        try {
            this.setViews();
            this.setListener();
            this.uID = getIntent().getStringExtra("uID");
            //this.getConcert();
            //this.getTickets();
            setConcertContent();
            fillList();
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
    private void setConcertContent () {
        TextView header = findViewById(R.id.list_item_concert_name);
        TextView genre = findViewById(R.id.list_item_concert_genre);
        TextView artist = findViewById(R.id.list_item_concert_artist);
        TextView location = findViewById(R.id.list_item_concert_location);
        TextView date = findViewById(R.id.list_item_concert_date);
        StringBuilder artists = new StringBuilder();
        /*for (Artist a : concert.getArtists()) {
            artists.append(a.getName()).append(", ");
        }
        artists = new StringBuilder(artists.substring(0, artists.length() - 2));*/
        location.setText(concert.getAddress());
        @SuppressLint ("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        date.setText(dateFormat.format(concert.getDate()));
        artist.setText(artists.toString());
        //genre.setText(concert.getGenre().toString());
        //setUpIconCategory(concert.getTickets().get(0).getType());
        header.setText(concert.getTitle());
    }
    private void fillList () throws ContentException {
        if (tickets == null) {
            throw new ContentException("no Content found");
        } else {
            AdapterListViewConcertTickets adapter = new AdapterListViewConcertTickets(this, uID, concert.get_id(), R.layout.listview_item_concert_ticket, tickets);
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
            TaskExecuteGraphQLQuery getConcert = new TaskExecuteGraphQLQuery(getString(R.string.webservice_get_concert_url), uID, this);
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
            setConcertContent();
        } catch (Exception error) {
            try {
                tickets = (ArrayList<Ticket>) result;
                fillList();
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
            TaskExecuteGraphQLQuery getTickets = new TaskExecuteGraphQLQuery(getString(R.string.webservice_get_tickets).replace("$cID", concert.get_id()), uID,this);
            getTickets.execute();
        } catch (Exception error) {
            swipeRefreshLayout.setRefreshing(false);
            HandlerState.handle(error, this);
        }
    }
}