package com.richardchankiyin.simplecache;

public interface Cache<K, V> {
	V get(K key);
}
