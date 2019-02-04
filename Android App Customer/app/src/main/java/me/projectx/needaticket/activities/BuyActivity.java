package me.projectx.needaticket.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cdflynn.android.library.checkview.CheckView;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskPurchaseTicket;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.TicketType;
public class BuyActivity extends AppCompatActivity implements InterfaceTaskDefault {
    private String uID;
    private String tID;
    private ImageView imageCategory;
    private TextView header;
    private TextView seller;
    private TextView price;
    private TextView totalPrice;
    private TextView amountSelected;
    private FrameLayout anchor;
    private CheckView checker;
    private Button btPurchase;
    private NavigationView navigation;
    private AppCompatActivity cx;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        this.setViews();
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        this.tID = getIntent().getStringExtra("tID");
        this.setContent();
        cx = this;
    }
    private void setViews () {
        this.navigation = findViewById(R.id.navigation_drawer);
        this.imageCategory = findViewById(R.id.category_image_ticket_list_item);
        this.header = findViewById(R.id.list_item_ticket_title);
        this.seller = findViewById(R.id.list_item_ticket_seller);
        this.price = findViewById(R.id.list_item_ticket_price);
        this.totalPrice = findViewById(R.id.tvPrice);
        this.amountSelected = findViewById(R.id.tvAmount);
        this.btPurchase = findViewById(R.id.btPurchase);
        this.anchor = findViewById(R.id.anchorFade);
        this.checker = findViewById(R.id.check);
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.btPurchase.setOnClickListener(new PurchaseListener());
    }
    private void setContent () {
        header.setText(getIntent().getStringExtra("ticketTitle"));
        seller.setText(getIntent().getStringExtra("sellerName"));
        price.setText(getIntent().getStringExtra("price"));
        amountSelected.setText(getIntent().getStringExtra("amountSelected"));
        totalPrice.setText(String.format("%s", round(Double.parseDouble(price.getText().toString()) * Double.parseDouble(amountSelected.getText().toString()), 2)));
        setUpIconCategory(TicketType.valueOf(getIntent().getStringExtra("ticketType")));
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private double round (double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private void setUpIconCategory (TicketType ticketType) {
        switch (ticketType) {
            case CONCERT:
                imageCategory.setImageResource(R.drawable.category_ticket_concert);
                break;
            case FESTIVAL:
                imageCategory.setImageResource(R.drawable.category_ticket_festival);
                break;
        }
    }
    private void purchase () {
        try {
            TaskPurchaseTicket purchaseTicket = new TaskPurchaseTicket(getString(R.string.webservice_purchase_ticket), tID, uID, Integer.parseInt(amountSelected.getText().toString()), this);
            purchaseTicket.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    @Override public void onPreExecute (Class resource) {
        // Maybe implement a loading screen in the future
    }
    @Override public void onPostExecute (Object result, Class resource) {
        try {
            anchor.setVisibility(View.GONE);
            checker.check();
            new android.os.Handler().postDelayed(new Runnable() {
                public void run () {
                    Intent concertActivity = new Intent(cx, ConcertActivity.class);
                    concertActivity.putExtra("uID", uID);
                    concertActivity.putExtra("cID", getIntent().getStringExtra("cID"));
                    finish();
                    startActivity(concertActivity);
                }
            }, 1300);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
    private class PurchaseListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            purchase();
        }
    }
}