package utils;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;

public class Test {
    static void printMediaPlayerEvents(MediaPlayer mediaPlayer){
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventListener() {
            @Override
            public void mediaChanged(MediaPlayer mediaPlayer, MediaRef mediaRef) {
                System.out.println("EVENT: MEDIACHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(mediaRef);
            }

            @Override
            public void opening(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: OPENING");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void buffering(MediaPlayer mediaPlayer, float v) {
                System.out.println("EVENT: BUFFERING");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(v);
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: PLAYING");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: PAUSED");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: STOPPED");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void forward(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: FORWARD");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void backward(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: BACKWARD");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: FINISHED");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long l) {
                System.out.println("EVENT: TIMECHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(l);
            }

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float v) {
                System.out.println("EVENT: POSITIONCHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(v);
            }

            @Override
            public void seekableChanged(MediaPlayer mediaPlayer, int i) {
                System.out.println("EVENT: SEEKABLECHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void pausableChanged(MediaPlayer mediaPlayer, int i) {
                System.out.println("EVENT: PAUSABLECHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void titleChanged(MediaPlayer mediaPlayer, int i) {
                System.out.println("TITLECHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String s) {
                System.out.println("EVENT: SANPSHOTAKER");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(s);
            }

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long l) {
                System.out.println("EVENT: LENGHTCHANGED");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(l);
            }

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int i) {
                System.out.println("EVENT: videoOutput");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void scrambledChanged(MediaPlayer mediaPlayer, int i) {
                System.out.println("EVENT: scrabledChanged");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType trackType, int i) {
                System.out.println("EVENT: elementaryStreamAdded");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(trackType);
                System.out.println(i);
            }

            @Override
            public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType trackType, int i) {
                System.out.println("EVENT: elementaryStreamDeleted");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(trackType);
                System.out.println(i);
            }

            @Override
            public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType trackType, int i) {
                System.out.println("EVENT: elementaryStreamSelected");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(trackType);
                System.out.println(i);
            }

            @Override
            public void corked(MediaPlayer mediaPlayer, boolean b) {
                System.out.println("EVENT: corked");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(b);
            }

            @Override
            public void muted(MediaPlayer mediaPlayer, boolean b) {
                System.out.println("EVENT: muted");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(b);
            }

            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float v) {
                System.out.println("EVENT: volumenChanged");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(v);
            }

            @Override
            public void audioDeviceChanged(MediaPlayer mediaPlayer, String s) {
                System.out.println("EVENT: audioDeviceChanged");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(s);
            }

            @Override
            public void chapterChanged(MediaPlayer mediaPlayer, int i) {
                System.out.println("EVENT: chapterChanged");
                System.out.println(mediaPlayer.audio().volume());
                System.out.println(i);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("EVENT: error");
                System.out.println(mediaPlayer.audio().volume());
            }

            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                System.out.println(mediaPlayer);
            }
        });
    }
}
