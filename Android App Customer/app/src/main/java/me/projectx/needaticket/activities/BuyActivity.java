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

import butterknife.BindView;
import butterknife.ButterKnife;
import cdflynn.android.library.checkview.CheckView;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.TicketType;
public class BuyActivity extends AppCompatActivity implements InterfaceTaskDefault {
    private String uID;
    private String sID;
    @BindView(R.id.category_image_ticket_list_item) ImageView imageCategory;
    @BindView(R.id.list_item_ticket_title) TextView header;
    @BindView(R.id.list_item_ticket_seller) TextView seller;
    @BindView(R.id.list_item_ticket_price) TextView price;
    @BindView(R.id.tvPrice) TextView totalPrice;
    @BindView(R.id.tvAmount) TextView amountSelected;
    @BindView(R.id.anchorFade) FrameLayout anchor;
    @BindView(R.id.check) CheckView checker;
    @BindView(R.id.btPurchase) Button btPurchase;
    @BindView(R.id.navigation_drawer) NavigationView navigation;
    private AppCompatActivity cx;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        this.sID = getIntent().getStringExtra("sID");
        this.setContent();
        cx = this;
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
            TaskExecuteGraphQLMutation purchaseTicket = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_purchase_ticket).replace("$cID", getIntent().getStringExtra("cID")).replace("$sID", sID).replace("$price", "" + price).replace("$amount", amountSelected.getText()), uID,this);
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