package utils.path;

public enum View {
    LOGIN("/controllers/pages/Login/Login.fxml"),
    HOME("/controllers/pages/Home/Home.fxml"),
    MAIN("/controllers/pages/Main/Main.fxml"),
    POPUP("/controllers/pages/PopUp/PopUp.fxml"),
    TEST("/controllers/pages/TestView/TestView.fxml"),
    CONFIGURATION("/controllers/pages/Configuration/Configuration.fxml");

    private final String path;

    View(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}