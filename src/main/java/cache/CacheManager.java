package cache;

import java.util.Iterator;
import java.util.Map;

public interface CacheManager {
    <K, V> int register(
            Class<K> kClass,
            Class<V> vClass,
            Cache<K, V> cache);

    <K, V> V getCache(
            int cacheId,
            Class<K> kClass,
            Class<V> vClass,
            K key) throws InterruptedException;

    <K, V> void putCache(
            int cacheId,
            Class<K> kClass,
            Class<V> vClass,
            K key,
            V value);

    <K, V> void removeCache(
            int cacheId,
            Class<K> kClass,
            Class<V> vClass,
            K key);

    <K, V> Iterator<Map.Entry<K, V>> getIter(
            int cacheId,
            Class<K> kClass,
            Class<V> vClass);

    <K> boolean containsKey(
            int cacheId,
            Class<K> kClass,
            K key);
}