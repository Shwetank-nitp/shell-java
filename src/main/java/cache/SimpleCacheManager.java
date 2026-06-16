package cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleCacheManager implements CacheManager {
    private record ClassHolder<K, V> (
            Class<K> keyClass,
            Class<V> valueClass,
            Cache<K, V> cache
    ) {}

    final List<ClassHolder<?, ?>> caches;

    public SimpleCacheManager() {
        this.caches = new ArrayList<>();
    }

    @Override
    public <K, V> int register(Class<K> keyClass, Class<V> valClass, Cache<K, V> cache) {
        int curr = caches.size();
        caches.add(new ClassHolder<>(
                keyClass,
                valClass,
                cache
        ));
        return curr;
    }

    private <K, V> void validateType(
            ClassHolder<?, ?> holder,
            Class<K> keyClass,
            Class<V> valueClass
    ) throws IllegalArgumentException {
        if (holder.keyClass() != keyClass ||
                holder.valueClass() != valueClass) {
            throw new IllegalArgumentException(
                    "Cache type mismatch");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V getCache(
            int cacheId,
            Class<K> keyClass,
            Class<V> valueClass,
            K key) throws IllegalArgumentException {

        ClassHolder<?, ?> holder = caches.get(cacheId);
        validateType(holder, keyClass, valueClass);


        Cache<K, V> cache =
                (Cache<K, V>) holder.cache();

        return cache.getValue(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> void putCache(int cacheId, Class<K> kClass, Class<V> vClass, K key, V value) {
        ClassHolder<?, ?> holder = caches.get(cacheId);
        validateType(holder, kClass, vClass);


        Cache<K, V> cache =
                (Cache<K, V>) holder.cache();

        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> void removeCache(int cacheId, Class<K> kClass, Class<V> vClass, K key) {
        ClassHolder<?, ?> holder = caches.get(cacheId);
        validateType(holder, kClass, vClass);


        Cache<K, V> cache =
                (Cache<K, V>) holder.cache();

        cache.removeCache(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Iterator<Map.Entry<K, V>> getIter(int cacheId, Class<K> kClass, Class<V> vClass) {
        ClassHolder<?, ?> holder = caches.get(cacheId);
        validateType(holder, kClass, vClass);


        Cache<K, V> cache =
                (Cache<K, V>) holder.cache();

        return cache.getIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K> boolean containsKey(int cacheId, Class<K> kClass, K key) {
        ClassHolder<?, ?> holder = caches.get(cacheId);
        if (holder.keyClass() != kClass) {
            throw new IllegalArgumentException(
                    "Cache key type mismatch");
        }


        Cache<K, ?> cache =
                (Cache<K, ?>) holder.cache();

        return cache.containsKey(key);
    }
}
