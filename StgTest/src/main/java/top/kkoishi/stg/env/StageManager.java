package top.kkoishi.stg.env;

import top.kkoishi.game.env.MethodRef;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public final class StageManager {

    public static String[] difficulties = {"Easy", "Normal", "Hard", "Lunatic"};

    public static int stage = 0;

    public static int displayWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static int displayHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static int areaWidth;

    public static int areaHeight;

    public static Point initPos = null;

    public static MethodRef<Point> playerPosGetter;

    private static Stage[] stages = new Stage[6];

    private static final Object LOCK = new Object();

    private static final Stage EMPTY_STAGE = new Stage.EmptyStage();

    public static BufferedImage sideBar;

    public static Stage cur = getStage(stage);

    public static void resetStageAmount (int amount) {
        synchronized (LOCK) {
            if (amount < 0) {
                throw new IndexOutOfBoundsException();
            }
            final Stage[] cpy = stages;
            stages = new Stage[amount];
            System.arraycopy(cpy, 0, stages, 0, cpy.length);
        }
    }

    public static boolean hasNextStage () {
        if (stage >= stages.length) {
            return false;
        }
        return stages[stage + 1] != null;
    }

    public static Stage getStage (int index) {
        final Stage stage = stages[index];
        return stage != null ? stage : EMPTY_STAGE;
    }

    public static void setStage (int index, Stage stage) {
        stages[index] = stage;
        System.out.println("Set the " + index + " stage to:" + stage);
    }

    @SuppressWarnings("all")
    public static void stageLogicStart () {
        new Thread(cur).start();
    }
}
