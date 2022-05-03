package top.kkoishi.stg.env;

import top.kkoishi.stg.StgTest;
import top.kkoishi.stg.enhanced.Boss;
import top.kkoishi.stg.enhanced.Dialog;
import top.kkoishi.stg.enhanced.SpellCard;
import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Enemy;
import top.kkoishi.stg.object.Item;
import top.kkoishi.stg.object.RenderAccess;
import top.kkoishi.stg.object.SideBar;

import java.awt.*;
import java.util.ArrayDeque;

/**
 * @author KKoishi_
 */
public final class RepaintManager {
    private RepaintManager () {
    }

    private static Boss boss = null;

    private static Boss.BossAction action = null;

    public static final ArrayDeque<Enemy> ENEMIES = new ArrayDeque<>(200);

    public static final ArrayDeque<Bullet> BULLETS = new ArrayDeque<>(200);

    public static final ArrayDeque<Item> ITEMS = new ArrayDeque<>(200);

    public static SideBar sideBar;

    public static void add (Bullet bullet) {
        BULLETS.add(bullet);
    }

    public static void add (Item item) {
        ITEMS.add(item);
    }

    public static Boss boss () {
        return boss;
    }

    public static void setBoss (Boss boss) {
        RepaintManager.boss = boss;
    }

    public static ArrayDeque<RenderAccess> getCopy () {
        final ArrayDeque<RenderAccess> cpy = new ArrayDeque<>(BULLETS.size());
        cpy.addAll(BULLETS);
        return cpy;
    }

    public static void clearBullets () {
        synchronized (RepaintManager.class) {
            for (final Bullet bullet : BULLETS) {
                bullet.setPos(0, 0);
            }
        }
    }

    public static void clearEnemies () {
        synchronized (RepaintManager.class) {
            for (final Enemy enemy : ENEMIES) {
                enemy.setLife(-1919810);
            }
        }
    }

    public static Runnable getLogicThread () {
        return () -> {
            synchronized (RepaintManager.class) {
                final ArrayDeque<Bullet> remove = new ArrayDeque<>(64);
                for (final Bullet task : BULLETS) {
                    if (task.deleteTest()) {
                        remove.offerLast(task);
                    }
                }
                while (!remove.isEmpty()) {
                    BULLETS.remove(remove.removeFirst());
                }

                final ArrayDeque<Enemy> removeEnemy = new ArrayDeque<>(16);
                for (final Enemy enemy : ENEMIES) {
                    if (enemy.deleteTest()) {
                        removeEnemy.offerLast(enemy);
                    } else if (enemy.life() <= 0) {
                        enemy.deadAction();
                        removeEnemy.offerLast(enemy);
                    }
                }
                while (!removeEnemy.isEmpty()) {
                    ENEMIES.remove(removeEnemy.removeFirst().deadAction());
                }

                final ArrayDeque<Item> removeItem = new ArrayDeque<>(32);
                for (final Item item : ITEMS) {
                    if (item.deleteTest()) {
                        removeItem.offerLast(item);
                    } else if (item.collectTest()) {
                        item.collect();
                        removeItem.offerLast(item);
                    }
                }
                while (!removeItem.isEmpty()) {
                    ITEMS.remove(removeItem.removeFirst());
                }
                BossLogic:
                if (boss != null) {
                    if (boss.hasMoreAction() || action != null) {
                        if (action == null) {
                            action = boss.getAction();
                            boss.popAction();
                        } else {
                            if (action.isEnded()) {
                                if (boss.hasMoreAction()) {
                                    action = boss.getAction();
                                    while (boss.popAction()) {
                                        System.out.println("Try pop action:" + action);
                                    }
                                } else {
                                    action = null;
                                    break BossLogic;
                                }
                            }
                        }
                        action.logic();
                    }
                }
                //run the player logic.
                StgTest.playerLogic();
            }
        };
    }

    public static Runnable getRenderThread () {
        return () -> {
            {
                for (final Bullet access : BULLETS) {
                    access.move();
                    access.render();
                }
                for (final Enemy enemy : ENEMIES) {
                    synchronized (enemy.bullets) {
                        while (!enemy.bullets.isEmpty()) {
                            BULLETS.offerLast(enemy.bullets.removeLast());
                        }
                    }
                    enemy.render();
                }
                for (final Item item : ITEMS) {
                    item.move();
                    item.render();
                }
            }
            BossRender:
            if (boss != null) {
                boss.dialog().show(GraphicsManager.instance.get());
                if (boss.deleteTest()) {
                    boss.deadAction();
                    boss = null;
                    break BossRender;
                }
                boss.render();
                if (boss.hasMoreAction() || action != null) {
                    final Graphics g = GraphicsManager.instance.get();
                    if (action == null) {
                        break BossRender;
                    }
                    if (action.isSpellCard() && boss.needShowCg()) {
                        boss.showCg();
                        final String spellCardName = ((SpellCard) action).name();
                        g.drawString(spellCardName, StageManager.areaWidth - spellCardName.length() * 25, 50);
                        g.drawString(boss.name(), 10, 50);
                    }
                    action.render(g);
                }
            }
            if (GameManager.getProcess() == GameManager.GAME) {
                sideBar.render();
            }
        };
    }
}
