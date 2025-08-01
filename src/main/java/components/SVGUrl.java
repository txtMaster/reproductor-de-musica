package components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.SVGPath;
import utils.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SVGUrl extends SVGPath {

    private final StringProperty svgPath = new SimpleStringProperty();

    public static final String fxmlPath = Component.fxmlPath(SVGUrl.class);

    public String getSvgPath() {
        return svgPath.get();
    }
    public void setSvgPath(String svgPath) {
        this.svgPath.set(svgPath);
    }
    public StringProperty svgPathProperty() {
        return svgPath;
    }

    public SVGUrl() {
        super();
        Component.init(this,fxmlPath);
        this.svgPathProperty().addListener((obs, oldVal, newVal)->{
            loadSvgFromUrl(newVal);
        });
    }
    void loadSvgFromUrl(String url){
        try {
            String path = "svg/"+url;
            InputStream is = getClass().getResourceAsStream("/"+path);
            if(is == null){
                System.err.println("SVG no found: "+path);
                return;
            }
            String svgContent = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            String d = svgContent.replaceAll("(?s).*?<path[^>]*d\\s*=\\s*\"([^\"]+)\".*", "$1");
            setContent(d);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
