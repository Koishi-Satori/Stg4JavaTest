package top.kkoishi.stg.enhanced;

import top.kkoishi.game.env.Action;
import top.kkoishi.stg.env.RepaintManager;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Bullet;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public final class Bullets {

    public static abstract class SimpleBullet extends Bullet {

        protected int speed;

        public int type;

        protected SimpleBullet (int type, Point pos, double r, int speed) {
            super(114514L, null, null, pos, r);
            this.speed = speed;
            if (type < 0 || type > TYPES.size()) {
                throw new NoSuchElementException("No such bullet type:" + type);
            }
            this.type = type;
        }

        @Override
        public int speed () {
            return speed;
        }

        @Override
        public abstract void move ();

        @Override
        public boolean deleteTest () {
            return super.pos.x < 10 || super.pos.x > StageManager.areaWidth || super.pos.y < 10 || super.pos.y > StageManager.areaHeight;
        }

        @Override
        protected void render0 (Graphics g) {
            final var img = getType(type);
            g.drawImage(img, pos.x - img.getWidth() / 2, pos.y - img.getHeight() / 2, null);
        }

        @Override
        protected void repaint0 (Graphics g) {
        }

        @Override
        public void setSpeed (int speed) {
            this.speed = speed;
        }
    }

    private static final ArrayList<BufferedImage> TYPES = new ArrayList<>();

    private static final ArrayDeque<Bullet> BUFFER = new ArrayDeque<>();

    private static final Object LOCK = new Object();

    private Bullets () {
    }

    public static void addType (BufferedImage image) {
        synchronized (LOCK) {
            TYPES.add(image);
        }
    }

    public static BufferedImage getType (int index) {
        synchronized (LOCK) {
            return TYPES.get(index);
        }
    }

    public static int typeOf (BufferedImage image) {
        synchronized (LOCK) {
            return TYPES.indexOf(image);
        }
    }

    public static ArrayList<BufferedImage> getTypesCopy () {
        synchronized (LOCK) {
            return new ArrayList<>(TYPES);
        }
    }

    /**
     * Get simple bullet instance from given type magic number and other params.
     *
     * @param type       index of image.
     * @param x          x of the bullet's position.
     * @param y          y of the bullet's position.
     * @param speed      speed of the bullet.
     * @param r          the radius used to test if the bullet hit player.
     * @param moveAction how to move.
     * @return bullet instance.
     */
    public static Bullet getBullet (int type, int x, int y, int speed, int r, Action moveAction) {
        return new SimpleBullet(type, new Point(x, y), r, speed) {
            @Override
            public void move () {
                moveAction.action();
            }
        };
    }

    public static void pushBuffer (int type, int x, int y, int speed, int r, Action moveAction) {
        synchronized (LOCK) {
            BUFFER.offerLast(getBullet(type, x, y, speed, r, moveAction));
        }
    }

    public static void flushBuffer () {
        synchronized (LOCK) {
            while (!BUFFER.isEmpty()) {
                RepaintManager.add(BUFFER.removeFirst());
            }
        }
    }
}
