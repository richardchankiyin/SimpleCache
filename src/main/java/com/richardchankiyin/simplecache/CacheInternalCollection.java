package com.richardchankiyin.simplecache;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInternalCollection<K,V> {
	private final static Logger logger = LoggerFactory.getLogger(CacheInternalCollection.class);
	private final Map<K,CacheResult<V>> map;
	private Function<? super K, ? extends V> function = null;
	private static final int DEFAULT_SIZE = 16;
	
	public CacheInternalCollection(int size, Function<? super K, ? extends V> mappingFunction) {
		Objects.requireNonNull(mappingFunction, "mapping function cannot be null");
		map = new HashMap<>(size);
		function = mappingFunction;
	}
	
	public CacheInternalCollection(Function<? super K, ? extends V> mappingFunction) {
		this(DEFAULT_SIZE, mappingFunction);
	}
	
	public V storeAndGet(K key) {
		CacheResult<V> result = map.get(key);
		if (result != null) {
			// retrieve from cache result
			return result.getValue();
		} else {
			// store required
			// perform a sync here
			Object o = getSyncObj(key);
			logger.debug("key: {} sync obj: {}", key, o);
			synchronized(o) {
				CacheResult<V> syncresult = map.get(key);
				
				if (syncresult != null) {
					return syncresult.isComputeSuccess() ? syncresult.getValue() : null;
				} else {
					CacheResult<V> creationresult = null;
					try {
						V v = function.apply(key);
						creationresult = new CacheResult<V>(v);
					}
					catch (Throwable t) {
						logger.error("function computing result for key:" + key + " failed", t);
						creationresult = new CacheResult<V>();
					}
					CacheResult<V> preresult = map.put(key, creationresult);
					if (preresult != null) {
						logger.warn("previous compute value found for key: {} with value: {}!!!!", key, preresult);
					} else {
						logger.debug("previous compute value put for key: {} with value: {}", key, preresult);
					}
					return creationresult.isComputeSuccess() ? creationresult.getValue() : null;
				}
			} // end sync
		}
	}
	
	private Object getSyncObj(K key) {
		// return hashCode value in string format and interned to
		// have a sync operation parameter for keys not the same
		// but they are still equals to each others
		return String.valueOf(key.hashCode()).intern();
	}
	
}


