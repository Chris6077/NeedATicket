package me.projectx.needaticket;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import me.projectx.needaticket.adapter.AdapterListViewConcert;
import me.projectx.needaticket.asynctask.TaskGetConcerts;
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

public class ConcertsActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout mdl;
    private ActionBarDrawerToggle toggle;
    private ListView listView_concerts;
    private NavigationView navigation;
    private String uID;
    private View progressView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concerts);
        try {
            this.setViews();
            this.setListener();
            //this.getConcerts();
            this.fillList(new ArrayList<Concert>());
            this.uID = getIntent().getStringExtra("uID");
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

    @Override
    public void onPreExecute(Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostExecute(Object result, Class resource) {
        try{
            swipeRefreshLayout.setRefreshing(false);
            ArrayList<Concert> concerts = (ArrayList<Concert>) result;
            fillList(concerts);
        }catch(Exception error){
            HandlerState.handle(error,getApplicationContext());
        }
    }

    @Override
    public void onRefresh() {
        this.getConcerts();
    }

    //custom
    private void setViews() throws Exception {
        this.mdl = (DrawerLayout) findViewById(R.id.content_concerts_list);
        this.listView_concerts = (ListView) findViewById(R.id.listview_concerts);
        this.navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_view_concerts_swipe_to_refresh_layout);
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

    private void fillList(ArrayList<Concert> concerts) throws Exception {
        fillWithDummy(concerts);
        if(concerts == null) {
            throw new Exception("no Content found");
        } else {
            AdapterListViewConcert adapter = new AdapterListViewConcert(this, uID, R.layout.listview_item_concert, concerts);
            this.listView_concerts.setAdapter(adapter);
        }
    }

    private void fillWithDummy(ArrayList<Concert> concerts){
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
        tickets.add(t1);
        tickets.add(t2);
        tickets.add(t3);
        oe.setTickets(tickets);
        concerts.add(c1);
    }

    private void getConcerts(){
        try {
            TaskGetConcerts get_concerts = new TaskGetConcerts(getString(R.string.webservice_get_concerts_url), uID,this);
            get_concerts.execute();
        } catch(Exception error){
            HandlerState.handle(error,this);
        }
    }
}