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
import me.projectx.needaticket.pojo.AmountWrapper;
public class TaskCashOut extends AsyncTask<String, Void, String> {
    private String url;
    private String uID;
    private Float amount;
    private InterfaceTaskDefault listener;
    public TaskCashOut (String url, String uID, Float amount, InterfaceTaskDefault listener) {
        this.url = url;
        this.uID = uID;
        this.amount = amount;
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
        BufferedWriter writer;
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("API_KEY", Resources.getSystem().getString(R.string.API_KEY));
            conn.setRequestProperty("uID", uID);
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            AmountWrapper aw = new AmountWrapper(amount);
            writer.write(new Gson().toJson(aw));
            writer.flush();
            writer.close();
            conn.getResponseCode();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
    }
    private String getData (HttpURLConnection conn) {
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