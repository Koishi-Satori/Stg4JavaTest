package top.kkoishi.stg.env;

import top.kkoishi.stg.object.RenderAccess;

import java.util.ArrayDeque;

public final class RepaintManager {
    private RepaintManager () {
    }

    public static final ArrayDeque<RenderAccess> TASKS = new ArrayDeque<>(200);

    public static void add (RenderAccess renderAccess) {
        TASKS.add(renderAccess);
    }

    public static ArrayDeque<RenderAccess> getCopy () {
        final ArrayDeque<RenderAccess> cpy = new ArrayDeque<>(TASKS.size());
        cpy.addAll(TASKS);
        return cpy;
    }

    public static Runnable getRenderThread () {
        return () -> RepaintManager.getCopy().forEach(RenderAccess::render);
    }
}
