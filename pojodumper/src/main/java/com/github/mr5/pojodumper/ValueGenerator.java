package com.github.mr5.pojodumper;

public interface ValueGenerator<V> {
    public Object generate(V value);
}