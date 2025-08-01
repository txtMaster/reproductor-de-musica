package components;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.layout.HBox;
import utils.Calcs;
import utils.Component;

public class RelativeHBox extends HBox {

    public static final String fxmlPath = Component.fxmlPath(RelativeHBox.class);


    private final FloatProperty min = new SimpleFloatProperty(6f);
    private final FloatProperty max = new SimpleFloatProperty(12f);
    private final FloatProperty relativeScale = new SimpleFloatProperty(0.05f);


    public FloatProperty minProperty(){
        return this.min;
    }
    public float getMin(){
        return this.min.get();
    }
    public void setMin(float min){
        this.min.set(min);
    }

    public FloatProperty maxProperty(){
        return this.max;
    }
    public float getMax(){
        return this.max.get();
    }
    public void setMax(float max){
        this.max.set(max);
    }

    public FloatProperty relativeScaleProperty(){
        return this.relativeScale;
    }
    public float getRelativeScale(){
        return this.relativeScale.get();
    }
    public void setRelativeScale(float relativeScale){
        this.relativeScale.set(relativeScale);
    }


    public RelativeHBox(){
        super();
        Component.init(this,fxmlPath);
        this.heightProperty().addListener((obs,oldVal,newVal) -> {
            double fontSize = Calcs.clamp(
                    this.getMin(),
                    (float)(Math.min(getHeight(),getWidth()) * this.getRelativeScale()),
                    this.getMax()
            );
            this.setStyle("-fx-font-size:"+fontSize);
        });
        this.widthProperty().addListener((obs,oldVal,newVal) -> {
            double fontSize = Calcs.clamp(
                    this.getMin(),
                    (float)(Math.min(getHeight(),getWidth()) * this.getRelativeScale()),
                    this.getMax()
            );
            this.setStyle("-fx-font-size:"+fontSize);
        });
    }
}

