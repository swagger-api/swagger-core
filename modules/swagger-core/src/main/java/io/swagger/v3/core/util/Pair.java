package io.swagger.v3.core.util;

/**
 * Replacement for commons-lang3 Pair; provides getLeft/getRight accessors.
 */
public abstract class Pair<L, R> {

    public abstract L getLeft();

    public abstract R getRight();

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }
}
