package com.ordadata.pack.charm.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@ToString
@Getter
public class Triple<T, U, V> {
    private final T a;
    private final U b;
    private final V c;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(getA(), triple.getA()) && Objects.equals(getB(), triple.getB()) &&
               Objects.equals(getC(), triple.getC());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getA(), getB(), getC());
    }
}
