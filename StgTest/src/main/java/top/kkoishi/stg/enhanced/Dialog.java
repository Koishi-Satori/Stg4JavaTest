package top.kkoishi.stg.enhanced;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

/**
 * @author KKoishi_
 */
public final class Dialog {

    public static final class DialogFrame {
        private final ArrayDeque<Cg> cgs = new ArrayDeque<>();

        String content;

        public DialogFrame (String content) {
            this.content = content;
        }

        public String content () {
            return content;
        }

        public void setContent (String content) {
            this.content = content;
        }
    }

    Point backgroundRenderPos;

    BufferedImage background;



    private final ArrayDeque<DialogFrame> frames = new ArrayDeque<>();

    public Dialog (Point backgroundRenderPos, BufferedImage background) {
        this.backgroundRenderPos = backgroundRenderPos;
        this.background = background;
    }

    public BufferedImage background () {
        return background;
    }

    public void setBackground (BufferedImage background) {
        this.background = background;
    }

    public void add (DialogFrame frame) {
        frames.offerLast(frame);
    }

    public void show (Graphics g) {
        if (frames.isEmpty()) {
            return;
        }
        final var frame = frames.removeFirst();
        //render frame.
        while (!frame.cgs.isEmpty()) {
            final var cg = frame.cgs.removeFirst();
            g.drawImage(cg.cg, cg.centre.x, cg.centre.y, null);
        }
        g.drawImage(background, backgroundRenderPos.x, backgroundRenderPos.y, null);
        //draw string.
    }

    public boolean isEnd () {
        return frames.isEmpty();
    }

    public void clear () {

    }
}
