package top.kkoishi.stg.object.enemies;

import top.kkoishi.stg.env.GameManager;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class SimpleTh06Butterfly
        extends Enemy {

    protected final BufferedImage[] images;

    protected volatile int index = 0;

    public SimpleTh06Butterfly (long uuid, BufferedImage ...images) {
        super(uuid, "Simple Th06 Butterfly Enemy", images[0]);
        this.images = images;
    }

    @Override
    public void action () {
    }

    @Override
    public void deadAction0 () {
        GameManager.playSound("enemy_dead");
    }

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
        synchronized (this) {
            super.image = images[index++];
            super.render0(g);
            if (index >= images.length) {
                index = 0;
            }
        }
    }

    @Override
    public void hitAction () {
        if (new Random().nextInt(2) == 1) {
            GameManager.playSound("enemy_damage_0");
        } else {
            GameManager.playSound("enemy_damage_1");
        }
    }
}
