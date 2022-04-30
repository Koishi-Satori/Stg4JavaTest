package top.kkoishi.stg.object;

public interface RenderAccess {

    int BULLET = 0;

    int ENEMY = 1;

    int PLAYER = 2;

    int ITEM = -1;

    int BACKGROUND = -2;

    long uuid ();

    String name ();

    void render ();

    int renderType ();

    void prepareRepaint ();
}
