/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // using a sentinel node to avoid checking whether the deque is empty
    private final Node sentinel;
    private int size;

    // using double linked list
    private class Node {
        private Item val;
        private Node pre;
        private Node next;

        public Node() {
            val = null;
            pre = null;
            next = null;
        }

        public Node(Item val, Node pre, Node next) {
            this.val = val;
            this.pre = pre;
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        sentinel = new Node();
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item, sentinel, sentinel.next);
        sentinel.next.pre = node;
        sentinel.next = node;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item, sentinel.pre, sentinel);
        sentinel.pre.next = node;
        sentinel.pre = node;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = sentinel.next.val;
        sentinel.next = sentinel.next.next;
        sentinel.next.pre = sentinel;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = sentinel.pre.val;
        sentinel.pre = sentinel.pre.pre;
        sentinel.pre.next = sentinel;
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements  Iterator<Item> {
        private int count;
        private Node ptr;

        public DequeIterator() {
            count = size;
            ptr = sentinel.next;
        }

        public boolean hasNext() {
            return count > 0;
        }

        public Item next() {
            if (count == 0) {
                throw new NoSuchElementException();
            }
            Item value = ptr.val;
            ptr = ptr.next;
            count--;
            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        System.out.println(dq.isEmpty());
        for (int i = 0; i < 10; i++) {
            dq.addFirst(i);
        }
        for (int i = 0; i < 10; i++) {
            dq.addLast(i);
        }
        System.out.println(dq.isEmpty());
        System.out.println(dq.size());
        for (int e : dq) {
            System.out.println(e);
        }
        System.out.println("-------------");
        dq.removeFirst();
        dq.removeLast();
        System.out.println(dq.size());
        for (int e : dq) {
            System.out.println(e);
        }
    }

}
