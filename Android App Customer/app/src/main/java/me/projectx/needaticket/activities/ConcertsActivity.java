package me.projectx.needaticket.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.adapter.AdapterListViewConcert;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLQuery;
import me.projectx.needaticket.exceptions.ContentException;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Concert;
public class ConcertsActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.listview_concerts) ListView listViewConcerts;
    @BindView(R.id.navigation_drawer) NavigationView navigation;
    private String uID;
    private Toast backToast;
    private long backPressedTime = 0;
    @BindView(R.id.list_view_concerts_swipe_to_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concerts);
        ButterKnife.bind(this);
        try {
            this.uID = getIntent().getStringExtra("uID");
            this.setListener();
            this.getConcerts();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.setListenerNavigationHeader();
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }
    private void fillList (ArrayList<Concert> concerts) throws ContentException {
        if (concerts == null) {
            throw new ContentException("no Content found");
        } else {
            AdapterListViewConcert adapter = new AdapterListViewConcert(this, uID, R.layout.listview_item_concert, concerts);
            this.listViewConcerts.setAdapter(adapter);
        }
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    @Override public void onPreExecute (Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        if(result != null && !result.equals("") && !((String)result).split("\"")[1].equals("errors")) {
            try {
                ArrayList<Concert> concerts = new Gson().fromJson(((String) result).substring(20,((String)result).length()-2),new TypeToken<List<Concert>>(){}.getType());
                fillList(concerts);
            } catch (Exception error) {
                HandlerState.handle(error, getApplicationContext());
            }
        } else {
            HandlerState.handle(getApplicationContext());
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override public void onRefresh () {
        this.getConcerts();
    }
    private void getConcerts () {
        try {
            TaskExecuteGraphQLQuery getConcerts = new TaskExecuteGraphQLQuery(getString(R.string.webservice_get_concerts), uID, this);
            getConcerts.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            final Intent loginActivity = new Intent(this, LoginActivity.class);
            try {
                finish();
                startActivity(loginActivity);
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        } else {
            backToast = FancyToast.makeText(getApplicationContext(), "Press back again to log out", Toast.LENGTH_SHORT, FancyToast.INFO, false);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}