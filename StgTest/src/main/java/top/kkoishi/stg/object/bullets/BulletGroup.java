package top.kkoishi.stg.object.bullets;

import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Collisionable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BulletGroup extends Bullet {

    protected int speed = 4;

    protected int amount = 5;

    protected int interval = 1;

    protected int index = 0;

    protected BulletGroup (long uuid, String name, BufferedImage image, Point pos, double r) {
        super(uuid, name, image, pos, r);
    }

    @Override
    public int speed () {
        return speed;
    }

    @Override
    public void move () {
        synchronized (this) {
            while (index < amount) {
                move0();
                ++index;
            }
            index = 0;
        }
    }

    /**
     * The actual implement of the move method.
     * Event time invoke this method, the {@code index} field will increase 1.
     * So you can use index field to qualify different bullet.
     *
     * @see BulletGroup#move()
     * @see BulletGroup#index
     */
    protected abstract void move0 ();

    @Override
    public boolean deleteTest () {
        synchronized (this) {
            final int x = super.pos.x;
            final int y = super.pos.y;
            return x > StageManager.areaWidth || x < 10 || y > StageManager.areaHeight || y < 10;
        }
    }

    @Override
    protected void render0 (Graphics g) {

    }

    @Override
    protected void repaint0 (Graphics g) {

    }

    @Override
    public void setSpeed (int speed) {
        this.speed = speed;
    }

    @Override
    public boolean pound (Collisionable another) {
        return super.pound(another);
    }
}
