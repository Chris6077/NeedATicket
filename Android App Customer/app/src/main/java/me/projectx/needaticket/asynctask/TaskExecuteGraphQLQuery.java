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
import java.util.logging.Level;
import java.util.logging.Logger;

import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
public class TaskExecuteGraphQLQuery <T> extends AsyncTask<Object, Object, T> {
    private String url;
    private String auth;
    private InterfaceTaskDefault listener;
    public TaskExecuteGraphQLQuery (String url, String auth, InterfaceTaskDefault listener) {
        this.setUrl(url);
        this.auth = auth;
        this.setListener(listener);
    }
    @Override protected T doInBackground (Object... params) {
        try {
            Gson gson = new Gson();
            HttpURLConnection conn = (HttpURLConnection) new URL(this.getUrl()).openConnection();
            Type collectionType = new TypeToken<T>() {}.getType();
            String result = getData(conn);
            return gson.fromJson(result, collectionType);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
        return null;
    }
    public String getUrl () {
        return url;
    }
    public void setUrl (String url) {
        this.url = url;
    }
    private String getData (HttpURLConnection conn) {
        BufferedReader reader;
        String content = null;
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authentication", "bearer " + auth);
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
    @Override protected void onPreExecute () {
        this.getListener().onPreExecute(this.getClass());
        super.onPreExecute();
    }
    @Override protected void onPostExecute (T object) {
        this.getListener().onPostExecute(object, this.getClass());
        super.onPostExecute(object);
    }
    public InterfaceTaskDefault getListener () {
        return listener;
    }
    public void setListener (InterfaceTaskDefault listener) {
        this.listener = listener;
    }
}
