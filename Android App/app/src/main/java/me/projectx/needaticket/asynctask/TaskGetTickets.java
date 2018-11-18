package me.projectx.needaticket.asynctask;

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

import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.Ticket;

public class TaskGetTickets extends AsyncTask<Object, Object, ArrayList<Ticket>> {
    private String url;
    private InterfaceTaskDefault listener;

    public TaskGetTickets(String url, InterfaceTaskDefault listener) {
        this.setUrl(url);
        this.setListener(listener);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
            Type collectionType = new TypeToken<Collection<Ticket>>(){}.getType();
            String result = GetData(conn);
            System.out.println("--------------------------------------------\n" + result);
            ArrayList<Ticket> tickets = gson.fromJson(result, collectionType);
            return tickets;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Ticket> tickets){
        this.getListener().onPostExecute(tickets,this.getClass());
        super.onPostExecute(tickets);
    }

    @Override
    protected void onPreExecute() {
        this.getListener().onPreExecute(this.getClass());
        super.onPreExecute();
    }

    private String GetData(HttpURLConnection conn){
        BufferedReader reader;
        String content = null;

        try{
            conn.setRequestMethod("GET");
            conn.setRequestProperty("API_KEY", "dmFsaTEyMzRpMjMwOGhnaW9zZ2Rqb2lqY3hvaTgwN");
            conn.setRequestProperty("uID", "5b2776116358aa0004540d92");

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

        }catch(Exception error){
            error.printStackTrace();
        }
        System.out.println("CONTENT" + content);
        return content;
    }
}