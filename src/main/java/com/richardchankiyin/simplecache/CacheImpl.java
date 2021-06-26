package com.richardchankiyin.simplecache;

import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheImpl<K,V> implements Cache<K, V> {
	private final static Logger logger = LoggerFactory.getLogger(CacheImpl.class);
	private CacheInternalCollection<K,V> collection = null;
	private Function<? super K, ? extends V> function = null;
	public CacheImpl(Function<? super K, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction, "mapping function cannot be null");
		function = mappingFunction;
		collection = new CacheInternalCollection<K,V>(function);
	}
	
	
	@Override
	public V get(K key) {
		logger.debug("retrieving value from: {} for key: {}", collection, key);
		if (key == null) {
			throw new NullPointerException("key is null");
		}
		V value = collection.storeAndGet(key);
		if (value == null) {
			throw new NullPointerException("no compute result for this key:" + key);
		}
		return value;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
