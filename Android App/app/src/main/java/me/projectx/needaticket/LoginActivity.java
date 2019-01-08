package me.projectx.needaticket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import me.projectx.needaticket.asynctask.TaskLogin;
import me.projectx.needaticket.asynctask.TaskRegister;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;

public class LoginActivity extends AppCompatActivity implements InterfaceTaskDefault {
    private TaskLogin mAuthTask;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btLogin;
    private Button btRegister;
    private ImageView image_logo;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        btLogin = (Button)findViewById(R.id.btLogin);
        mEmailView = (EditText) findViewById(R.id.etEmailAddress);
        mPasswordView = (EditText) findViewById(R.id.etPassword);
        mLoginFormView = (ConstraintLayout) findViewById(R.id.login_form);
        mProgressView = (RelativeLayout) findViewById(R.id.loadingPanel);
    }
    private void registrateEventHandlers(){
        btRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterIntent();
            }
        });
        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }
    private void showRegisterIntent(){
        final Intent register_activity = new Intent(this, RegisterActivity.class);
        try{
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new Pair<View, String>(image_logo, "logo"));
            finish();
            startActivity(register_activity);
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

    private void attemptLogin(){
        try {
            mAuthTask = new TaskLogin(getString(R.string.webservice_login), mEmailView.getText().toString(), mPasswordView.getText().toString(), this);
            mAuthTask.execute();
        }
        catch(Exception ex){
            showProgress(false);
            HandlerState.handle(ex,this);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPreExecute(Class resource) {

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
}