package top.kkoishi.stg.env;

import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Enemy;
import top.kkoishi.stg.object.RenderAccess;
import top.kkoishi.stg.object.SideBar;

import java.util.ArrayDeque;

/**
 * @author KKoishi_
 */
public final class RepaintManager {
    private RepaintManager () {
    }

    public static final ArrayDeque<Enemy> ENEMIES = new ArrayDeque<>(200);

    public static final ArrayDeque<Bullet> TASKS = new ArrayDeque<>(200);

    public static SideBar sideBar;

    public static void add (Bullet renderAccess) {
        TASKS.add(renderAccess);
    }

    public static ArrayDeque<RenderAccess> getCopy () {
        final ArrayDeque<RenderAccess> cpy = new ArrayDeque<>(TASKS.size());
        cpy.addAll(TASKS);
        return cpy;
    }

    public static Runnable getLogicThread () {
        return () -> {
            synchronized (TASKS) {
                final ArrayDeque<Bullet> remove = new ArrayDeque<>(64);
                for (final Bullet task : TASKS) {
                    if (task.deleteTest()) {
                        remove.offerLast(task);
                    }
                }
                while (!remove.isEmpty()) {
                    TASKS.remove(remove.removeFirst());
                }
            }
            synchronized (ENEMIES) {
                final ArrayDeque<Enemy> remove = new ArrayDeque<>(16);
                for (final Enemy enemy : ENEMIES) {
                    if (enemy.deleteTest() || enemy.life() <= 0) {
                        remove.offerLast(enemy);
                    }
                }
                while (!remove.isEmpty()) {
                    ENEMIES.remove(remove.removeFirst().deadAction());
                }
            }
        };
    }

    public static Runnable getRenderThread () {
        return () -> {
            synchronized (TASKS) {
                for (final Bullet access : TASKS) {
                    access.move();
                    access.render();
                }
            }
            synchronized (ENEMIES) {
                if (ENEMIES.isEmpty()) {
                    return;
                }
                for (Enemy enemy : ENEMIES) {
                    synchronized (enemy.bullets) {
                        while (!enemy.bullets.isEmpty()) {
                            TASKS.offerLast(enemy.bullets.removeLast());
                        }
                    }
                    enemy.render();
                }
            }
            if (GameManager.getProcess() == GameManager.GAME) {
                sideBar.render();
            }
        };
    }
}
