package io.deeplay.core.logic.newlogic;

@FunctionalInterface
public interface QuadFunction<A, B, C, E, R> {
    R apply(A a, B b, C c, E e);
}
