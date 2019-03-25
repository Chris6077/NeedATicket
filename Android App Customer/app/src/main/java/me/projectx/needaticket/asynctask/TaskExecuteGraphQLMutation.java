package me.projectx.needaticket.asynctask;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
public class TaskExecuteGraphQLMutation extends AsyncTask<String, Void, String> {
    private String url;
    private String query;
    private String auth;
    private InterfaceTaskDefault listener;
    public TaskExecuteGraphQLMutation (String url, String query, String auth, InterfaceTaskDefault listener) {
        this.url = url;
        this.query = query;
        this.auth = auth;
        this.listener = listener;
    }
    @Override protected String doInBackground (String... params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.url).openConnection();
            return this.postData(conn, params);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return null;
    }
    @Override protected void onPreExecute () {
        this.listener.onPreExecute(this.getClass());
        super.onPreExecute();
    }
    @Override protected void onPostExecute (final String result) {
        this.listener.onPostExecute(result, this.getClass());
        super.onPostExecute(result);
    }
    private String postData (HttpURLConnection conn, String... params) {
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("query", query);
            if(auth != null && auth != "") conn.setRequestProperty("authentication", "bearer " + auth);
            if(conn.getResponseCode() == 200) return conn.getResponseMessage();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Error connecting to the server. Check your connection.");
        }
        return "";
    }
}
