package top.kkoishi.stg;

import top.kkoishi.stg.enhanced.Boss;
import top.kkoishi.stg.enhanced.Dialog;
import top.kkoishi.stg.enhanced.SpellCard;
import top.kkoishi.stg.env.GameManager;
import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Collisionable;
import top.kkoishi.stg.object.bullets.ZappaBullet;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

final class BehaveManager {

    static BufferedImage firstBoss;

    static BufferedImage firstBossCg;

    static Boss firstBossInst;

    public static void initialize (File src, File cgSrc) throws IOException {
        System.out.println("Initialing boss image:" + src.getCanonicalPath());
        firstBoss = ImageIO.read(src);
        System.out.println("Initialing boss image:" + cgSrc.getCanonicalPath());
        firstBossCg = ImageIO.read(cgSrc);
        firstBossInst = new Boss(0L, "???Hakuri Reimu???", firstBoss) {
            private int count = 0;

            @Override
            public Dialog dialog () {
                return new Dialog(null, null);
            }

            @Override
            public void showCg () {
                if (count > 200) {
                    return;
                }
                ++count;
                GraphicsManager.instance.get().drawImage(firstBossCg,
                        StageManager.areaWidth - firstBossCg.getWidth(),
                        StageManager.areaHeight - (count < 100 ? 500 : count * 5),
                        null);
            }

            @Override
            public boolean needShowCg () {
                return count < 200;
            }

            @Override
            public void resetNeedShowCg () {
                count = 0;
            }

            @Override
            protected void deadAction0 () {
                System.out.println("Boss " + this + " is dead.");
            }

            @Override
            public int state () {
                return super.life <= 0 ? Boss.DEAD : super.getAction().isSpellCard() ? SPELL_CARD : NON_SPELL;
            }

            @Override
            public void hitAction () {
                //play hit sound.
                if (new Random().nextInt(2) == 1) {
                    GameManager.playSound("enemy_damage_0");
                } else {
                    GameManager.playSound("enemy_damage_1");
                }
            }

            @Override
            protected void renderAddition (Graphics g) {
                //pass.
            }
        };
        firstBossInst.setDefineRound(51.4);
        firstBossInst.setPos(StageManager.areaWidth / 2, StageManager.areaHeight / 10);
        firstBossInst.addAction(new SpellCard() {
            @Override
            public void init () {
                super.bulletBuffer.offerLast(ZappaBullet.getInstance(ZappaBullet.RED, firstBossInst.centre().x, firstBossInst.centre().y, 6));
            }

            private int frameTimer = 0;

            @Override
            public String name () {
                return "[Test]Test";
            }

            @Override
            public void logic () {
                super.logic();
                //timer++
                if (frameTimer < 3600) {
                    ++frameTimer;
                }
            }

            @Override
            public boolean isEnded () {
                //ended when the timer reaches 3600 frames.
                return frameTimer >= 3600;
            }

            @Override
            public void hitPlayer () {
                StgTest.select.hit();
            }

            @Override
            public boolean test (Collisionable c) {
                return StgTest.select.pound(c);
            }
        });
        System.out.println("Success to load the boss:" + firstBossInst);
    }

    private BehaveManager () {
    }

    public static void moveDown (Bullet bullet) {
        bullet.setY(bullet.getY() + bullet.speed());
    }

    public static Boss firstTestBoss (int x, int y) {
        if (firstBossInst == null) {
            throw new NullPointerException("The first boss has been used.");
        }
        firstBossInst.setPos(x, y);
        return firstBossInst;
    }
}
