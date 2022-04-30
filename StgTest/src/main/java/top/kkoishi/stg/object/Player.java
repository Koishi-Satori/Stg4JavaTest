package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.StageManager;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;

import static top.kkoishi.stg.env.KeyControl.down;
import static top.kkoishi.stg.env.KeyControl.fire;
import static top.kkoishi.stg.env.KeyControl.left;
import static top.kkoishi.stg.env.KeyControl.right;
import static top.kkoishi.stg.env.KeyControl.slow;
import static top.kkoishi.stg.env.KeyControl.up;

/**
 * @author KKoishi_
 */
public class Player extends Entire
        implements Runnable, KeyListener, Collisionable {

    public void setPower (double v) {
        power = v;
    }

    protected static class PlayerBullet extends Bullet
            implements Collisionable {

        protected int speed = 8;

        public static PlayerBullet create (long uuid, BufferedImage bi, int x, int y, double r) {
            return new PlayerBullet(uuid, "PlayerBullet", bi, new Point(x, y), r);
        }

        protected PlayerBullet (long uuid, String name, BufferedImage image, Point pos, double r) {
            super(uuid, name, image, pos, r);
        }

        public void move () {
            final Graphics g = GraphicsManager.instance.get().create();
            final int w = image.getWidth();
            final int h = image.getHeight();
            StageManager.cur.partRender(pos.x - w / 2, pos.y - h / 2, w, h);
            pos.y -= speed;
            render0(g);
        }

        @Override
        protected void render0 (Graphics g) {
            g.drawImage(super.image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2, null);
        }

        @Override
        protected void repaint0 (Graphics g) {
            final int w = image.getWidth();
            final int h = image.getHeight();
            StageManager.cur.partRender(pos.x - w / 2, pos.y - h / 2, w, h);
        }

        @Override
        public double radius () {
            return super.r;
        }

        @Override
        public Point centre () {
            return super.pos;
        }
    }

    protected static final ArrayDeque<PlayerBullet> BULLETS = new ArrayDeque<>(200);

    protected static final AtomicLong INDEX = new AtomicLong(0);

    protected double power = 4.00;

    protected double damage = 2.50;

    protected int speedHigh = 6;

    protected int speedLow = 3;

    protected int speed = speedHigh;

    protected boolean shooting = false;

    protected double r = 2.00;

    protected BufferedImage imageMoveLeft;

    protected BufferedImage imageMoveRight;

    protected BufferedImage bulletImage;

    protected BufferedImage mainBltImage;

    protected double bulletR = 4.00;

    public void setMainBltImage (BufferedImage mainBltImage) {
        this.mainBltImage = mainBltImage;
    }

    public void setBulletImage (BufferedImage bulletImage) {
        this.bulletImage = bulletImage;
        if (mainBltImage == null) {
            mainBltImage = bulletImage;
        }
    }

    public void setImageMoveLeft (BufferedImage imageMoveLeft) {
        this.imageMoveLeft = imageMoveLeft;
    }

    public void setImageMoveRight (BufferedImage imageMoveRight) {
        this.imageMoveRight = imageMoveRight;
    }

    public Player (String name) {
        this(INDEX.getAndIncrement(), name, null);
    }

    protected Player (long uuid, String name, BufferedImage image) {
        super(uuid, name, image);
        if (image != null) {
            render();
        }
    }

    @Override
    public int renderType () {
        return RenderAccess.PLAYER;
    }

    @Override
    public void run () {
        //render player bullets.
        synchronized (BULLETS) {
            remove();
            bulletsRender();
        }
        synchronized (this) {
            final int w = image.getWidth();
            final int h = image.getHeight();
            final int x = pos.x - w / 2;
            final int y = pos.y - h / 2;
            StageManager.cur.partRender(x, y, w, h);
            if (left) {
                if (pos.x - speed > 10) {
                    super.pos.x -= speed;
                }
            }
            if (right) {
                if (pos.x + speed < StageManager.areaWidth) {
                    super.pos.x += speed;
                }
            }
            if (up) {
                if (pos.y - speed > 10) {
                    super.pos.y -= speed;
                }
            }
            if (down) {
                if (pos.y + speed < StageManager.areaHeight) {
                    super.pos.y += speed;
                }
            }
            speed = slow ? speedLow : speedHigh;
            shooting = fire;
            render();
        }
    }

    private void remove () {
        final ArrayDeque<PlayerBullet> remove = new ArrayDeque<>(8);
        for (final PlayerBullet bullet : BULLETS) {
            if (bullet.pos.y < bullet.speed) {
                remove.add(bullet);
                bullet.prepareRepaint();
            }
        }
        while (!remove.isEmpty()) {
            BULLETS.remove(remove.removeFirst());
        }
    }

    public void bulletsRender () {
        synchronized (BULLETS) {
            BULLETS.forEach(PlayerBullet::move);
        }
    }

    public void shot () {
        synchronized (BULLETS) {
            if (shooting) {
                int bltWidth = mainBltImage.getWidth();
                int bltHeight = mainBltImage.getHeight();
                BULLETS.offerLast(PlayerBullet.create(uuid, mainBltImage, pos.x - bltWidth, pos.y - bltHeight, bulletR));
                BULLETS.offerLast(PlayerBullet.create(uuid, mainBltImage, pos.x + bltWidth, pos.y - bltHeight, bulletR));
                if (power >= 1.00) {
                    damage = 3.50;
                } else {
                    damage = 2.50;
                }
                bltWidth = bulletImage.getWidth();
                bltHeight = bulletImage.getHeight();
                if (power >= 2.00) {
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x - bltWidth * 2, pos.y - bltHeight * 3 / 10, bulletR));
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x + bltWidth * 2, pos.y - bltHeight * 3 / 10, bulletR));
                }
                if (power >= 3.00) {
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x - bltWidth * 3, pos.y - bltHeight * 3 / 10, bulletR));
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x + bltWidth * 3, pos.y - bltHeight * 3 / 10, bulletR));
                }
                if (power > 3.99) {
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x - bltWidth * 4, pos.y - bltHeight * 3 / 10, bulletR));
                    BULLETS.offerLast(PlayerBullet.create(uuid, bulletImage, pos.x + bltWidth * 4, pos.y - bltHeight * 3 / 10, bulletR));
                }
            }
        }
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void keyTyped (KeyEvent e) {
    }

    @Override
    public void keyPressed (KeyEvent e) {
        final int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_Z:
                fire = true;
                break;
            case KeyEvent.VK_SHIFT:
                slow = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased (KeyEvent e) {
        keyRelease(e);
    }

    public final void keyRelease (KeyEvent e) {
        synchronized (this) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    up = false;
                    break;
                case KeyEvent.VK_DOWN:
                    down = false;
                    break;
                case KeyEvent.VK_LEFT:
                    left = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = false;
                    break;
                case KeyEvent.VK_SHIFT:
                    slow = false;
                    break;
                case KeyEvent.VK_Z:
                    fire = false;
                    break;
                default:
                    break;
            }
        }
    }


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
        StageManager.cur.render();
    }

    @Override
    protected void render0 (Graphics g) {
        g.drawImage(super.image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2, null);
    }

    @Override
    public String toString () {
        return "Player{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", pos=" + pos +
                ", power=" + power +
                ", damage=" + damage +
                ", speedHigh=" + speedHigh +
                ", speedLow=" + speedLow +
                ", speed=" + speed +
                ", shooting=" + shooting +
                ", r=" + r +
                ", imageMoveLeft=" + imageMoveLeft +
                ", imageMoveRight=" + imageMoveRight +
                ", bulletImage=" + bulletImage +
                ", mainBltImage=" + mainBltImage +
                ", bulletR=" + bulletR +
                '}';
    }
}
