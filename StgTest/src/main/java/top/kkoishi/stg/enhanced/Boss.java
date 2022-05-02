package top.kkoishi.stg.enhanced;

import top.kkoishi.stg.object.Collisionable;
import top.kkoishi.stg.object.Enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Objects;

public abstract class Boss
        extends Enemy {

    public interface BossAction {
        void logic ();

        void render (Graphics g);

        boolean isSpellCard ();

        default boolean test (Collisionable c) {
            Objects.requireNonNull(c);
            return false;
        }

        default void hitPlayer () {
        }

        default boolean isEnded () {
            return false;
        }
    }

    /**
     * Represent the boss instance is acting non-spell.
     */
    public static final int NON_SPELL = 0;

    /**
     * Represent the boss instance is acting spell-card.
     */
    public static final int SPELL_CARD = 1;

    /**
     * Represent the boss instance is dead.
     */
    public static final int DEAD = 2;

    protected final ArrayDeque<BossAction> actions = new ArrayDeque<>(16);

    protected Boss (long uuid, String name, BufferedImage image) {
        super(uuid, name, image);
        super.life = 1000;
        super.r = 30;
    }

    public void setDefineRound (double r) {
        super.r = r;
    }

    /**
     * The dialog process.
     */
    public abstract Dialog dialog ();

    public void addAction (BossAction action) {
        synchronized (actions) {
            actions.add(action);
        }
    }

    /**
     * Show cg before the spell card is released.
     */
    public abstract void showCg ();

    public boolean needShowCg () {
        return false;
    }

    public abstract void resetNeedShowCg ();

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void deadAction0 ();

    @Override
    public void action () {
    }

    public boolean hasMoreAction () {
        synchronized (actions) {
            return !actions.isEmpty();
        }
    }

    public boolean popAction () {
        synchronized (actions) {
            if (actions.isEmpty()) {
                return false;
            } else {
                actions.removeFirst();
                return true;
            }
        }
    }

    public BossAction getAction () {
        synchronized (actions) {
            if (actions.isEmpty()) {
                return null;
            } else {
                return actions.element();
            }
        }
    }

    /**
     * The state of the boss instance.
     *
     * @return magic number, one of NON_SPELL, SPELL_CARD and DEAD.
     * @see Boss#NON_SPELL
     * @see Boss#SPELL_CARD
     * @see Boss#DEAD
     */
    public abstract int state ();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void hitAction ();

    /**
     * The additional render process occurs while the {@code render0(Graphics)} method is invoking.
     *
     * @param g Graphics instance.
     * @see Boss#render0(Graphics)
     */
    protected abstract void renderAddition (Graphics g);

    @Override
    protected void render0 (Graphics g) {
        super.render0(g);
        renderAddition(g);
    }

    @Override
    public boolean deleteTest () {
        return actions.isEmpty() && super.life <= 0;
    }

    @Override
    public String toString () {
        return "Boss{" +
                "actions=" + actions +
                "\n, life=" + life +
                ", r=" + r +
                ", bullets=" + bullets +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", pos=" + pos +
                '}';
    }
}
