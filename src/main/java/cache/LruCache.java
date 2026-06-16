package cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LruCache<K, V> implements Cache<K, V> {
    int limit;
    int size = 0;

    private final HashMap<K, V> map = new HashMap<>();
    private final HashMap<K, Node<K>> nodeMap = new HashMap<>();

    private final Node<K> tailNode = new Node<>();
    private final Node<K> headNode = new Node<>();

    // LinkList Class
    private static class Node<K> {
        K key;
        Node<K> next;
        Node<K> prev;

        Node(K key) {
            this.key = key;
            this.next = null;
            this.prev = null;
        }

        Node() {
            this.key = null;
            this.next = null;
            this.prev = null;
        }
    }

    // Constructor
    public LruCache(int limit) {
        this.limit = limit;
    }

    private void remove(Node<K> node) {
        var nextNode = node.next;
        var prevNode = node.prev;

        node.next = null;
        if (nextNode != null) {
            nextNode.prev = null;
        }

        node.prev = null;
        if (prevNode != null) {
            prevNode.next = null;
        }

        if (prevNode != null && nextNode != null) {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    private void addFirst(Node<K> node) {
        var nextNode = headNode.next;
        headNode.next = node;
        node.prev = headNode;

        if (nextNode != null) {
            nextNode.prev = node;
            node.next = nextNode;
        }
    }

    private K pop() {
        var prevNode = tailNode.prev;

        if (prevNode == headNode) {
            throw new RuntimeException("Empty list pop exception");
        }

        var prevPrevNode = prevNode.prev;
        prevPrevNode.next = tailNode;
        tailNode.prev = prevPrevNode;

        prevNode.next = null;
        prevNode.prev = null;

        return prevNode.key;
    }

    @Override
    public void put(K key, V value) {
        if (!map.containsKey(key)) {
            size++;
            if (size > limit) {
                K delKey = pop();
                map.remove(delKey);
                size--;
            }

            Node<K> newNode = new Node<>();
            newNode.key = key;

            nodeMap.put(key, newNode);
            addFirst(newNode);

            return;
        }

        if (!nodeMap.containsKey(key)) {
            throw new RuntimeException("Cache fault: Target Node missing in the cache");
        }

        Node<K> targetNode = nodeMap.get(key);

        remove(targetNode);
        addFirst(targetNode);

        map.put(key, value);
        targetNode.key = key;
    }

    @Override
    public V getValue(K key) {
        if (!map.containsKey(key)) return null;

        if (!nodeMap.containsKey(key)) {
            throw new RuntimeException("Cache fault: Target Node missing in the cache");
        }

        Node<K> tnode = nodeMap.get(key);
        remove(tnode);
        addFirst(tnode);

        return map.get(key);
    }

    @Override
    public Iterator<Map.Entry<K, V>> getIterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void removeCache(K key) {
        if (!map.containsKey(key)) return;

        if (!nodeMap.containsKey(key)) {
            throw new RuntimeException("Cache fault: Target Node missing in the cache");
        }

        Node<K> tnode = nodeMap.get(key);

        remove(tnode);
        nodeMap.remove(key);
        map.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
}
