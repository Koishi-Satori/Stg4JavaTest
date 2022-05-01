package top.kkoishi.stg.object.bullets;

import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Collisionable;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class BasicLaser extends Bullet {
    protected BasicLaser (long uuid, BufferedImage image, Point pos, double r) {
        super(uuid, "Common Laser", image, pos, r);
        if (length * 2 < image.getHeight()) {
            throw new IllegalArgumentException("The length of BasicLaser must be bigger than half of the source image.");
        }
        setLength(24);
    }

    protected double length;

    protected double direct;

    protected int speed = 4;

    public void setLength (double length) {
        this.length = length;
        super.image = stretch(image, length);
    }

    protected strictfp static BufferedImage stretch (BufferedImage origin, double length) {
        final BufferedImage nImage = new BufferedImage(origin.getWidth(), (int) length * 2, origin.getType());
        final Graphics2D g2d = nImage.createGraphics();
        g2d.drawImage(origin, null, 0, (int) (origin.getHeight() / 2 - length / 2));
        double rest = length * 2 - origin.getHeight();
        double cutted = 0;
        int y = origin.getHeight();
        final double dx = origin.getWidth() / rest;
        rest /= 2;
        while (rest > 0) {
            g2d.drawImage(origin.getSubimage((int) cutted, 0, (int) (rest / 2), 1), (int) cutted, y, null);
            rest -= dx;
            cutted += dx;
            ++y;
        }
        return nImage;
    }

    public double length () {
        return length;
    }

    @Override
    public int speed () {
        return speed;
    }

    @Override
    public void move () {

    }

    @Override
    public boolean deleteTest () {
        return true;
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
        //TODO:finish laser.
        return false;
    }
}
