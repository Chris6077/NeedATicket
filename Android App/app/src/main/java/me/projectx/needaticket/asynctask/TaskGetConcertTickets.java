package me.projectx.needaticket.asynctask;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.R;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.Ticket;

public class TaskGetConcertTickets extends AsyncTask<Object, Object, ArrayList<Ticket>> {
    private String url;
    private String uID;
    private String cID;
    private InterfaceTaskDefault listener;

    public TaskGetConcertTickets(String url, String uID, String cID, InterfaceTaskDefault listener) {
        this.setUrl(url);
        this.setcID(cID);
        this.uID = uID;
        this.setListener(listener);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public InterfaceTaskDefault getListener() {
        return listener;
    }

    public void setListener(InterfaceTaskDefault listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Ticket> doInBackground(Object... params) {
        try {
            Gson gson = new Gson();
            HttpURLConnection conn = (HttpURLConnection) new URL(this.getUrl()).openConnection();
            Type collectionType = new TypeToken<Collection<Ticket>>() {
            }.getType();
            String result = getData(conn);
            return gson.fromJson(result, collectionType);

        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(ArrayList<Ticket> tickets) {
        this.getListener().onPostExecute(tickets, this.getClass());
        super.onPostExecute(tickets);
    }

    @Override
    protected void onPreExecute() {
        this.getListener().onPreExecute(this.getClass());
        super.onPreExecute();
    }

    private String getData(HttpURLConnection conn) {
        BufferedReader reader;
        String content = null;
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("API_KEY", Resources.getSystem().getString(R.string.API_KEY));
            conn.setRequestProperty("uID", uID);
            conn.setRequestProperty("cID", cID);

            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
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
