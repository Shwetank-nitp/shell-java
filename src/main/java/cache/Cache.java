package cache;

import java.util.Iterator;
import java.util.Map;

public interface Cache<K, V> {
    void put(K key, V value);
    V getValue(K key);
    Iterator<Map.Entry<K, V>> getIterator();
    void removeCache(K key);
    boolean containsKey(K key);
}
