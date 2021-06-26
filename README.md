# SimpleCache
Lightweight simple caching solution


Caching Function
Implement the following interface for a class that caches the results of a function.

public interface Cache&lt;K, V&gt; {
    V get(K key);
}

Constraints:
1. The implementation will return V from an internal collection if the value is cached otherwise it will call a
provided Function&lt;K, V&gt; to get the value.
2. The implementation should allow the user of this class to provide a Function&lt;K, V&gt; that is used to obtain the
value.
3. Important that for any unique instance of K the function is only called once.
4. How to handle null K and V is within your prerogative as is, what happens if Function&lt;K, V&gt; throws, however
we do need to know your design choices and why in the interview.
5. Threading constraints: -
a. The function is assumed thread-safe so for different values of K it may be called concurrently.
b. #3 should never be violated so if 2 or more threads have a cache miss on the same key then only 1
may call the function, the other threads must wait efficiently and return the cached value once the
winner has called the function and obtained a value.

The above may be implemented with a “Map.computeIfAbsent” however we are interested in how you would
implement this.
