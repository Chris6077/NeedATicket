/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgData.noIdea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Valon
 */
public class ControllerSync {

    private URL url;

    ControllerSync(URL url) {
        this.url = url;
    }

    String getResponse(String[] params) {
        String result;
        // extract request params for clarity
        HttpMethod httpMethod = HttpMethod.valueOf(params[0]);
        String route = params[1];
        String payload = (params.length >= 3) ? params[2] : null;

        try {
            if (payload != null) {
                // set payload or querystring
                if (httpMethod == HttpMethod.GET) {
                    route += "?" + payload;
                }

                url = new URL(url + "/" + route);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.DELETE) {
                    write(connection, httpMethod, payload);
                }

                if(route.equals("login") || route.equals("register")){
                    System.out.println("here token");
                    result = read_headerOnly(connection);
                    System.out.println("here token : " + result);
                }else{
                    result = read(connection);
                }
                
                connection.disconnect();

                return result;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    private void write(HttpURLConnection connection, HttpMethod httpMethod, String json) throws IOException {
        connection.setRequestMethod(httpMethod.toString());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            
            writer.write(json);
            writer.flush();
        }

        connection.getResponseCode();
    }

    private String read(HttpURLConnection connection) throws IOException {
        String result = null;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader((connection.getInputStream())))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();
        }
        return result;
    }
    
    
    private String read_headerOnly(HttpURLConnection connection) throws IOException {
        String token = connection.getHeaderField("x-access-token");
        return token == null ? "-1" : token;
    }
    

}
