package com.richardchankiyin.simplecache;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CacheResult<V> {

	private V value;
	private boolean computeSuccess = false;
	public CacheResult() {
		
	}
	public CacheResult(V value) {
		if (value != null) {
			this.value = value;
			this.computeSuccess = true;
		}
	}
	
	public V getValue() { return value; }
	public boolean isComputeSuccess() { return this.computeSuccess; }
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
