package me.projectx.needaticket.asynctask;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.projectx.needaticket.interfaces.InterfaceTaskDefault;
import me.projectx.needaticket.pojo.PasswordWrapper;

public class TaskChangePassword extends AsyncTask<String, Void, String> {

    //fields
    private String url;
    private String uID;
    private String oldPassword;
    private String newPassword;
    private InterfaceTaskDefault listener;

    //constructors

    public TaskChangePassword(String url, String uID, String oldPassword, String newPassword, InterfaceTaskDefault listener) {
        this.url = url;
        this.uID = uID;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.listener = listener;
    }

    //super

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(this.url).openConnection();
            this.PostData(conn, params);
            return this.GetData(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String result) {
        this.listener.onPostExecute(result,this.getClass());
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        this.listener.onPreExecute(this.getClass());
        super.onPreExecute();
    }

    //custom

    private void PostData(HttpURLConnection conn, String... params) {
        BufferedWriter writer;

        try {
            //posting the data
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            PasswordWrapper pw = new PasswordWrapper(uID, oldPassword, newPassword);
            writer.write(pw.toString()); //product - object in json-format
            writer.flush();
            writer.close();
            conn.getResponseCode();
        } catch (Exception error) {
            System.out.println("ERROR --- " + error);
        }

    }

    private String GetData(HttpURLConnection conn) {
        BufferedReader reader;
        String content = null;

        try {
            //reading the result
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
        } catch (Exception error) {
            System.out.println("ERROR --- " + error);
        }
        return content;
    }

}