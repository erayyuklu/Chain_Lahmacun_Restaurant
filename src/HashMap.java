import java.util.ArrayList;
import java.util.List;

// Custom implementation of a HashMap with separate chaining collision resolution
class HashMap<K, V> {
    private static final int INITIAL_CAPACITY = 101; // Initial capacity of the HashMap
    private int capacity; // Current capacity of the HashMap
    private int size; // Number of key-value pairs stored in the HashMap
    private List<List<Entry<K, V>>> buckets; // Buckets to store entries (using a List of Lists)

    // Entry class to hold key-value pairs
    private static class Entry<K, V> {
        K key; // Key of the entry
        V value; // Value associated with the key

        Entry(K key, V value) {
            this.key = key; // Assign the provided key
            this.value = value; // Assign the provided value
        }
    }

    // Constructor to initialize the HashMap with an initial capacity and an empty set of buckets
    public HashMap() {
        this.capacity = INITIAL_CAPACITY; // Set the initial capacity
        this.size = 0; // Initialize size to zero
        this.buckets = new ArrayList<>(capacity); // Initialize the List of buckets
        for (int i = 0; i < capacity; i++) {
            buckets.add(null); // Initialize each bucket as null
        }
    }

    // Hash function to determine the index (bucket) for a given key
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Method to add or update a key-value pair in the HashMap
    public V put(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets.set(index, bucket);
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;

        // Perform resizing if needed
        if ((double) size / capacity > 0.75) {
            resize();
        }

        return null;
    }

    // Method to remove a key-value pair from the HashMap
    public V remove(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    bucket.remove(entry);
                    size--;
                    return entry.value;
                }
            }
        }
        return null;
    }

    // Method to put a key-value pair if the key doesn't exist in the HashMap
    public V putIfAbsent(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets.set(index, bucket);
        }

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        bucket.add(new Entry<>(key, value));
        size++;

        // Perform resizing if needed
        if ((double) size / capacity > 0.75) {
            resize();
        }

        return null;
    }

    // Method to check if a key exists in the HashMap
    public boolean containsKey(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to retrieve the value associated with a given key
    public V get(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets.get(index);
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    // Method to retrieve all values stored in the HashMap
    public List<V> values() {
        List<V> allValues = new ArrayList<>();
        for (List<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    allValues.add(entry.value);
                }
            }
        }
        return allValues;
    }

    // Method to resize the HashMap when the load factor exceeds a threshold
    private void resize() {
        int newCapacity = capacity * 2;
        List<List<Entry<K, V>>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(null);
        }

        for (List<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    int newIndex = Math.abs(entry.key.hashCode()) % newCapacity;
                    List<Entry<K, V>> newBucket = newBuckets.get(newIndex);
                    if (newBucket == null) {
                        newBucket = new ArrayList<>();
                        newBuckets.set(newIndex, newBucket);
                    }
                    newBucket.add(entry);
                }
            }
        }

        this.capacity = newCapacity;
        this.buckets = newBuckets;
    }
}