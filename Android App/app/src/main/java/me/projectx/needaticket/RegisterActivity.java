package me.projectx.needaticket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import me.projectx.needaticket.asynctask.TaskLogin;
import me.projectx.needaticket.asynctask.TaskRegister;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements InterfaceTaskDefault {
    private TaskRegister mAuthTask;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private View mLoginFormView;
    private View mProgressView;
    private Button btRegister;
    private Button btSignIn;
    private ImageView image_logo;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // VideoView
        videoBG = (VideoView) findViewById(R.id.videoView);

        //Video URI
        Uri uri = Uri.parse("android.resource://"
                + getPackageName()
                + "/"
                + R.raw.c1);

        //Set URI to video
        videoBG.setVideoURI(uri);
        videoBG.setBackgroundColor(getResources().getColor(R.color.transparency));

        //Start video
        videoBG.start();
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                if(mCurrentVideoPosition != 0){
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
        setViews();
        registrateEventHandlers();
    }
    private void setViews() {
        image_logo = (ImageView) findViewById(R.id.imageView);
        btRegister = (Button)findViewById(R.id.btRegister);
        btSignIn = (Button)findViewById(R.id.btSignIn);
        mEmailView = (EditText) findViewById(R.id.etEmailAddress);
        mPasswordView = (EditText) findViewById(R.id.etPassword);
        mPasswordConfirmView = (EditText) findViewById(R.id.etPasswordConfirm);
        mProgressView = (RelativeLayout) findViewById(R.id.loadingPanel);
        mLoginFormView = (ConstraintLayout) findViewById(R.id.register_form);
    }
    private void registrateEventHandlers(){
        btRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        btSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginIntent();
            }
        });
    }
    private void showLoginIntent(){
        final Intent login_activity = new Intent(this, LoginActivity.class);
        try{
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new Pair<View, String>(image_logo, "logo"));
            finish();
            startActivity(login_activity);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    // IMPORTANT FOR VIDEO VIEW
    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mPasswordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password) && password != confirmPassword) {
            mPasswordConfirmView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordConfirmView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                mAuthTask = new TaskRegister(getString(R.string.webservice_register), email, password, confirmPassword, this);
                mAuthTask.execute();
            }
            catch(Exception ex){
                showProgress(false);
                HandlerState.handle(ex,this);
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPreExecute(Class resource) {
        showProgress(true);
    }

    @Override
    public void onPostExecute(Object result, Class resource) {
        Intent concerts_activity = new Intent(this, ConcertsActivity.class);
        concerts_activity.putExtra("uID", (String)result);
        try{
            finish();
            startActivity(concerts_activity);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
