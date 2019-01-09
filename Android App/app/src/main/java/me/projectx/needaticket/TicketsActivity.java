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

import me.projectx.needaticket.adapter.AdapterListViewTicket;
import me.projectx.needaticket.asynctask.TaskGetMyTickets;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Ticket;

public class TicketsActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout mdl;
    private ActionBarDrawerToggle toggle;
    private ListView listView_tickets;
    private NavigationView navigation;
    private String uID;
    private View progressView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        try {
            this.setViews();
            this.setListener();
            this.getTickets();
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
            ArrayList<Ticket> tickets = (ArrayList<Ticket>) result;
            fillList(tickets);
        }catch(Exception error){
            HandlerState.handle(error,getApplicationContext());
        }
    }

    @Override
    public void onRefresh() {
        this.getTickets();
    }

    private void setViews() {
        this.mdl = findViewById(R.id.content_ticket_list);
        this.listView_tickets = findViewById(R.id.listview_tickets);
        this.navigation = findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = findViewById(R.id.list_view_tickets_swipe_to_refresh_layout);
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
            AdapterListViewTicket adapter = new AdapterListViewTicket(this, uID, R.layout.listview_item_ticket, tickets);
            this.listView_tickets.setAdapter(adapter);
        }
    }

    private void getTickets(){
        try {
            TaskGetMyTickets get_tickets = new TaskGetMyTickets(getString(R.string.webservice_get_my_tickets_url), uID,this);
            get_tickets.execute();
        } catch(Exception error){
            HandlerState.handle(error,this);
        }
    }
}