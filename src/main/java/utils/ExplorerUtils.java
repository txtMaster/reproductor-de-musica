package utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
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
    public static AudioFile getAudioFile(String path) throws
            CannotReadException,
            TagException,
            InvalidAudioFrameException,
            ReadOnlyFileException,
            IOException
    {
        return AudioFileIO.read(new java.io.File(path));
    }
    public static AudioFile getAudioFile(File audio) throws
            CannotReadException,
            TagException,
            InvalidAudioFrameException,
            ReadOnlyFileException,
            IOException
    {
        return getAudioFile(audio.getAbsolutePath());
    }
}
