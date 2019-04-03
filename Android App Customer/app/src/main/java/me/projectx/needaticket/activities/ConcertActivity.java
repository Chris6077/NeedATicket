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
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

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
import me.projectx.needaticket.pojo.ConcertType;
import me.projectx.needaticket.pojo.Genre;
import me.projectx.needaticket.pojo.Seller;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;
public class ConcertActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    private String uID;
    private String cID;
    private ListView listViewConcertTickets;
    private NavigationView navigation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Concert concert;
    private ArrayList<Ticket> tickets;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        try {
            this.uID = getIntent().getStringExtra("uID");
            this.cID = getIntent().getStringExtra("cID");
            this.setViews();
            this.setListener();
            this.setConcertContent();
            this.getData();
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
        location.setText(getIntent().getStringExtra("cAddress"));
        date.setText(getIntent().getStringExtra("cDate"));
        artist.setText(getIntent().getStringExtra("cArtistName"));
        genre.setText(getIntent().getStringExtra("cGenre"));
        setUpIconCategory(concert.getType());
        header.setText(getIntent().getStringExtra("cTitle"));
    }
    private void fillList () throws ContentException {
        if (tickets == null) {
            throw new ContentException("no Content found");
        } else {
            AdapterListViewConcertTickets adapter = new AdapterListViewConcertTickets(this, uID, cID, R.layout.listview_item_concert_ticket, tickets);
            this.listViewConcertTickets.setAdapter(adapter);
        }
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void setUpIconCategory (ConcertType concertType) {
        ImageView imageviewHeaderImageCategory = findViewById(R.id.category_image_concert_list_item);
        switch (concertType) {
            case CONCERT:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
            case FESTIVAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_festival);
                break;
            case REHEARSAL:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
            default:
                imageviewHeaderImageCategory.setImageResource(R.drawable.category_concert);
                break;
        }
    }
    @Override public void onPreExecute (Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        if(result != null && !result.equals("") && !((String)result).split("\"")[1].equals("errors")) {
            try {
                tickets = new Gson().fromJson(((String) result).substring(26,((String)result).length()-2),new TypeToken<List<Ticket>>(){}.getType());
                fillList();
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        } else {
            HandlerState.handle(getApplicationContext());
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override public void onRefresh () {
        this.getData();
    }
    private void getData () {
        try {
            TaskExecuteGraphQLQuery getTickets = new TaskExecuteGraphQLQuery(getString(R.string.webservice_get_tickets).replace("$cID", cID), uID,this);
            getTickets.execute();
        } catch (Exception error) {
            swipeRefreshLayout.setRefreshing(false);
            HandlerState.handle(error, this);
        }
    }
}