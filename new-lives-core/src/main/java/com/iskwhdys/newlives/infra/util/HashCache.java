package com.iskwhdys.newlives.infra.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HashCache<K extends Object, V> {

    private Map<K, V> data = new HashMap<>();
    private Map<K, LocalDateTime> readTime = new HashMap<>();
    private Map<K, LocalDateTime> writeTime = new HashMap<>();
    private int capacity;

    public HashCache(int capacity) {
        this.capacity = capacity;
    }

    public void clear() {
        data.clear();
        readTime.clear();
        writeTime.clear();
    }

    public boolean containsKey(K key) {
        return data.containsKey(key);
    }

    public LocalDateTime getLastReadTime(K key) {
        return readTime.get(key);
    }

    public LocalDateTime getLastWriteTime(K key) {
        return writeTime.get(key);
    }

    public V put(K key, V value) {
        writeTime.put(key, LocalDateTime.now());
        readTime.put(key, LocalDateTime.now());

        data.put(key, value);
        if (readTime.size() > capacity) {
            removeLastReadData();
        }
        return value;
    }

    private void removeLastReadData() {
        readTime.entrySet().stream().min((t1, t2) -> t1.getValue().compareTo(t2.getValue())).ifPresent(old -> {
            data.remove(old.getKey());
            readTime.remove(old.getKey());
            writeTime.remove(old.getKey());
        });
    }

    public V get(K key) {
        V value = data.get(key);
        readTime.put(key, LocalDateTime.now());
        return value;
    }

}
