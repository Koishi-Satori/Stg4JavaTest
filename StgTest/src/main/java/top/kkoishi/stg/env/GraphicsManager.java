package top.kkoishi.stg.env;

import java.awt.Graphics;

public final class GraphicsManager {

    public static GraphicsManager instance = null;

    public static int maxHeight = 0;

    public static void initialInstance (Graphics g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        instance = new GraphicsManager(g);
    }

    private final Graphics g;

    private GraphicsManager (Graphics g) {
        this.g = g;
    }

    public Graphics get () {
        return g;
    }
}
