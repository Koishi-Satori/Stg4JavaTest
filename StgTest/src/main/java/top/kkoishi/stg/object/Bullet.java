package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GraphicsManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Bullet implements RenderAccess {

    protected final long uuid;

    protected String name;

    protected BufferedImage image;

    protected Point pos;

    protected double r;

    public Bullet (long uuid) {
        this.uuid = uuid;
    }

    protected Bullet (long uuid, String name, BufferedImage image, Point pos, double r) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
        this.pos = pos;
        this.r = r;
    }

    public void setName (String name) {
        this.name = name;
    }

    public BufferedImage loadImage (BufferedImage image) {
        final var oldVal = this.image;
        this.image = image;
        render();
        return oldVal;
    }

    @Override
    public long uuid () {
        return uuid;
    }

    @Override
    public String name () {
        return null;
    }

    @Override
    public void render () {
        render0(GraphicsManager.instance.get());
    }

    @Override
    public int renderType () {
        return RenderAccess.BULLET;
    }

    @Override
    public void prepareRepaint () {
        repaint0(GraphicsManager.instance.get());
    }

    protected abstract void render0 (Graphics g);

    protected abstract void repaint0 (Graphics g);
}
