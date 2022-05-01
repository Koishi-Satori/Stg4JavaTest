package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.StageManager;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Bullet implements RenderAccess, Collisionable {

    protected final long uuid;

    protected String name;

    protected BufferedImage image;

    protected Point pos;

    protected double r;
    
    protected boolean point2player = false;

    /**
     * Direction degree.
     */
    public int direction = 0;

    /**
     * Rotate degree.
     */
    public int degree = 0;

    public void setPoint2player (boolean point2player) {
        this.point2player = point2player;
    }

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
    
    protected strictfp final int move2player () {
        final Point playerPos = StageManager.playerPosGetter.get();
        System.out.println("Moving to player.From " + pos + " to " + playerPos);
        final double tgRec = (((float) this.pos.x - playerPos.x) / (this.pos.y - playerPos.y));
        System.out.println(tgRec);
        if (Double.isInfinite(StrictMath.abs(tgRec))) {
            if (this.pos.x < playerPos.x) {
                return (int) StrictMath.toDegrees(StrictMath.PI / 2);
            } else {
                return (int) StrictMath.toDegrees(-StrictMath.PI / 2);
            }
        }
        final double degree = StrictMath.atan(tgRec);
        System.out.println(degree);
        if (degree < 0) {
            if (this.pos.y > playerPos.y) {
                return (int) StrictMath.toDegrees(degree - StrictMath.PI / 2);
            }
        } else {
            if (this.pos.y > playerPos.y) {
                return (int) StrictMath.toDegrees(degree + StrictMath.PI / 2);
            }
        }
        return (int) StrictMath.toDegrees(degree);
    }

    public abstract int speed ();

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

    }

    public abstract void move ();

    /**
     * Test if the bullet will be deleted.
     *
     * @return true if it is needed to be deleted.
     */
    @Override
    public abstract boolean deleteTest ();

    /**
     * The actual render method.
     *
     * @see GraphicsManager#get()
     * @param g inst(GraphicsManager.instance.get())
     */
    protected abstract void render0 (Graphics g);

    protected abstract void repaint0 (Graphics g);

    @Override
    public double radius () {
        return r;
    }

    @Override
    public Point centre () {
        return pos;
    }

    public abstract void setSpeed (int speed);
}
