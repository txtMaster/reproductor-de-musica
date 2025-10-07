package components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.ComponentUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SVGUrl extends SVGPath {

    private final StringProperty svgPath = new SimpleStringProperty();

    public static final String fxmlPath = ComponentUtils.fxmlPath(SVGUrl.class);

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
        ComponentUtils.init(this,fxmlPath);
        this.svgPathProperty().addListener((obs, oldVal, newVal)->{loadSvgFromUrl(newVal);
        });
    }
    public void loadSvgFromUrl(String url){
        try (InputStream is = getClass().getResourceAsStream(
                File.separator+"svg"+File.separator+url
        )) {
            if (is == null) return;

            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();

            // Obtener el primer <path>
            NodeList pathList = doc.getElementsByTagName("path");
            if (pathList.getLength() == 0) return;

            Element pathElement = (Element) pathList.item(0);
            String d = pathElement.getAttribute("d");
            this.setContent(d);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
