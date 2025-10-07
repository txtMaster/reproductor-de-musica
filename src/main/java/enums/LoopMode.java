package enums;

public enum LoopMode{
    NO_LOOP("no-loop"),
    LOOP_ONE("loop-one"),
    LOOP_ALL("loop-all");

    private final String label;
    LoopMode(String label){this.label = label;}
    @Override public String toString(){return label;}
}
