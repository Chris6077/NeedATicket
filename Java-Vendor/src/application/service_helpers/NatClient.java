/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgData.noIdea;

import application.data.User;
import com.google.gson.Gson;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;



/**
 *
 * @author Valon
 */
public class NatClient {

    private static NatClient client;
    private static URL url;
    private final Gson GSON = new Gson();
    private User currentUser;
    private String token;

    public static NatClient newInstance() {
        if (client == null) {
            try {
                URL url_2 = new URL("https://9baa0294-d2ca-4ae4-b71d-cc6a4d9fb8cf.mock.pstmn.io");
                client = new NatClient(url_2);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return client;
    }

    private NatClient(URL url) {
        NatClient.url = url;
    }

    private String request(HttpMethod httpMethod, String route, String... params) throws Exception {
        ControllerSync controller = new ControllerSync(url);

        ArrayList<String> connectionParams = new ArrayList<>();
        connectionParams.add(httpMethod.toString());
        connectionParams.add(route);
        connectionParams.addAll(Arrays.asList(params));
        params = connectionParams.toArray(params);
        String response = controller.getResponse(params);
        System.out.println("res : " + response);
        return response;
    }

    public int getCurrentUserID() {
        return currentUser.getId();
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public String getToken(){
        return token;
    }

//    public int login(String email, String password) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("username", email);
//            jsonObject.put("password", password);
//
//            token = request(HttpMethod.POST, "login", jsonObject.toString());
//            if(token.equals("-1")) return -1;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//    
//    public int register(String email, String password) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("username", email);
//            jsonObject.put("password", password);
//
//            token = request(HttpMethod.POST, "register", jsonObject.toString());
//            if(token.equals("-1")) return -1;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

}
