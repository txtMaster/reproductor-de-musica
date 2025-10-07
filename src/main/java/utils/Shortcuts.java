package utils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Shortcuts {
    
    public Shortcuts(){
        actions = new HashMap<>(Map.of());
    }
    public Shortcuts(HashMap<Object, EventHandler<KeyEvent>> actions){
        this.actions = actions;
    }

    public HashMap<Object, EventHandler<KeyEvent>> actions;

    public EventHandler<KeyEvent> run = e -> {
        EventHandler<KeyEvent> action = this.actions.getOrDefault(e.getEventType(),null);
        if(action != null)action.handle(e);

    };
}
