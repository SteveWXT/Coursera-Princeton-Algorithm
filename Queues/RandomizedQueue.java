/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int size;
    private int capacity;

    private final int originCap = 8;
    private final double ratio = 0.25;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[originCap];
        size = 0;
        capacity = originCap;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == capacity) {
            resize(2 * capacity);
        }
        arr[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int rdIdx = StdRandom.uniform(size);
        Item item = arr[rdIdx];
        arr[rdIdx] = arr[size - 1];
        arr[--size] = null;
        if (size > 0 && size * 1.0 / capacity < ratio) {
            resize(capacity / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int rdIdx = StdRandom.uniform(size);
        Item item = arr[rdIdx];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] copyArr;
        private int count;

        public RandomizedQueueIterator() {
            copyArr = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copyArr[i] = arr[i];
            }
            count = size;
            StdRandom.shuffle(copyArr);
        }

        public boolean hasNext() {
            return count > 0;
        }

        public Item next() {
            if (count == 0) {
                throw new NoSuchElementException();
            }
            Item item = copyArr[--count];
            copyArr[count] = null;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // Helper methods

    // resize the array to a new size
    private void resize(int newSize) {
        Item[] arr2 = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            arr2[i] = arr[i];
        }
        arr = arr2;
        capacity = newSize;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        System.out.println(rq.isEmpty());
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        System.out.println(rq.size());
        System.out.println("-------------");
        for (int i = 0; i < 5; i++) {
            System.out.println(rq.sample());
        }
        System.out.println(rq.size());
        System.out.println("-------------");
        for (int i = 0; i < 5; i++) {
            System.out.println(rq.dequeue());
        }
        System.out.println(rq.size());
        System.out.println("-------------");
        for (int e : rq) {
            System.out.println(e);
        }
    }
}
