package top.kkoishi.stg.object;

import top.kkoishi.stg.env.StageManager;

import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class BaseItem
        extends Item {

    public BaseItem (long uuid, String name, BufferedImage image) {
        super(uuid, name, image);
    }

    protected int speed = 5;

    public int speed () {
        return speed;
    }

    public void setSpeed (int speed) {
        this.speed = speed;
    }


    @Override
    public void move () {
        super.pos.y += speed;
    }

    @Override
    public boolean deleteTest () {
        return super.pos.y > StageManager.areaHeight;
    }
}
