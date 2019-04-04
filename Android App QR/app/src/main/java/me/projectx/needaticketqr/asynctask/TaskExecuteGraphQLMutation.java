package me.projectx.needaticketqr.asynctask;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticketqr.interfaces.InterfaceTaskDefault;
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
            this.postData(conn, params);
            return this.getData(conn);
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
    private void postData (HttpURLConnection conn, String... params) {
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if(auth != null && auth != "") conn.setRequestProperty("authorization", "bearer " + auth);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(query);
            wr.flush();
            wr.close();
            if(conn.getResponseCode() != 200) throw new Exception("Error when connecting!");
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Error connecting to the server. Check your connection.");
        }
    }
    private String getData (HttpURLConnection conn) {
        BufferedReader reader;
        String content = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            content = sb.toString();
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return content;
    }
}