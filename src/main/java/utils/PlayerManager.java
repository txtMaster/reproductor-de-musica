package utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Optional;

interface LibVlc extends Library {
    // Aquí defines los métodos nativos que usarás
    void libvlc_new(int argc, String[] argv);
}

public class PlayerManager {
    private static boolean vlcVerified = false;
    private static final ConfigurationManager config = new ConfigurationManager();
    private PlayerManager(){

    }

    public static boolean tryLoadVLC(){
        if(vlcVerified) return true;
        //si no requiere cargar, como en linux, intentar iniciar VLC
        if(!config.isRequiredLoadVLC()){
            System.out.println("intentando cargar automaticamente");
            vlcVerified = tryInitVLC();
            if(vlcVerified) return true;
        }

        //si no, intentar obtener del sistema
        String SO = System.getProperty("os.name").toLowerCase();
        if(SO.contains("win")){
            vlcVerified = tryLoadVLC("C:\\Program Files\\VideoLAN\\VLC",true);
            if(vlcVerified) config.setRequireLoadVLC(true);
        }else if(SO.contains("linux")){
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"which","vlc"});
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())
                );
                String line = reader.readLine();
                reader.close();
                System.out.println("linea de path del sistema: "+line);
                if(line != null && !line.isEmpty()) {
                    vlcVerified = tryInitVLC();
                    config.setRequireLoadVLC(!vlcVerified);
                }else config.setRequireLoadVLC(true);
            } catch (Exception _) {}
        }
        return  vlcVerified;
    }

    public static boolean tryLoadVLC(String path){
        return  tryLoadVLC(path,false);
    }
    public static boolean tryLoadVLC(String path, boolean absolute){
        if(vlcVerified) return true;
        if(path.isEmpty()) return false;
        try {
            File vlcDir = new File(path);
            //si el path no es un directorio retornar falso
            if (!vlcDir.exists() || !vlcDir.isDirectory()) {
                System.out.println("La ruta proporcionada no es válida o no es un directorio: " + path);
                return false;
            }
            System.setProperty("jna.library.path", vlcDir.getAbsolutePath());
            //System.setProperty("VLC_PLUGIN_PATH","vlc"+File.separator+"plugins");

            String vlcPath = System.getProperty("jna.library.path");


            if (vlcPath != null && !vlcPath.isEmpty())
                System.out.println("jna.library.path configurada correctamente: " + vlcPath);
            else {
                System.out.println("No se pudo configurar jna.library.path correctamente.");
                return false;
            }
            vlcVerified = tryInitVLC();
            if(vlcVerified) config.setVlcPath(vlcPath,absolute);
            return vlcVerified;
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Error al cargar la librería VLC: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error inesperado al intentar cargar VLC: " + e.getMessage());
            return false;
        }
    }

    static boolean tryInitVLC(){
        System.out.println("tryinitVLC");
        try {
            LibVlc vlc = (LibVlc) Native.loadLibrary("libvlc", LibVlc.class);
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public static AudioMediaPlayerComponent getMediaPlayerFactory(){
        tryLoadVLC();
        if(vlcVerified) return new AudioMediaPlayerComponent();
        return null;
    }
}
