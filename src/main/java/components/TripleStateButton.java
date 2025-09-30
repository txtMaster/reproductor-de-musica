package components;

import enums.LoopMode;
import javafx.scene.control.Button;

import java.util.function.Consumer;

public class TripleStateButton extends Button {
    public TripleStateButton(){
        super();
        this.getStyleClass().add("triple-state-button");
        this.setState(LoopMode.NO_LOOP);
        this.setOnAction(_ ->toggleState());
    }

    private LoopMode state = LoopMode.NO_LOOP;

    public void setState(LoopMode state){
        this.state = state;
        this.getStyleClass().removeAll(
                LoopMode.NO_LOOP.toString(),
                LoopMode.LOOP_ALL.toString(),
                LoopMode.LOOP_ONE.toString()
        );
        this.getStyleClass().add(state.toString());
        this.onChangeState.accept(state);
    }
    public LoopMode getState(){
        return this.state;
    }

    void toggleState(){
        LoopMode state = switch (getState()){
            case NO_LOOP -> LoopMode.LOOP_ONE;
            case LOOP_ONE -> LoopMode.LOOP_ALL;
            case LOOP_ALL -> LoopMode.NO_LOOP;
        };
        setState(state);
    }

    public Consumer<LoopMode> onChangeState = (e)->{};
}

