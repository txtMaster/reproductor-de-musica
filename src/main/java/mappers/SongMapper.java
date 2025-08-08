package mappers;

import datas.SongData;
import models.SongModel;

public class SongMapper {
    public static SongModel toModel(SongData data) {
        return new SongModel(
                data.getTitle(),
                data.getArtist(),
                data.getDuration(),
                data.getPath(),
                data.getImageData()
        );
    }
}