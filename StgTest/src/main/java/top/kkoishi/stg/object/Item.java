package top.kkoishi.stg.object;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Item extends Entity
        implements Collisionable {

    protected double range = 20;

    protected Item (long uuid, String name, BufferedImage image) {
        super(uuid, name + " Item", image);
    }

    public abstract void transport2player ();

    @Override
    protected void repaint0 (Graphics g) {
    }

    @Override
    protected void render0 (Graphics g) {
        g.drawImage(super.image, super.pos.x - super.image.getWidth() / 2,
                super.pos.y - super.image.getHeight() / 2, null);
    }

    /**
     * The movement of the item.
     */
    public abstract void move ();

    /**
     * The action accepted when the item is collected by the player.
     */
    public abstract void collect ();

    /**
     * Test if the item is collected.
     *
     * @return if the item is collected.
     */
    public abstract boolean collectTest ();

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

    /**
     * Test if the item should be deleted.
     *
     * @return if the item should be deleted.
     */
    @Override
    public abstract boolean deleteTest ();
}
