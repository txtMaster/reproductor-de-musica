package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ExplorerUtils {
    public static List<File> getAudios(File directory){
        return Arrays
                .stream(Objects.requireNonNull(directory.listFiles()))
                .filter(
                        file->{
                            String name = file.getName().toLowerCase();
                            return PlayerUtils.EXTENSIONS.stream().anyMatch(name::endsWith);
                        }

                ).sorted()
                .toList();
    }
}
