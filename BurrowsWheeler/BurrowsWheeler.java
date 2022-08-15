/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String origin = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(origin);
        int n = csa.length();
        List<Character> t = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            t.add(getLast(origin, csa.index(i)));
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(t.get(i));
        }
        BinaryStdOut.close();
    }

    // given origin string and origin suffix row number, return the last char in the suffix
    private static char getLast(String origin, int i) {
        int n = origin.length();
        if (i > 0) {
            return origin.charAt(i - 1);
        } else {
            return origin.charAt(i + n - 1);
        }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        ArrayList<Character> t = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            t.add(BinaryStdIn.readChar());
        }

        int[] next = getNext(t);

        int idx = next[first];
        for (int i = 0; i < t.size(); i++) {
            BinaryStdOut.write(t.get(idx));
            idx = next[idx];
        }
        BinaryStdOut.close();
    }

    // give t and first, return the next array
    private static int[] getNext(ArrayList<Character> t) {
        // using indexed counting method similar to radix sort
        int[] count = new int[256 + 1]; // extended ASCII
        int n = t.size();
        int[] next = new int[n];

        for (int i = 0; i < n; i++) {
            count[t.get(i) + 1]++;
        }

        for (int r = 0; r < 256; r++) {
            count[r+1] += count[r];
        }

        for (int i = 0; i < n; i++) {
            next[count[t.get(i)]++] = i;
        }

        return next;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")){
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }

}
