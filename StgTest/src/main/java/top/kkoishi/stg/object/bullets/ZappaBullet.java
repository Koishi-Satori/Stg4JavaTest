package top.kkoishi.stg.object.bullets;

import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Bullet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ZappaBullet extends Bullet {

    private static final BufferedImage TEXTURE;

    private static final BufferedImage RED_TEXTURE;

    private static final BufferedImage PURPLE_TEXTURE;

    private static final BufferedImage PINK_TEXTURE;

    private static final BufferedImage NULL_TEXTURE;

    public static BufferedImage getTexture (int type) {
        return switch (type) {
            case RED:
                yield RED_TEXTURE;
            case PURPLE:
                yield PURPLE_TEXTURE;
            case PINK:
                yield PINK_TEXTURE;
            case NULL:
                yield NULL_TEXTURE;
            default:
                throw new IllegalArgumentException("The type " + type + " does not exist.");
        };
    }

    static {
        try {
            TEXTURE = ImageIO.read(new File("./data/bullets/zappa_bullets.png")).getSubimage(1, 1, 14, 63);
            RED_TEXTURE = TEXTURE.getSubimage(3, 48, 10, 15);
            PURPLE_TEXTURE = TEXTURE.getSubimage(3, 33, 10, 15);
            PINK_TEXTURE = TEXTURE.getSubimage(3, 17, 10, 15);
            NULL_TEXTURE = TEXTURE.getSubimage(3, 1, 10, 15);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static final int RED = 0;

    public static final int PURPLE = 1;

    public static final int PINK = 2;

    public static final int NULL = 114;

    public int speed = 3;

    @Override
    public void setSpeed (int speed) {
        this.speed = speed;
    }

    public static ZappaBullet getInstance (int type, int x, int y, int r) {
        final ZappaBullet inst;
        return switch (type) {
            case RED: {
                yield new ZappaBullet(1145141919810L, RED_TEXTURE, new Point(x, y), r);
            }
            case PURPLE: {
                yield new ZappaBullet(1145141919810L, PURPLE_TEXTURE, new Point(x, y), r);
            }
            case PINK: {
                yield new ZappaBullet(1145141919810L, PINK_TEXTURE, new Point(x, y), r);
            }
            case NULL: {
                yield new ZappaBullet(1145141919810L, NULL_TEXTURE, new Point(x, y), r);
            }
            default:
                throw new IllegalArgumentException("The type should be one of next:RED, PURPLE, PINK, NULL, but get:" + type);
        };
    }

    private ZappaBullet (long uuid, BufferedImage image, Point pos, double r) {
        super(uuid, "Zappa Bullet", image, pos, r);
    }

    @Override
    public void setPoint2player (boolean point2player) {
        super.setPoint2player(point2player);
        direction = super.move2player();
        degree = -1 * direction;
        super.point2player = false;
    }

    @Override
    public boolean deleteTest () {
        // default condition:if the centre point is out of the border, the set to true.
        synchronized (this) {
            final int x = super.pos.x;
            final int y = super.pos.y;
            return x > StageManager.areaWidth || x < 10 || y > StageManager.areaHeight || y < 30;
        }
    }

    @Override
    public void move () {
        if (!point2player) {
            super.pos.x += StrictMath.sin(StrictMath.toRadians(direction)) * speed;
            super.pos.y += StrictMath.cos(StrictMath.toRadians(direction)) * speed;
        } else {
            direction = super.move2player();
            degree = direction + 90;
            point2player = false;
            move();
        }
    }

    @Override
    public int speed () {
        return speed;
    }

    @Override
    protected void render0 (Graphics g) {
        if (direction != 0) {
            final BufferedImage rotated = GraphicsManager.rotate(super.image, StrictMath.toRadians(degree));
            g.drawImage(rotated, pos.x - rotated.getWidth() / 2, pos.y - rotated.getHeight() / 2, null);
        } else {
            g.drawImage(super.image, pos.x - image.getWidth() / 2, pos.y - image.getHeight() / 2, null);
        }
    }

    @Override
    protected void repaint0 (Graphics g) {
        if (degree != 0) {
            GraphicsManager.cover(StageManager.cur, StrictMath.toRadians(degree), image.getWidth() * 4, image.getHeight() * 4, pos.x, pos.y);
        } else {
            final int w = image.getWidth();
            final int h = image.getHeight();
            StageManager.cur.partRender(pos.x - w / 2, pos.y - h / 2, w, h);
        }
    }
}
