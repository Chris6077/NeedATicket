package me.projectx.needaticket;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import me.projectx.needaticket.adapter.AdapterListViewTicket;
import me.projectx.needaticket.asynctask.TaskGetMyTickets;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.pojo.Ticket;

import java.util.ArrayList;

public class TicketsActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout mdl;
    private ActionBarDrawerToggle toggle;
    private ListView listView_tickets;
    private NavigationView navigation;
    private View progressView;
    private SwipeRefreshLayout swipeRefreshLayout;

    //super

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        try {
            this.setViews();
            this.setListener();
            this.getTickets();
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

    //custom
    private void setViews() throws Exception {
        this.mdl = (DrawerLayout) findViewById(R.id.content_ticket_list);
        this.listView_tickets = (ListView) findViewById(R.id.listview_tickets);
        this.navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_view_tickets_swipe_to_refresh_layout);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this));
        this.setListenerNavigationHeader();
        this.setActionBarToggle();
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setListenerNavigationHeader(){
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this));
    }

    private void setActionBarToggle(){
        this.toggle = new ActionBarDrawerToggle(this,mdl,R.string.actionbar_open,R.string.actionbar_close);
        this.mdl.addDrawerListener(this.toggle);
        toggle.syncState();
        toggle.setDrawerSlideAnimationEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void fillList(ArrayList<Ticket> tickets) throws Exception {
        if(tickets == null) {
            throw new Exception("no Content found");
        }else {
            AdapterListViewTicket adapter = new AdapterListViewTicket(this, R.layout.listview_item_ticket, tickets);
            this.listView_tickets.setAdapter(adapter);
        }
    }

    private void getTickets(){
        try{
            TaskGetMyTickets get_tickets = new TaskGetMyTickets(getString(R.string.webservice_get_my_tickets_url),this);
            get_tickets.execute();
        }catch(Exception error){
            HandlerState.handle(error,this);
        }
    }
}