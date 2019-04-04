package me.projectx.needaticket.activities;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLQuery;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.listener.ListenerNavigationMenu;
import me.projectx.needaticket.listener.ListenerNavigationMenuHeader;
import me.projectx.needaticket.pojo.User;
import moer.intervalclick.api.IntervalClick;
public class UserActivity extends AppCompatActivity implements InterfaceTaskDefault, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.user_swipe_to_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private String uID;
    private View dialogView;
    private Dialog dialog;
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
        this.uID = getIntent().getStringExtra("uID");
        this.setListener();
        this.getUser();
    }
    private void setListener () {
        this.navigation.setNavigationItemSelectedListener(new ListenerNavigationMenu(this, uID));
        this.navigation.setItemIconTintList(null); //THIS LITTLE PIECE OF ... FIXES THE ICONS NOT SHOWING IN THE NAVMENU >:(
        this.setListenerNavigationHeader();
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.fabUser.setOnClickListener(new EditUserListener());
    }
    private void setContent () {
        email.setText(user.getEmail());
        boughtTickets.setText("" + user.getTotalBought());
        password.setText(user.getPasswordStrength().getStatus());
    }
    private void setListenerNavigationHeader () {
        View navHeader = navigation.getHeaderView(0);
        navHeader.setOnClickListener(new ListenerNavigationMenuHeader(this, uID));
    }
    private void changeMail () {
        try {
            boolean cancel = false;
            View focusView = null;
            EditText mEmailView = dialog.findViewById(R.id.etEmailAddress);
            if (TextUtils.isEmpty(mEmailView.getText())) {
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!mEmailView.getText().toString().contains("@")) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
            if (cancel) focusView.requestFocus();
            else {
                TaskExecuteGraphQLMutation changeEmail = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_change_email).replace("$email", mEmailView.getText()), uID, this);
                changeEmail.execute();
            }
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    private void changePassword () {
        try {
            boolean cancel = false;
            View focusView = null;
            EditText mPasswordView = dialog.findViewById(R.id.etNewPassword);
            EditText mPasswordConfirmView = dialog.findViewById(R.id.etPasswordConfirm);
            if (TextUtils.isEmpty(mPasswordView.getText()) || mPasswordView.getText().toString().length() <= 4) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
            if(!mPasswordView.getText().toString().equals(mPasswordConfirmView.getText().toString())){
                mPasswordView.setError(getString(R.string.error_passwords_dont_match));
                mPasswordConfirmView.setError(getString(R.string.error_passwords_dont_match));
                focusView = mPasswordConfirmView;
                cancel = true;
            }
            if (cancel) focusView.requestFocus();
            else {
                TaskExecuteGraphQLMutation changePassword = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_change_password).replace("$password", mPasswordView.getText()), uID, this);
                changePassword.execute();
            }
        } catch (Exception error) {
            HandlerState.handle(error, this);
        }
    }
    private void showDiag () {
        dialogView = View.inflate(this, R.layout.dialog_edit_user, null);
        dialog = new Dialog(this, R.style.UserAlertStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = dialog.findViewById(R.id.closeDialog);
        imageView.setOnClickListener(v -> revealShow(dialogView, false, dialog));
        Button changeEmail = dialog.findViewById(R.id.btChangeEmail);
        changeEmail.setOnClickListener(new ChangeEmailListener());
        Button changePassword = dialog.findViewById(R.id.btChangePassword);
        changePassword.setOnClickListener(new ChangePasswordListener());
        dialog.setOnShowListener(dialogInterface -> revealShow(dialogView, true, null));
        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                revealShow(dialogView, false, dialog);
                return true;
            }
            return false;
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
        if(dialogView != null && dialog != null && dialog.isShowing()) revealShow(dialogView, false, dialog);
        if(result != null && !result.equals("") && !((String)result).split("\"")[1].equals("errors")) {
            try {
                user = new Gson().fromJson("{" + ((String) result).split("\\{")[3].split("\\}")[0] + "{" + ((String) result).split("\\{")[4].split("\\}")[0] + "}" + "}", User.class);
                setContent();
            } catch (Exception e) {
                HandlerState.handle(new Exception("Error: Check your connection!"), getApplicationContext());
            }
        } else HandlerState.handle(getApplicationContext());
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override public void onRefresh () {
        getUser();
    }
    private void getUser () {
        try {
            TaskExecuteGraphQLQuery getUser = new TaskExecuteGraphQLQuery(getString(R.string.webservice_get_user), uID, this);
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