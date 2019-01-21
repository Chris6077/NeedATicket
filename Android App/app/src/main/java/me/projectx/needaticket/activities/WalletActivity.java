package me.projectx.needaticket.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskCashOut;
import me.projectx.needaticket.asynctask.TaskGetWallet;
import me.projectx.needaticket.asynctask.TaskUpload;
import me.projectx.needaticket.customGUI.InputFilterMin;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Wallet;

public class WalletActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Wallet wallet;
    private String uID;
    private TextView balance;
    private EditText amount;
    private Button upload;
    private Button cashOut;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        this.setViews();
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        this.getWallet();
        this.setContent();
    }

    private void getWallet(){
        Wallet w = new Wallet(1, Float.parseFloat("1325.12"));
        wallet = w;
        TaskGetWallet getWallet = new TaskGetWallet(getString(R.string.webservice_get_wallet) + uID, uID,this);
        getWallet.execute();
    }

    private void setContent(){
        amount.setFilters(new InputFilter[]{new InputFilterMin(0)});
        startCountAnimation(wallet.getBalance());
    }

    private void startCountAnimation(float newBalance) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, newBalance);
        animator.setDuration(2000);
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                balance.setText(df.format(animation.getAnimatedValue()));
            }
        });
        animator.start();
    }

    private void setListenerNavigationHeader(){
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void setViews() {
        this.navigation = findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = findViewById(R.id.user_swipe_to_refresh_layout);
        this.balance = findViewById(R.id.textview_balance);
        this.amount = findViewById(R.id.edittext_amount);
        this.cashOut = findViewById(R.id.btCashOut);
        this.upload = findViewById(R.id.btUpload);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.upload.setOnClickListener(new uploadListener());
        this.cashOut.setOnClickListener(new cashOutListener());
    }

    private class uploadListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            upload();
        }
    }

    private void upload(){
        TaskUpload tUpload = new TaskUpload(getString(R.string.webservice_upload), uID, Float.parseFloat(amount.getText().toString()), this);
        tUpload.execute();
    }

    private class cashOutListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            cashOut();
        }
    }

    private void cashOut(){
        TaskCashOut tCashOut = new TaskCashOut(getString(R.string.webservice_cashout), uID, Float.parseFloat(amount.getText().toString()),this);
        tCashOut.execute();
    }

    @Override
    public void onPreExecute(Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostExecute(Object result, Class resource) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            try{
                Float f = Float.parseFloat((String)result);
                startCountAnimation(f);
            }
            catch (Exception ex){
                wallet = new Gson().fromJson((String)result, Wallet.class);
                setContent();
            }
        }
        catch(Exception e){
            HandlerState.handle(e,getApplicationContext());
        }
    }

    @Override
    public void onRefresh() {
        getWallet();
        setContent();
    }
}