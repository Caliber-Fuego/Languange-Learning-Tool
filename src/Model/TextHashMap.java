package Model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TextHashMap <K, V> implements Iterable<K>{
    private int capacity, size;
    private HMNode<K, V>[] table;

    private int defaultCapacity = 16;
    private double loadFactor = 0.75;

    public TextHashMap(){
        this(16);
    }

    public TextHashMap(int capacity){
        this.capacity = capacity;
        this.size = 0;
        this.table = new HMNode[capacity];
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    private void resize() {
        int newCapacity = capacity * 2; // Double the capacity for simplicity

        @SuppressWarnings("unchecked")
        HMNode<K, V>[] newTable = new HMNode[newCapacity];

        // Rehash existing elements into the new array
        for (HMNode<K, V> currentNode : table) {
            while (currentNode != null) {
                HMNode<K, V> nextNode = currentNode.next;
                int newIndex = getIndex(currentNode.key, newCapacity);
                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }

        // Update the reference to the new array and capacity
        table = newTable;
        capacity = newCapacity;
    }




    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {
        private int currentBucket = 0;
        private HMNode<K, V> currentNode = table[currentBucket];

        @Override
        public boolean hasNext() {
            while (currentBucket < capacity) {
                if (currentNode != null){
                    return true;
                }
                currentBucket++;
                if(currentBucket < capacity){
                    currentNode = table[currentBucket];
                }
            }
            return false;
        }

        @Override
        public K next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            K key = currentNode.key;
            currentNode = currentNode.next;
            return key;

        }


    }

    private static class HMNode<K, V>{
        K key;
        V value;
        HMNode<K, V> next;

        HMNode(K key, V value){
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public HMNode getNext() {
            return next;
        }

        public void setNext(HMNode<K, V> next) {
            this.next = next;
        }


    }


    public V get(K key){
        int index = getIndex(key, capacity);
        HMNode<K, V> current = table[index];
        while (current != null){
            if (current.key.equals(key)){
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    public void put(K key, V value) {
        if ((double) size / capacity > loadFactor) {
            resize(); // Check if resize is needed before adding a new element
        }

        int index = getIndex(key, capacity);
        HMNode<K, V> newNode = new HMNode<>(key, value);

        HMNode<K, V> current = table[index];
        HMNode<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            previous = current;
            current = current.next;
        }

        if (previous == null) {
            table[index] = newNode;
        } else {
            previous.next = newNode;
        }
        size++;
    }

    public void putAll(TextHashMap<K, V> otherMap) {
        for (K key : otherMap) {
            V value = otherMap.get(key);
            put(key, value);
        }
    }

    private int getIndex(K key, int arrayLength) {
        return key == null ? 0 : Math.abs(key.hashCode() % arrayLength);
    }


    public boolean containsKey(K key) {
        int index = getIndex(key, capacity);
        HMNode<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

}
