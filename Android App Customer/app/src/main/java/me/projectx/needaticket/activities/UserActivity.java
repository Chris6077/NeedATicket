package me.projectx.needaticket.activities;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLQuery;
import me.projectx.needaticket.exceptions.PasswordException;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.Artist;
import me.projectx.needaticket.pojo.Concert;
import me.projectx.needaticket.pojo.Genre;
import me.projectx.needaticket.pojo.Seller;
import me.projectx.needaticket.pojo.Ticket;
import me.projectx.needaticket.pojo.TicketType;
import me.projectx.needaticket.pojo.User;
import me.projectx.needaticket.pojo.Wallet;
public class UserActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.user_swipe_to_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private String uID;
    @BindView(R.id.textview_email) TextView email;
    @BindView(R.id.textview_password) TextView password;
    @BindView(R.id.etOldPassword) @Nullable EditText dialogOldPassword;
    @BindView(R.id.etNewPassword) @Nullable EditText dialogNewPassword;
    @BindView(R.id.etPasswordConfirm) @Nullable EditText dialogConfirmNewPassword;
    @BindView(R.id.etPassword) @Nullable EditText dialogPassword;
    @BindView(R.id.etEmailAddress) @Nullable EditText dialogNewEmail;
    @BindView(R.id.textview_bought) TextView boughtTickets;
    @BindView(R.id.navigation_drawer) NavigationView navigation;
    @BindView(R.id.fab_user) FloatingActionButton fabUser;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        this.setListener();
        this.uID = getIntent().getStringExtra("uID");
        //this.getUser();
        ArrayList<Ticket> tickets = new ArrayList<>();
        Artist a = new Artist("lol", "Martin Garrix");
        ArrayList<Ticket> tickets2 = new ArrayList<>();
        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(a);
        Concert c1 = new Concert("lol", "We are here", new Date(), new Date(), "Loliweg 3", artists, Genre.DANCE, tickets2);
        Seller oe = new Seller("iiooo", "OETicket@oe.com");
        Ticket t1 = new Ticket(1, TicketType.CONCERT, "Day 1 Ticket", (float) 22.99, oe, null, c1);
        tickets.add(t1);
        User u = new User("lol", "user@bashit.me", tickets, new Wallet(1, Float.parseFloat("1337.17")));
        setContent(u);
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.fabUser.setOnClickListener(new EditUserListener());
    }
    private void setContent (User user) {
        email.setText(user.getEmail());
        boughtTickets.setText("" + user.getTickets().size());
        password.setText("Strong");
    }
    private void setListenerNavigationHeader () {
        View navHeader;
        navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void changeMail () {
        try {
            TaskExecuteGraphQLMutation changeEmail = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_change_email).replace("$email", dialogNewEmail.getText()), uID, this);
            changeEmail.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    private void changePassword () {
        try {
            if (dialogNewPassword != dialogConfirmNewPassword)
                throw new PasswordException("Passwords don't match!");
            TaskExecuteGraphQLMutation changePassword = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_change_password).replace("$password", dialogNewPassword.getText()), uID, this);
            changePassword.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    private void showDiag () {
        final View dialogView = View.inflate(this, R.layout.dialog_edit_user, null);
        final Dialog dialog = new Dialog(this, R.style.UserAlertStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = dialog.findViewById(R.id.closeDialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick (View v) {
                revealShow(dialogView, false, dialog);
            }
        });
        Button changeEmail = dialog.findViewById(R.id.btChangeEmail);
        changeEmail.setOnClickListener(new ChangeEmailListener());
        Button changePassword = dialog.findViewById(R.id.btChangePassword);
        changePassword.setOnClickListener(new ChangePasswordListener());
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow (DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    revealShow(dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    private void revealShow (View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        int cx = (int) (fabUser.getX() + ((double) fabUser.getWidth() / 2));
        int cy = (int) (fabUser.getY()) + fabUser.getHeight() + 56;
        if (b) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd (Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(700);
            anim.start();
        }
    }
    @Override public void onPreExecute (Class resource) {
        swipeRefreshLayout.setRefreshing(true);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            user = new Gson().fromJson((String) result, User.class);
            setContent(user);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
    @Override public void onRefresh () {
        getUser();
    }
    private void getUser () {
        try {
            TaskExecuteGraphQLQuery<User> getUser = new TaskExecuteGraphQLQuery<>(getString(R.string.webservice_get_user), uID, this);
            getUser.execute();
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    private class ChangeEmailListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            changeMail();
        }
    }
    private class ChangePasswordListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            changePassword();
        }
    }
    private class EditUserListener implements View.OnClickListener {
        @Override public void onClick (View v) {
            showDiag();
        }
    }
}