package top.kkoishi.game.env;

@FunctionalInterface
public interface MethodRef<T> {
    T get();
}
