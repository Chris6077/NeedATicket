package application.controllers;

import application.Launch;
import application.helpers.AnimationGenerator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author Benny
 */
public class LoginController implements Initializable {


    @FXML
    private AnchorPane parent;
    @FXML
    private Pane content;
    @FXML
    private JFXButton btn_CreateAccount;
    @FXML
    private ImageView gif;
    @FXML
    private JFXTextField tft_email;
    @FXML
    private JFXPasswordField tft_password;
    @FXML
    private JFXButton btn_login;
    @FXML
    private HBox top;

    private double xOffSet = 0;
    private double yOffSet = 0;

    private final AnimationGenerator animationGenerator = new AnimationGenerator();

    private void animateWhenLoginSuccess() {
        try {
            int duration = 100; // used to be 1000 => 1s ( 100m/s for testing)
            Parent menu = FXMLLoader.load(getClass().getResource("/application/views/Menu.fxml"));
            StackPane temp = new StackPane();
            temp.getChildren().add(new ImageView(this.getClass().getResource("/resources/pictures/checkmark.png").toString()));
            animationGenerator.applyFadeAnimationOn(content, duration, 1.0f, 0f, (ActionEvent event) -> {
                temp.setOpacity(0);
                Launch.stage.setScene(new Scene(temp, 1280, 720));
                animationGenerator.applyFadeAnimationOn(temp, duration, 0f, 1.0f, event1 -> {
                    animationGenerator.applyFadeAnimationOn(temp, duration, 1.0f, 0f, event2 -> {
                        parent.getChildren().removeAll();
                        parent.getChildren().setAll(menu);
                        Launch.stage.setScene(parent.getScene());
                        animationGenerator.applyFadeAnimationOn(menu, duration, 0f, 1.0f, null);

                    });
                });
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gif.setOpacity(0.5);
        makeStageDragable();
    }

    private void makeStageDragable() {
        parent.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        parent.setOnMouseDragged((event) -> {
            Launch.stage.setX(event.getScreenX() - xOffSet);
            Launch.stage.setY(event.getScreenY() - yOffSet);
            Launch.stage.setOpacity(0.8f);
        });
        parent.setOnDragDone((event) -> {
            Launch.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((event) -> {
            Launch.stage.setOpacity(1.0f);
        });
    }
    boolean login_mode = true;

    @FXML
    void handle_bttn(ActionEvent event) throws IOException {

        if (event.getSource().equals(btn_CreateAccount)) {

            if (login_mode) {
                btn_login.setText("Register");
                btn_CreateAccount.setText("Already have an account ? Login now !");
                login_mode = false;
                alert("whwhwhwhwh");
            } else {
                btn_login.setText("Login");
                btn_CreateAccount.setText("You do not have an account ? Create one now !");
                login_mode = true;
            }
        } else if (event.getSource().equals(btn_login)) {
            if (validate()) {
                if (login_mode) {
                    login();
                    animateWhenLoginSuccess();
                } else {
                    //register();
                }

            } else {
                //show msg
            }
        }
    }

    @FXML
    private void minimize_stage(MouseEvent event) {
        Launch.stage.setIconified(true);
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }
    @FXML
    private StackPane rootPane;

    public void alert(String msg) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dl = new JFXDialogLayout();
        JFXButton bttn = new JFXButton("Ok");
        JFXDialog dialog = new JFXDialog(rootPane, dl, JFXDialog.DialogTransition.TOP);
        bttn.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            dialog.close();
        });
        dl.setHeading(new Text(msg));
        dl.setActions(bttn);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent e) -> {
            parent.setEffect(null);
        });
        parent.setEffect(blur);

    }

    private boolean validate() {
        return true;
    }

    private void login() {

    }

    private void register() {

    }
}
