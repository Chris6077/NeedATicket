package me.projectx.needaticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import me.projectx.needaticket.asynctask.TaskGetUser;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.User;

public class UserActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mdl;
    private TextView email;
    private TextView password;
    private TextView boughtTickets;
    private NavigationView navigation;
    private FloatingActionButton fab_edit;

    //super
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        this.setViews();
        this.setListener();
        this.getUser();
    }

    //custom
    private void setContent(User user){
        email.setText(user.getEmail());
        boughtTickets.setText(user.getTickets().size());
        password.setText("Strong");
    }
    private void setListenerNavigationHeader(){
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this));
    }
    private void setViews() {
        this.navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.user_swipe_to_refresh_layout);
        this.mdl = (DrawerLayout) findViewById(R.id.content_user);
        this.email = (TextView) findViewById(R.id.textview_email);
        this.password = (TextView) findViewById(R.id.textview_password);
        this.boughtTickets = (TextView) findViewById(R.id.textview_bought);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getUser(){
        try{
            Intent intent = getIntent();
            String uID = intent.getStringExtra("uID");
            TaskGetUser getUser = new TaskGetUser(getString(R.string.webservice_get_user) + uID,this);
            getUser.execute();
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
        swipeRefreshLayout.setRefreshing(false);
        try {
            user = (User) result;
            setContent(user);
        }
        catch(Exception e){
            HandlerState.handle(e,getApplicationContext());
        }
    }

    @Override
    public void onRefresh() {
        getUser();
    }
}