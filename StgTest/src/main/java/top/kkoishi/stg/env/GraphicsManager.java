package top.kkoishi.stg.env;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    public strictfp static BufferedImage rotate (BufferedImage src, double rotateDegree) {
        //calculate the new height and new width after right rotation.
        final int oldWidth = src.getWidth();
        final int oldHeight = src.getHeight();
        final double sin = StrictMath.sin(rotateDegree);
        final double cos = StrictMath.cos(rotateDegree);
        final BufferedImage inst = new BufferedImage((int) (oldWidth * sin + oldHeight * cos), (int) (oldWidth * cos + oldHeight * sin), src.getType());
        final Graphics2D g = inst.createGraphics();
        g.rotate(rotateDegree, inst.getWidth(), inst.getHeight());
        g.drawImage(src, null, (int) (inst.getWidth() - oldHeight * cos), (int) (oldWidth * sin));
        return inst;
    }

    public strictfp static void cover (Stage stage, double rotateDegree, int w, int h, int x, int y) {
        final double sin = StrictMath.sin(rotateDegree);
        final double cos = StrictMath.cos(rotateDegree);
        final double dw = (w * cos + h * sin);
        final double dh = (w * sin + h * cos);
        stage.partRender((int) (x - dw / 2), (int) (y - dh / 2), (int) dw, (int) dh);
    }
}
