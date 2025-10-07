package controllers.pages;

import controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PopUpController extends Controller {

    @FXML
    private Button btnAccept;

    @FXML
    private Label lbMessage;

    @FXML
    void onBtnAcceptAction(ActionEvent event) {
        stage.close();
    }

    @FXML
    void initialize(){
    }

    static public void show(String message){

    }

    public void setMessage(String message) {
        lbMessage.setText(message);
    }
}
