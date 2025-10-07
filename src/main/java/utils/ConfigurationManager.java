package utils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

public class ConfigurationManager {

    static final String VLC_PATH_KEY = "vlc.path";
    static final String REQUIRE_LOAD_VLC = "require.load.vlc";
    static final String FILE_NAME = "config.properties";

    private final Properties properties;

    public ConfigurationManager() {
        properties = new Properties();
        try {
            File configFile = new File(FILE_NAME);
            if(!configFile.exists()){
                boolean hasCreated = configFile.createNewFile();
                if(!hasCreated) return;
            }
            FileInputStream inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException _) {}
    }

    //retorna la ruta de los archivos vlc, si no existe retorna la carpeta embebida
    public String getVlcPath() {
        String vlcPath = properties.getProperty(VLC_PATH_KEY,"");
        if (!vlcPath.isEmpty()) return vlcPath;
        else return getEmbebedVLCDir().getPath();
    }

    public void setRequireLoadVLC(boolean isRequired){
        properties.setProperty(REQUIRE_LOAD_VLC,Boolean.toString(isRequired));
        save();
    }
    public boolean isRequiredLoadVLC(){
        String isRequired = properties.getProperty(REQUIRE_LOAD_VLC,null);
        if(isRequired == null) return true;
        return Boolean.parseBoolean(isRequired);
    }

    public  void setVlcPath(String path,boolean absolute){
        try{
            properties.setProperty(
                    VLC_PATH_KEY,
                    absolute
                    ? new File(path).getAbsolutePath()
                    : PathUtils.getRelativePath(PathUtils.getAppDir().getAbsolutePath(),path));
            save();
        } catch (URISyntaxException e) {e.printStackTrace();}
    }

    public void setVlcPath(String path) {
        setVlcPath(path,false);
    }

    // Guardar las propiedades en el archivo
    public void save() {
        System.out.println("GUARDANDO CONFIG: "+properties.toString());
        try {
            System.out.println("INICIANDO INTENTO");
            File configFile = new File(FILE_NAME);
            if(!configFile.exists()){
                System.out.println("config no existe, creando....");
                boolean hasCreated = configFile.createNewFile();
                if(!hasCreated) return;
                System.out.println("config creado");
            }
            try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
                properties.store(outputStream, "Configuraciones iniciales");
                System.out.println("CONFIGURACION GUARDADA.");
            }
        } catch (IOException e) {e.printStackTrace();}
    }
    public File getEmbebedVLCDir(){
        try{
            return new File(
                    PathUtils.getAppDir().getPath()+File.separator+"vlc"+File.separator
            );
        } catch (Exception e) {
            return new File("");
        }
    }
    public File getSystemVLCDir(){
        final String currentDir = System.getProperty("user.dir");
        final String SO = System.getProperty("os.name").toLowerCase();
        File file = new File("");
        if(SO.contains("win")) file = new File("C:\\Program Files\\VideoLAN\\VLC");
        return file;
    }
}
