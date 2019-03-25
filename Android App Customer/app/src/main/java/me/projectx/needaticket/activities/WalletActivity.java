package me.projectx.needaticket.activities;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLQuery;
import me.projectx.needaticket.customgui.InputFilterMin;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Wallet;
public class WalletActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.user_swipe_to_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    private Wallet wallet;
    private String uID;
    @BindView(R.id.textview_balance) TextView balance;
    @BindView(R.id.edittext_amount) EditText amount;
    @BindView(R.id.btUpload) Button upload;
    @BindView(R.id.btCashOut) Button cashOut;
    @BindView(R.id.navigation_drawer) NavigationView navigation;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        this.getWallet();
        this.setContent();
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.upload.setOnClickListener(new UploadListener());
        this.cashOut.setOnClickListener(new CashOutListener());
    }
    private void getWallet () {
        wallet = new Wallet(1, Float.parseFloat("1325.12"));
        TaskExecuteGraphQLQuery<Wallet> getWallet = new TaskExecuteGraphQLQuery<>(getString(R.string.webservice_get_wallet).replace("$uID",uID), uID, this);
        getWallet.execute();
    }
    private void setContent () {
        amount.setFilters(new InputFilter[]{new InputFilterMin(0)});
        startCountAnimation(wallet.getBalance());
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void startCountAnimation (float newBalance) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, newBalance);
        animator.setDuration(2000);
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate (ValueAnimator animation) {
                balance.setText(df.format(animation.getAnimatedValue()));
            }
        });
        animator.start();
    }
    private void upload () {
        TaskExecuteGraphQLMutation tUpload = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_upload).replace("$uID", uID).replace("$amount", amount.getText()), uID, this);
        tUpload.execute();
    }
    private void cashOut () {
        TaskExecuteGraphQLMutation tCashOut = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_cashout).replace("$uID", uID).replace("$amount", amount.getText()), uID, this);
        tCashOut.execute();
    }
    @Override public void onPreExecute (Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            float f = Float.parseFloat((String) result);
            startCountAnimation(f);
        } catch (Exception error) {
            try {
                wallet = new Gson().fromJson((String) result, Wallet.class);
                setContent();
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        }
    }
    @Override public void onRefresh () {
        getWallet();
        setContent();
    }
    private class UploadListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            upload();
        }
    }
    private class CashOutListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            cashOut();
        }
    }
    @Override
    public void onBackPressed() {
        final Intent concertsActivity = new Intent(this, ConcertsActivity.class);
        try {
            finish();
            concertsActivity.putExtra("uID", uID);
            startActivity(concertsActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
}