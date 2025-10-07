package utils;

import libraries.demo.application.App;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    static String getRelativePath(String from, String to){
        Path fromPath = Paths.get(from).toAbsolutePath();
        Path toPath = Paths.get(to).toAbsolutePath();
        return fromPath.relativize(toPath).toString();
    }
    static File getAppDir() throws URISyntaxException {
        return new File(
                App.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath()
        ).getParentFile();
    }
}
