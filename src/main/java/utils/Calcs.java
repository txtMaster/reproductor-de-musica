package utils;

public class Calcs {
    public static float clamp(float min,float value,float max){
        return Math.max(min,Math.min(value,max));
    }
}
