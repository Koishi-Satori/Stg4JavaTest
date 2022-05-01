package top.kkoishi.game.env;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class AudioPlayer {
    private final Clip clip;

    public static AudioPlayer openAudioChannel (File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        final Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
        clip.open(ais);
        clip.setLoopPoints(0, -1);
        return new AudioPlayer(clip);
    }

    private AudioPlayer (Clip clip) {
        this.clip = clip;
    }

    public boolean isPlaying () {
        return clip.isActive();
    }

    public void play () {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop () {
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void loop (int amount) {
        clip.setFramePosition(0);
        clip.loop(amount);
    }

    public void stop () {
        clip.stop();
    }

    public void close () {
        clip.close();
    }

    public void adjustClipSettings (FloatControl.Type type, float value) {
        final FloatControl control = (FloatControl) clip.getControl(type);
        control.setValue(value);
    }
}
