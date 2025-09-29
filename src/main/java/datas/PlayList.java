package datas;

import org.jaudiotagger.audio.AudioFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayList extends ArrayList<SongData> {
    private final List<SongData> songs = new ArrayList<SongData>();

    public PlayList(){
        super();
    }

    public void strictBind(Object any, Runnable callback){
        callback.run();
        any = null;
    }

    public boolean add(AudioFile audio){
        SongData song = new SongData(audio);
        boolean result = super.add(song);
        if(result) onAdd.accept(song,audio);
        return result;
    }

    public boolean add(SongData song){
        boolean result = super.add(song);
        if(result) onAdd.accept(song,null);
        return result;
    }

    public boolean remove(SongData song){
        boolean result = super.remove(song);
        if(result) onRemove.accept(song);
        return result;
    }

    public void selected(SongData song){
        if(this.contains(song))onSelectSong.accept(song);
    }

    public Consumer<SongData> onSelectSong = (SongData song)->{};

    public BiConsumer<SongData,AudioFile> onAdd = (SongData song, AudioFile audioFile)->{};
    public Consumer<SongData> onRemove = (SongData song)->{};
}
