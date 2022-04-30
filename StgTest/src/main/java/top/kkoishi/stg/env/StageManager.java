package top.kkoishi.stg.env;

import java.awt.Toolkit;

public final class StageManager {

    public static int stage = 0;

    public static int displayWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static int displayHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static int areaWidth;

    public static int areaHeight;

    private static Stage[] stages = new Stage[6];

    private static final Object LOCK = new Object();

    private static final Stage EMPTY_STAGE = new Stage.EmptyStage();

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

    public static Stage getStage (int index) {
        final Stage stage = stages[index];
        return stage != null ? stage : EMPTY_STAGE;
    }

    public static void setStage (int index, Stage stage) {
        stages[index] = stage;
        System.out.println("Set the " + index + " stage to:" + stage);
    }
}
