package top.kkoishi.stg.object;

/**
 * @author KKoishi_
 */
public interface RenderAccess {

    int BULLET = 0;

    int ENEMY = 1;

    int PLAYER = 2;

    int ITEM = -1;

    int BACKGROUND = -2;

    /**
     * Get the uuid.
     *
     * @return long.
     */
    long uuid ();

    /**
     * Get the name.
     *
     * @return name;
     */
    String name ();

    /**
     * The render method, and the render thread will invoke this method to render objects.
     */
    void render ();

    /**
     * The render type.
     *
     * @return int.
     */
    int renderType ();

    /**
     * The repaint method.
     */
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
