package me.projectx.needaticket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cdflynn.android.library.checkview.CheckView;
import me.projectx.needaticket.asynctask.TaskChangeEmail;
import me.projectx.needaticket.asynctask.TaskChangePassword;
import me.projectx.needaticket.asynctask.TaskGetConcertTickets;
import me.projectx.needaticket.asynctask.TaskGetUser;
import me.projectx.needaticket.asynctask.TaskPurchaseTicket;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.TicketType;
import me.projectx.needaticket.pojo.User;

public class BuyActivity extends AppCompatActivity implements InterfaceTaskDefault{
    private String uID;
    private String tID;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mdl;
    private ImageView imageview_header_image_category;
    private TextView header;
    private TextView seller;
    private TextView price;
    private TextView totalPrice;
    private TextView amount;
    private TextView amountSelected;
    private FrameLayout anchor;
    private CheckView checker;
    private Button btPurchase;
    private NavigationView navigation;

    //super
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        this.setViews();
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        this.tID = getIntent().getStringExtra("tID");
        this.setContent();
    }

    //custom
    private void setContent(){
        header.setText(getIntent().getStringExtra("ticketTitle"));
        seller.setText(getIntent().getStringExtra("sellerName"));
        price.setText(getIntent().getStringExtra("price"));
        amount.setText(getIntent().getStringExtra("amount"));
        amountSelected.setText(getIntent().getStringExtra("amountSelected"));
        totalPrice.setText("" + round(Double.parseDouble(price.getText().toString()) * Double.parseDouble(amountSelected.getText().toString()),2));
        setUpIconCategory(TicketType.valueOf(getIntent().getStringExtra("ticketType")));
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setUpIconCategory(TicketType ticketType){
        switch (ticketType){
            case CONCERT:
                imageview_header_image_category.setImageResource(R.drawable.category_ticket_concert);
                break;
            case FESTIVAL:
                imageview_header_image_category.setImageResource(R.drawable.category_ticket_festival);
                break;
        }
    }
    private void setListenerNavigationHeader(){
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void setViews() {
        this.navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        this.imageview_header_image_category = (ImageView) findViewById(R.id.category_image_ticket_list_item);
        this.mdl = (DrawerLayout) findViewById(R.id.content_buy);
        this.header = (TextView) findViewById(R.id.list_item_ticket_title);
        this.seller = (TextView) findViewById(R.id.list_item_ticket_seller);
        this.price = (TextView) findViewById(R.id.list_item_ticket_price);
        this.totalPrice = (TextView) findViewById(R.id.tvPrice);
        this.amount = (TextView) findViewById(R.id.list_item_ticket_count);
        this.amountSelected = (TextView) findViewById(R.id.tvAmount);
        this.btPurchase = (Button) findViewById(R.id.btPurchase);
        this.anchor = (FrameLayout) findViewById(R.id.anchorFade);
        this.checker = (CheckView) findViewById(R.id.check);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.btPurchase.setOnClickListener(new purchaseListener());
    }

    private class purchaseListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            purchase();
        }
    }

    private void purchase(){
        try{
            TaskPurchaseTicket purchaseTicket = new TaskPurchaseTicket(getString(R.string.webservice_purchase_ticket), tID, uID, Integer.parseInt(amountSelected.getText().toString()), this);
            purchaseTicket.execute();
        }catch(Exception error){
            HandlerState.handle(error,this);
        }
    }

    @Override
    public void onPreExecute(Class resource) {}

    @Override
    public void onPostExecute(Object result, Class resource) {
        try {
            anchor.setVisibility(View.GONE);
            checker.check();
        }
        catch(Exception e){
            HandlerState.handle(e,getApplicationContext());
        }
    }
}