package top.kkoishi.stg.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Item extends Entity implements Collisionable {

    protected double range = 20;

    protected Item (long uuid, String name, BufferedImage image) {
        super(uuid, name + " Item", image);
    }

    @Override
    protected void repaint0 (Graphics g) {
    }

    @Override
    protected void render0 (Graphics g) {

    }

    public abstract void gottenAction ();

    @Override
    public int renderType () {
        return RenderAccess.ITEM;
    }

    @Override
    public double radius () {
        return range;
    }

    @Override
    public Point centre () {
        return super.pos;
    }
}
