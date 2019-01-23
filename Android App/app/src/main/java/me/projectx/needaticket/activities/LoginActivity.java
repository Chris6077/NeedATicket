package me.projectx.needaticket.activities;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskLogin;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
public class LoginActivity extends AppCompatActivity implements InterfaceTaskDefault {
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    private TaskLogin mAuthTask;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btLogin;
    private Button btRegister;
    private VideoView videoBG;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // VideoView
        videoBG = findViewById(R.id.videoView);
        //Video URI
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.c1);
        //Set URI to video
        videoBG.setVideoURI(uri);
        videoBG.setBackgroundColor(getResources().getColor(R.color.transparency));
        //Start video
        videoBG.start();
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override public void onPrepared (MediaPlayer mp) {
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
        setViews();
        registrateEventHandlers();
    }
    private void setViews () {
        btRegister = findViewById(R.id.btRegister);
        btLogin = findViewById(R.id.btLogin);
        mEmailView = findViewById(R.id.etEmailAddress);
        mPasswordView = findViewById(R.id.etPassword);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.loadingPanel);
    }
    private void registrateEventHandlers () {
        btRegister.setOnClickListener(new OnClickListener() {
            @Override public void onClick (View v) {
                showRegisterIntent();
            }
        });
        btLogin.setOnClickListener(new OnClickListener() {
            @Override public void onClick (View v) {
                attemptLogin();
            }
        });
    }
    private void showRegisterIntent () {
        final Intent registerActivity = new Intent(this, RegisterActivity.class);
        try {
            finish();
            startActivity(registerActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
    private void attemptLogin () {
        final Intent concertsActivity = new Intent(this, ConcertsActivity.class);
        concertsActivity.putExtra("uID", "lol");
        try {
            finish();
            startActivity(concertsActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
        /*
        try {
            mAuthTask = new TaskLogin(getString(R.string.webservice_login), mEmailView.getText().toString(), mPasswordView.getText().toString(), this);
            mAuthTask.execute();
        }
        catch(Exception ex){
            showProgress(false);
            HandlerState.handle(ex,this);
        }*/
    }
    @Override protected void onDestroy () {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
    // IMPORTANT FOR VIDEO VIEW
    @Override protected void onPause () {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }
    @Override protected void onResume () {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }
    @Override public void onPreExecute (Class resource) {
        showProgress(true);
    }
    private void showProgress (final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    @Override public void onPostExecute (Object result, Class resource) {
        Intent concertsActivity = new Intent(this, ConcertsActivity.class);
        concertsActivity.putExtra("uID", (String) result);
        try {
            finish();
            startActivity(concertsActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
}