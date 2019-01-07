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

import me.projectx.needaticket.R;
import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.Concert;

public class TaskGetConcerts extends AsyncTask<Object, Object, ArrayList<Concert>> {
    private String url;
    private String uID;
    private InterfaceTaskDefault listener;

    public TaskGetConcerts(String url, String uID, InterfaceTaskDefault listener) {
        this.setUrl(url);
        this.uID = uID;
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
    protected ArrayList<Concert> doInBackground(Object... params) {
        try {
            Gson gson = new Gson();
            HttpURLConnection conn = (HttpURLConnection) new URL(this.getUrl()).openConnection();
            Type collectionType = new TypeToken<Collection<Concert>>(){}.getType();
            String result = GetData(conn);
            System.out.println("--------------------------------------------\n" + result);
            ArrayList<Concert> concerts = gson.fromJson(result, collectionType);
            return concerts;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Concert> concerts){
        this.getListener().onPostExecute(concerts,this.getClass());
        super.onPostExecute(concerts);
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
            conn.setRequestProperty("uID", uID);

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
