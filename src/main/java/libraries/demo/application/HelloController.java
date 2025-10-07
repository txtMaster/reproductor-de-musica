package libraries.demo.application;

import components.SVGUrl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class HelloController {

    @FXML
    public ToggleButton toggleBtn;
    @FXML
    public StackPane testSP;
    @FXML
    public SVGUrl perilla;

    @FXML
    public ImageView myimage;

    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize(){
    }
}