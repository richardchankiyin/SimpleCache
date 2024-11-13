package com.richardchankiyin.simplecache;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheImpl<K,V> implements Cache<K, V> {
	private final static Logger logger = LoggerFactory.getLogger(CacheImpl.class);
	private ConcurrentHashMap<K,V> ic = null;
	
	private Function<? super K, ? extends V> function = null;
	public CacheImpl(Function<? super K, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction, "mapping function cannot be null");
		function = mappingFunction;
		ic = new ConcurrentHashMap<>();
	}
	
	@Override
	public V get(K key) {
		logger.debug("retrieving value from: {} for key: {}", ic, key);
		if (key == null) {
			throw new NullPointerException("key is null");
		}
		
		return ic.computeIfAbsent(key, k -> function.apply(k));
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
