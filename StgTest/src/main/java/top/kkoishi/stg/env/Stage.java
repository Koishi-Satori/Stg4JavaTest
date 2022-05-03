package top.kkoishi.stg.env;

import top.kkoishi.game.env.Action;
import top.kkoishi.stg.object.RenderAccess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Stage
        implements RenderAccess, Runnable {

    @Override
    public void run () {
        action();
        if (StageManager.hasNextStage()) {
            (StageManager.cur = StageManager.getStage(++StageManager.stage)).run();
        }
    }

    /**
     * The actual implementation of the actions in the stage instance.
     */
    protected abstract void action ();

    public static final class EmptyStage extends Stage {

        final Graphics g = GraphicsManager.instance.get().create();

        @Override
        protected void action () {
        }

        private EmptyStage (BufferedImage bi) {
            super(bi);
        }

        public EmptyStage () {
            this(null);
            g.setColor(Color.GRAY);
        }

        @Override
        public void render () {
            g.fillRect(0, 0, StageManager.areaWidth, StageManager.areaHeight);
        }

        @Override
        public void partRender (int x, int y, int w, int h) {
            g.fillRect(x, y, w, h);
        }
    }

    protected final BufferedImage bi;

    protected Stage (BufferedImage bi) {
        this.bi = bi;
    }

    public Image getBackground () {
        return bi;
    }

    @Override
    public long uuid () {
        return 0;
    }

    @Override
    public String name () {
        return null;
    }

    @Override
    public void render () {
        GraphicsManager.instance.get().drawImage(bi, 7, 30, StageManager.displayWidth, StageManager.displayHeight, null);
        if (StageManager.sideBar != null) {
            GraphicsManager.instance.get().drawImage(StageManager.sideBar, StageManager.areaWidth + 7, 30, null);
        }
    }

    public void draw (Graphics g) {
        g.drawImage(bi, 7, 30, null);
    }

    @Override
    public int renderType () {
        return RenderAccess.BACKGROUND;
    }

    @Override
    @Deprecated
    public void prepareRepaint () {
        throw new UnsupportedOperationException();
    }

    public void partRender (int x, int y, int w, int h) {
        final Graphics g = GraphicsManager.instance.get();
        g.drawImage(bi, x, y, w, h, null);
    }

    @Override
    public String toString () {
        return "Stage{" +
                "background=" + bi +
                ", action=" + (Action) (this::action) +
                '}';
    }
}
