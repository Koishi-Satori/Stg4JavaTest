package top.kkoishi.stg.enhanced;

import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Entity;

import java.awt.Graphics;
import java.util.ArrayDeque;

/**
 * @author KKoishi_
 */
public class SpellCard implements Boss.BossAction {

    protected final ArrayDeque<Bullet> bulletBuffer = new ArrayDeque<>(100);

    protected final ArrayDeque<Entity> entityBuffer = new ArrayDeque<>(16);

    public SpellCard () {
        init();
    }

    public void init () {
    }

    public String name () {
        return "Boss Spell Card";
    }

    @Override
    public void logic () {
        synchronized (bulletBuffer) {
            final ArrayDeque<Bullet> remove = new ArrayDeque<>(8);
            for (final Bullet bullet : bulletBuffer) {
                System.out.println(bullet.centre());
                if (bullet.deleteTest()) {
                    remove.offerLast(bullet);
                } else if (test(bullet)) {
                    hitPlayer();
                    remove.offerLast(bullet);
                }
            }
            while (!remove.isEmpty()) {
                bulletBuffer.remove(remove.removeFirst());
            }
            bulletBuffer.forEach(Bullet::move);
        }
    }

    @Override
    public void render (Graphics g) {
        synchronized (bulletBuffer) {
            bulletBuffer.forEach(Bullet::render);
        }
        synchronized (entityBuffer) {
            entityBuffer.forEach(Entity::render);
        }
    }

    @Override
    public boolean isSpellCard () {
        return true;
    }

    @Override
    public String toString () {
        return "SpellCard{" +
                "bulletBuffer=\n\t" + bulletBuffer +
                "\n\t, entityBuffer=" + entityBuffer +
                "}";
    }
}
