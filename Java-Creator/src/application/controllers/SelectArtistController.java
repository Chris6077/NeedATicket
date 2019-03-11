    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;
////#108C90
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.sun.javafx.webkit.WebConsoleListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * FXML Controller class
 *
 * @author schueler
 */
public class SelectArtistController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String msg;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private JFXButton btn_NEXT;

    @FXML
    private WebView galleryWebView;
    /**
     * for communication to the Javascript engine.
     */
    private JSObject javascriptConnector;

    /**
     * for communication from the Javascript engine.
     */
    private JavaConnector javaConnector = new JavaConnector();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        HashMap<String, String> artists = new HashMap<>();
        artists.put("1", "f.png");
        artists.put("1", "eee.png");
        artists.put("harveyy", "peter.png");
       
        Gson test = new Gson();
        String toJson = test.toJson(artists);
        HashMap fromJson = test.fromJson(toJson, HashMap.class);
        System.out.println(toJson);
        System.out.println();
        initGallery();
    }

    @FXML
    void handleBtn(ActionEvent event) {
        if (event.getSource().equals(btn_NEXT)) {
            System.out.println("f");
            String x = (String) javascriptConnector.call("showResult:", 4);

            System.out.println("X = " + x);
        }
    }

    private void initGallery() {
        final WebEngine webEngine = this.galleryWebView.getEngine();
        // set up the listener
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                // set an interface object named 'javaConnector' in the web engine's page
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", javaConnector);

                // get the Javascript connector object. 
                javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
            }
        });

        WebConsoleListener.setDefaultListener((webView2, message, lineNumber, sourceId) -> {
            System.out.println(message + "[at " + lineNumber + "]");
        });
        // now load the page
        //webEngine.load("file:///C:/Users/schueler/Documents/NetBeansProjects/Application/src/resources/libs/index.html");
        webEngine.load("file:///C:/Users/Valon/Documents/NetBeansProjects/Application/src/resources/libs/index.html");
    }

    public class JavaConnector {

        /**
         * called when the JS side wants
         *
         * @param value the String to convert
         */
        public void toLowerCase(String value) {
            System.out.println("value : " + value);
            if (null != value) {
                javascriptConnector.call("showResult", 7);
            }
        }
        
        public void receiveData(String json){
            System.out.println("gggg boi : " + json);
            Gson g = new Gson();
            ArrayList<String> fromJson = (ArrayList<String>)g.fromJson(json, ArrayList.class);
            System.out.println("fromJson: " + fromJson);
        }

    }
}
