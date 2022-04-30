package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GraphicsManager;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Entire implements RenderAccess {

    protected final long uuid;

    protected String name;

    protected BufferedImage image;

    protected Point pos = new Point(0, 0);

    protected Entire (long uuid, String name, BufferedImage image) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
    }

    public void setName (String name) {
        this.name = name;
    }

    public BufferedImage loadImage (BufferedImage image) {
        final var oldVal = this.image;
        this.image = image;
        System.out.println("load image to:" + image);
        render();
        return oldVal;
    }

    public void setPos (int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    @Override
    public long uuid () {
        return uuid;
    }

    @Override
    public String name () {
        return name;
    }

    @Override
    public void render () {
        render0(GraphicsManager.instance.get());
    }

    @Override
    public void prepareRepaint () {
        repaint0(GraphicsManager.instance.get());
    }

    protected abstract void repaint0 (Graphics g);

    protected abstract void render0 (Graphics g);
}
