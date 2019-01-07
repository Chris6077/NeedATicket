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
import android.widget.ImageView;
import android.widget.TextView;

import me.projectx.needaticket.asynctask.TaskChangeEmail;
import me.projectx.needaticket.asynctask.TaskChangePassword;
import me.projectx.needaticket.asynctask.TaskGetConcertTickets;
import me.projectx.needaticket.asynctask.TaskGetUser;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.User;

public class UserActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private String uID;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mdl;
    private TextView email;
    private TextView password;
    private EditText dialog_oldPassword;
    private EditText dialog_newPassword;
    private EditText dialog_confirmNewPassword;
    private EditText dialog_Password;
    private EditText dialog_newEmail;
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
        this.uID = getIntent().getStringExtra("uID");
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
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void setViews() {
        this.navigation = (NavigationView) findViewById(R.id.navigation_drawer);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.user_swipe_to_refresh_layout);
        this.mdl = (DrawerLayout) findViewById(R.id.content_user);
        this.email = (TextView) findViewById(R.id.textview_email);
        this.password = (TextView) findViewById(R.id.textview_password);
        this.boughtTickets = (TextView) findViewById(R.id.textview_bought);
        this.fab_user = (FloatingActionButton) findViewById(R.id.fab_user);
        this.dialog_oldPassword = (EditText) findViewById(R.id.etOldPassword);
        this.dialog_newPassword = (EditText) findViewById(R.id.etNewPassword);
        this.dialog_confirmNewPassword = (EditText) findViewById(R.id.etPasswordConfirm);
        this.dialog_newEmail = (EditText) findViewById(R.id.etEmailAddress);
        this.dialog_Password = (EditText) findViewById(R.id.etPassword);
    }

    private void setListener(){
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.fab_user.setOnClickListener(new editUserListener());
    }

    private class changeEmailListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            changeMail();
        }
    }

    private void changeMail(){
        try {
            TaskChangeEmail change_mail = new TaskChangeEmail(getString(R.string.webservice_change_email) + "/" + user.getId(), user.getId(), dialog_newEmail.getText().toString(), dialog_Password.getText().toString(), this);
            change_mail.execute();
        } catch(Exception error){
            HandlerState.handle(error,this);
        }
    }

    private class changePasswordListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            changePassword();
        }
    }

    private void changePassword(){
        try {
            if(dialog_newPassword != dialog_confirmNewPassword) throw new Exception("Passwords don't match!");
            TaskChangePassword change_password = new TaskChangePassword(getString(R.string.webservice_change_password) + "/" + user.getId(), user.getId(), dialog_oldPassword.getText().toString(), dialog_newPassword.getText().toString(), this);
            change_password.execute();
        } catch(Exception error){
            HandlerState.handle(error,this);
        }
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
        Button changeEmail = (Button)dialog.findViewById(R.id.btChangeEmail);
        changeEmail.setOnClickListener(new changeEmailListener());
        Button changePassword = (Button)dialog.findViewById(R.id.btChangePassword);
        changePassword.setOnClickListener(new changePasswordListener());
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
            TaskGetUser getUser = new TaskGetUser(getString(R.string.webservice_get_user) + uID, uID,this);
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