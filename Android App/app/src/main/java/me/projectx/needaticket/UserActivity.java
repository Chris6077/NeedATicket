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
import android.widget.ImageView;
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
    private FloatingActionButton fab_user;

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
        this.fab_user = (FloatingActionButton) findViewById(R.id.fab_user);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.fab_user.setOnClickListener(new editUserListener());
    }

    private class editUserListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
           showDiag();
        }
    }

    private void showDiag() {
        final View dialogView = View.inflate(this,R.layout.dialog_edit_user,null);
        final Dialog dialog = new Dialog(this,R.style.UserAlertStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = (ImageView)dialog.findViewById(R.id.closeDialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(dialogView, false, dialog);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){

                    revealShow(dialogView, false, dialog);
                    return true;
                }

                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        int cx = (int) (fab_user.getX() + (fab_user.getWidth()/2));
        int cy = (int) (fab_user.getY())+ fab_user.getHeight() + 56;
        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();
        } else {
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            anim.setDuration(700);
            anim.start();
        }
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