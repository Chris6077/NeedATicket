package me.projectx.needaticket.asynctask;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.R;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.LoginWrapper;

public class TaskRegister extends AsyncTask<String, Void, String> {
    private String url;
    private String email;
    private String password;
    private InterfaceTaskDefault listener;

    public TaskRegister(String url, String email, String password, String confirmPassword, InterfaceTaskDefault listener) throws Exception {
        this.url = url;
        this.email = email;
        this.password = password;
        if (confirmPassword.compareTo(password) != 0) throw new Exception("Passwords don't match!");
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.url).openConnection();
            this.postData(conn, params);
            return this.getData(conn);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String result) {
        this.listener.onPostExecute(result, this.getClass());
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        this.listener.onPreExecute(this.getClass());
        super.onPreExecute();
    }

    private void postData(HttpURLConnection conn, String... params) {
        BufferedWriter writer;

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("API_KEY", Resources.getSystem().getString(R.string.API_KEY));
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            LoginWrapper lw = new LoginWrapper(email, password);
            writer.write(new Gson().toJson(lw));
            writer.flush();
            writer.close();
            conn.getResponseCode();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
    }

    private String getData(HttpURLConnection conn) {
        BufferedReader reader;
        String content = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return content;
    }
}