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

import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
public class LoginActivity extends AppCompatActivity implements InterfaceTaskDefault {
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    private TaskExecuteGraphQLMutation mAuthTask;
    @BindView(R.id.etEmailAddress) EditText mEmailView;
    @BindView(R.id.etPassword) EditText mPasswordView;
    @BindView(R.id.loadingPanel) View mProgressView;
    @BindView(R.id.login_form) View mLoginFormView;
    @BindView(R.id.btLogin) Button btLogin;
    @BindView(R.id.btRegister) Button btRegister;
    private VideoView videoBG;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        registrateEventHandlers();
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
        try {
            /*
            Intent concertsActivity = new Intent(this, ConcertsActivity.class);
            concertsActivity.putExtra("uID", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOThhYTY3YzU1NWEyMDAxNzlmYWQ5YSIsImVtYWlsIjoiY2hyaXNAYmFzaGl0Lm1lIiwiaWF0IjoxNTUzNjA4MzUyLCJleHAiOjE1ODUxNjU5NTJ9.PoDTQ4XjEFp7QVRvUTR0OE-MntkKEvxAl24Dus1S6Qs");
            try {
                finish();
                startActivity(concertsActivity);
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }*/

            mAuthTask = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_login).replace("$email", mEmailView.getText()).replace("$password", mPasswordView.getText()), "", this);
            mAuthTask.execute();
        }
        catch(Exception ex){
            showProgress(false);
            HandlerState.handle(ex,this);
        }
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
        mProgressView.setBackgroundColor(show ? getColor(R.color.colorPrimary) : getColor(R.color.transparency));
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        videoBG.setVisibility(show ? View.GONE : View.VISIBLE);
        if(!show) {
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
        }
        else videoBG.pause();
    }
    @Override public void onPostExecute (Object result, Class resource) {
        if(result != null && !result.equals("") && !((String)result).split("\"")[1].equals("errors")) {
            Intent concertsActivity = new Intent(this, ConcertsActivity.class);
            concertsActivity.putExtra("uID", ((String) result).split(":")[2].split("\"")[1]);
            try {
                finish();
                startActivity(concertsActivity);
            } catch (Exception e) {
                HandlerState.handle(e, getApplicationContext());
            }
        } else {
            HandlerState.handle(getApplicationContext());
        }
        showProgress(false);
    }
    @Override
    public void onBackPressed() {
        final Intent welcomeActivity = new Intent(this, WelcomeActivity.class);
        try {
            finish();
            startActivity(welcomeActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
}