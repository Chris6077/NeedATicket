package me.projectx.needaticket.activities;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.projectx.needaticket.R;
import me.projectx.needaticket.asynctask.TaskExecuteGraphQLMutation;
import me.projectx.needaticket.handler.HandlerState;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
public class RegisterActivity extends AppCompatActivity implements InterfaceTaskDefault {
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    private TaskExecuteGraphQLMutation mAuthTask;
    @BindView(R.id.etEmailAddress) EditText mEmailView;
    @BindView(R.id.etPassword) EditText mPasswordView;
    @BindView(R.id.etPasswordConfirm) EditText mPasswordConfirmView;
    @BindView(R.id.register_form) View mLoginFormView;
    @BindView(R.id.loadingPanel) View mProgressView;
    @BindView(R.id.btRegister) Button btRegister;
    @BindView(R.id.btSignIn) Button btSignIn;
    private VideoView videoBG;
    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                attemptLogin();
            }
        });
        btSignIn.setOnClickListener(new OnClickListener() {
            @Override public void onClick (View v) {
                showLoginIntent();
            }
        });
    }
    private void attemptLogin () {
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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password) && !password.equals(confirmPassword)) {
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
        if(!password.equals(confirmPassword)){
            mPasswordView.setError(getString(R.string.error_passwords_dont_match));
            mPasswordConfirmView.setError(getString(R.string.error_passwords_dont_match));
            focusView = mPasswordConfirmView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                mAuthTask = new TaskExecuteGraphQLMutation(getString(R.string.webservice_default), getString(R.string.webservice_register).replace("$email", email).replace("$password", password), "", this);
                mAuthTask.execute();
            } catch (Exception ex) {
                showProgress(false);
                HandlerState.handle(ex, this);
            }
        }
    }
    private void showLoginIntent () {
        final Intent loginActivity = new Intent(this, LoginActivity.class);
        try {
            finish();
            startActivity(loginActivity);
        } catch (Exception e) {
            HandlerState.handle(e, getApplicationContext());
        }
    }
    private boolean isPasswordValid (String password) {
        return password.length() > 4;
    }
    private boolean isEmailValid (String email) {
        return email.contains("@");
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
        } else{
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

