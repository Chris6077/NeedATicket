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

import me.projectx.needaticket.R;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.Concert;

public class TaskGetConcert extends AsyncTask<Object, Object, Concert> {
    private String url;
    private InterfaceTaskDefault listener;

    public TaskGetConcert(String url, InterfaceTaskDefault listener) {
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
    protected Concert doInBackground(Object... params) {
        try {
            Gson gson = new Gson();
            HttpURLConnection conn = (HttpURLConnection) new URL(this.getUrl()).openConnection();
            Type collectionType = new TypeToken<Concert>(){}.getType();
            String result = GetData(conn);
            System.out.println("--------------------------------------------\n" + result);
            Concert concert = gson.fromJson(result, collectionType);
            return concert;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Concert concert){
        this.getListener().onPostExecute(concert,this.getClass());
        super.onPostExecute(concert);
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
            conn.setRequestProperty("API_KEY", Resources.getSystem().getString(R.string.APIKEY));
            conn.setRequestProperty("uID", Resources.getSystem().getString(R.string.uID));

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
