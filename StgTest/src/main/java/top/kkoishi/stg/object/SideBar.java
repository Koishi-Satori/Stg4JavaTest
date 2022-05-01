package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GameManager;
import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.StageManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SideBar implements RenderAccess {

    protected BufferedImage life;

    protected BufferedImage bomb;

    protected String point;

    protected String score;

    protected String graze;

    protected String lifeText;

    protected String bombText;

    public SideBar (BufferedImage life, BufferedImage bomb) {
        this.life = life;
        this.bomb = bomb;
    }

    @Override
    public long uuid () {
        return -1145141919810L;
    }

    @Override
    public String name () {
        return "SideBar";
    }

    @Override
    public void render () {
//        //draw sidebar background will be handled by the stage instance.
//        final Graphics g = GraphicsManager.instance.get().create();
//        //render the life amount.
//        final int height = StageManager.areaHeight;
//        final int x = StageManager.areaWidth + 30;
//        final int fontSize = height / 30;
//        g.setFont(new Font("幼圆", Font.ITALIC, fontSize));
//        g.drawString(score + "  " + GameManager.score(), x, height / 10);
//        g.drawString(lifeText, x, height * 7 / 30);
//        int r = GameManager.life();
//        for (int i = 0; i < r; i++) {
//            g.drawImage(life, x + (10 + life.getWidth()) * i, height * 7 / 30, null);
//        }
//        g.drawString(bombText, x, height * 13 / 30);
//        r = GameManager.bomb();
//        for (int i = 0; i < r; i++) {
//            g.drawImage(bomb, x + (10 + life.getWidth()) * i, height * 13 / 30, null);
//        }
//        g.drawString(point + "    " + GameManager.maxPoint(), x, height * 17 / 30);
//        g.drawString(graze + "    " + GameManager.graze(), x, height * 2 / 3);
    }

    @Override
    public int renderType () {
        return 0;
    }

    @Override
    public void prepareRepaint () {

    }
}
