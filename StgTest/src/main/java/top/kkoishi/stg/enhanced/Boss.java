package top.kkoishi.stg.enhanced;

import top.kkoishi.stg.object.Enemy;

import java.awt.image.BufferedImage;

public class Boss extends Enemy {
    protected Boss (long uuid, String name, BufferedImage image) {
        super(uuid, name, image);
    }

    @Override
    protected void deadAction0 () {

    }

    @Override
    public void action () {

    }

    @Override
    public void hitAction () {

    }
}
