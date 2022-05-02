package top.kkoishi.stg.env;

import top.kkoishi.game.env.AudioPlayer;
import top.kkoishi.game.env.MethodRef;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KKoishi_
 */
public final class GameManager {
    private GameManager () {
        throw new UnsupportedOperationException();
    }

    public static int difficulty = 1;

    public static final HashMap<String, AudioManager> PLAYERS = new HashMap<>(64);

    public static final int TITLE = -1;

    public static final int DIFFICULTY = 0;

    public static final int SELECT = 1;

    public static final int GAME = 2;

    public static final int MANUAL = 3;

    public static int audioChannelAmount = 10;

    private static int process = GAME;

    public static MethodRef<Integer> life;

    public static MethodRef<Integer> bomb;

    public static MethodRef<Integer> score;

    @SuppressWarnings("all")
    private static volatile AtomicInteger maxPoint = new AtomicInteger(10000);

    @SuppressWarnings("all")
    private static volatile AtomicInteger graze = new AtomicInteger(0);

    private static BufferedImage background;

    public static int getProcess () {
        return process;
    }

    public static int life () {
        return life.get();
    }

    public static int maxPoint () {
        return maxPoint.get();
    }

    public static int graze () {
        return graze.get();
    }

    public static void setMaxPoint (int maxPoint) {
        GameManager.maxPoint.set(maxPoint);
    }

    public static void setGraze (int graze) {
        GameManager.graze.set(graze);
    }

    public static int bomb () {
        return bomb.get();
    }

    public static int score () {
        return score.get();
    }

    public static void setProcess (int process) {
        GameManager.process = process;
    }

    public static void paintSideBackground (BufferedImage image) {
        GraphicsManager.instance.get().drawImage(image, StageManager.areaWidth, StageManager.areaHeight, null);
    }

    public static void setBackground (BufferedImage image) {
        GraphicsManager.instance.get().drawImage(image, 0, 0, null);
        background = image;
    }

    public static void paintBack () {
        if (background != null) {
            GraphicsManager.instance.get().drawImage(background, 0, 0, null);
        }
    }

    public static void initialSounds () {
        PLAYERS.forEach((key, m) -> Arrays.stream(m.players).forEach(ap -> ap.adjustClipSettings(FloatControl.Type.MASTER_GAIN, -5.0f)));
        for (final AudioPlayer ele : PLAYERS.get("stage_1_bg").players) {
            ele.adjustClipSettings(FloatControl.Type.MASTER_GAIN, 6.02060f);
        }
        for (final AudioPlayer ele : PLAYERS.get("player_hit").players) {
            ele.adjustClipSettings(FloatControl.Type.MASTER_GAIN, 3.02060f);
        }
    }

    public static AudioManager getAudioManager (File src) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        final AudioManager audioManager = new AudioManager();
        for (int i = 0; i < audioManager.players.length; i++) {
            audioManager.players[i] = AudioPlayer.openAudioChannel(src);
        }
        return audioManager;
    }

    public static void playSound (String key) {
        PLAYERS.get(key).play();
    }

    public static void loopSound (String key) {
        PLAYERS.get(key).play();
    }

    static final class AudioManager {
        private final AudioPlayer[] players = new AudioPlayer[10];

        private final ArrayDeque<Integer> reuseList = new ArrayDeque<>(10);

        AudioManager () {
            for (int i = 0; i < 10; i++) {
                reuseList.offerLast(i);
            }
        }

        void play () {
            synchronized (reuseList) {
                int i = 0;
                for (AudioPlayer player : players) {
                    if (!player.isPlaying()) {
                        reuseList.offerLast(i);
                    }
                    ++i;
                }
                if (reuseList.isEmpty()) {
                    return;
                }
                players[reuseList.removeFirst()].play();
            }
        }
    }
}
