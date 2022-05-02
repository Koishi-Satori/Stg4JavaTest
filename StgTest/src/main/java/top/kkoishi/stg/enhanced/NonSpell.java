package top.kkoishi.stg.enhanced;

import top.kkoishi.stg.object.Bullet;
import top.kkoishi.stg.object.Entity;

import java.awt.Graphics;
import java.util.ArrayDeque;

public class NonSpell implements Boss.BossAction {

    protected final ArrayDeque<Bullet> bulletBuffer = new ArrayDeque<>(100);

    protected final ArrayDeque<Entity> entityBuffer = new ArrayDeque<>(16);

    @Override
    public void logic () {
        synchronized (bulletBuffer) {
            final ArrayDeque<Bullet> remove = new ArrayDeque<>(8);
            for (final Bullet bullet : bulletBuffer) {
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
        synchronized (entityBuffer) {
            final ArrayDeque<Entity> remove = new ArrayDeque<>(8);
            for (final Entity entity : entityBuffer) {
                if (entity.deleteTest()) {
                    remove.offerLast(entity);
                }
            }
            while (!remove.isEmpty()) {
                entityBuffer.remove(remove.removeFirst());
            }
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
        return false;
    }
}
