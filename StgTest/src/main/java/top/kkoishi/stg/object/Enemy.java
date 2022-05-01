package top.kkoishi.stg.object;

import top.kkoishi.stg.env.StageManager;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

/**
 * The super class of all the enemies.
 *
 * @author KKoishi_
 */
public abstract class Enemy extends Entity
        implements Collisionable {

    protected int life = 100;

    public int life () {
        return life;
    }

    public Enemy (long uuid , BufferedImage image, double r) {
        super(uuid, "Common Bullet", image);
        this.r = r;
    }

    protected Enemy (long uuid, String name, BufferedImage image) {
        super(uuid, name, image);
        r = (float) StrictMath.min(image.getHeight(), image.getWidth()) / 2;
        super.pos = new Point(20, 20);
    }

    protected double r;

    /**
     * Bullets deque,add and the game thread will auto render them.
     */
    public final ArrayDeque<Bullet> bullets = new ArrayDeque<>(50);

    /**
     * Add a bullet instance to the deque.
     *
     * @param bullet bullet instance.
     */
    public void addBullet (Bullet bullet) {
        bullets.add(bullet);
    }

    /**
     * Render all the bullets.
     */
    public void bulletRender () {
        //lock the bullet queue.
        synchronized (bullets) {
            bullets.forEach(Bullet::render);
        }
    }

    /**
     * The logic of bullets.
     * It will auto delete the bullets out of the border.
     */
    public void bulletLogic () {
        //lock the bullet queue.
        synchronized (bullets) {
            final ArrayDeque<Bullet> remove = new ArrayDeque<>(16);
            for (final Bullet bullet : bullets) {
                if (bullet.deleteTest()) {
                    remove.add(bullet);
                }
            }
            while (!remove.isEmpty()) {
                bullets.remove(remove.removeFirst());
            }
            bullets.forEach(Bullet::move);
        }
    }

    public Enemy deadAction () {
        deadAction0();
        return this;
    }

    protected abstract void deadAction0 ();

    public abstract void action ();

    @Override
    public double radius () {
        return r;
    }

    @Override
    public Point centre () {
        return super.pos;
    }

    @Override
    protected void repaint0 (Graphics g) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int x = pos.x - w / 2;
        final int y = pos.y - h / 2;
        StageManager.cur.partRender(x, y, w, h);
    }

    @Override
    protected void render0 (Graphics g) {
        g.drawImage(super.image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2, null);
    }

    @Override
    public final int renderType () {
        return RenderAccess.BULLET;
    }

    public abstract void hitAction ();
}
