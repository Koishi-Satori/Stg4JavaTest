package top.kkoishi.stg.object;

import top.kkoishi.stg.enhanced.Boss;
import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.RepaintManager;
import top.kkoishi.stg.env.Stage;
import top.kkoishi.stg.env.StageManager;

/**
 * This interface is used to mark a class is able to be rendered.
 * You can render an instance by invoking the {@code render} method.
 * <br>An example & How to use:
 * <pre>
 *     The GraphicsManager instance has got a graphics instance for rendering,
 *     and it can render objects to a buffer.({@code e.g. StgTest.buf})
 *     The player instance can use the graphics to render herself and
 *     the bullets shooed.Also, we can use queues to render enemies' bullets,
 *     common bullets, boss, items, and so on.The <code>prepareRepaint()</code>
 *     method is used to repaint the background for next call of render.
 *     The <code>deleteTest() </code>method is used to test if the instance
 *     is needed to be deleted, after deleted you can clear them or just
 *     wait JVM garbage collection. Other method is not important, just used
 *     to differ objects.
 *
 *     <b>Important</b>
 *     The <code>prepareRepaint()</code> method is not used by standard api now,
 *     for its render speed is much slower than directly re-render the
 *     background to the buffer before render objects to the buffer.Also,
 *     some implemented method now just throws <code>UnsupportedOperationException</code>
 *     instead, or they are just deprecated.
 *
 *     <b>How to Use?</b>
 *     During writing test-project, its main class is <code>StgTest.class</code>,
 *     it uses {@code RepaintManager} to manage their render thread and logic thread,
 *     and then scheduled them at fixed rate(16ms).When there exists new bullets
 *     to be added, directly add them to Enemy Bullet Queue by using
 *     <code>RepaintManager.add(Bullet )</code> method. Above is the simplest
 *     way to start, and you can get detailed information reading the Test class.
 * </pre>
 *
 * @author KKoishi_
 * @version 1.0.1
 * @apiNote Some objects' prepareRepaint method will cause
 * {@code UnsupportedOperationException} or they are just simply deprecated.
 * And after 2022.5, the standard invoke method will not invoke the repaint
 * method, but use render method of a Stage instance like
 * <code>StageManager.cur.render()</code> instead.
 * (the StageManager held stage instances, and you can use them to manage
 * stages.)
 * @see StageManager#cur
 * @see RepaintManager#getRenderThread()
 * @see GraphicsManager
 * @see Stage#render()
 * @see Player.PlayerBullet#prepareRepaint()
 * @see Player#prepareRepaint()
 * @see Entity
 * @see Bullet
 * @see Boss
 * @see BaseItem
 * @see Enemy
 * @since 17
 */
public interface RenderAccess {

    /**
     * The bullet type magic number.
     */
    int BULLET = 0;

    /**
     * The enemy type magic number.
     */
    int ENEMY = 1;

    /**
     * The player type magic number.
     */
    int PLAYER = 2;

    /**
     * The item type magic number.
     */
    int ITEM = -1;

    /**
     * The background type magic number.
     */
    int BACKGROUND = -2;

    /**
     * Get the uuid.
     *
     * @return long.
     */
    long uuid ();

    /**
     * Get the name of the instance.
     *
     * @return name;
     */
    String name ();

    /**
     * The render method, and the render thread will invoke this method to render objects.
     */
    void render ();

    /**
     * The render type, must return one of the magic numbers.
     *
     * @return int.
     */
    @SuppressWarnings("unused")
    int renderType ();

    /**
     * The repaint method.
     */
    @SuppressWarnings("unused")
    void prepareRepaint ();

    /**
     * Test if the instance can be removed from the render queue.
     *
     * @return if it is needed to be removed.
     */
    default boolean deleteTest () {
        return false;
    }

    /**
     * Clear render cache.
     */
    default void clear () {
    }
}
