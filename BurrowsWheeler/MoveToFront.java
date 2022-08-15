import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoveToFront {

    private static List<Character> alphabet;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initial();
        ArrayList<Character> input = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            input.add(BinaryStdIn.readChar());
        }
        for (int i = 0; i < input.size(); i++) {
            int idx = alphabet.indexOf(input.get(i));
            BinaryStdOut.write(idx, 8);
            char c = alphabet.remove(idx);
            alphabet.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initial();
        ArrayList<Character> input = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            input.add(BinaryStdIn.readChar());
        }
        for (int i = 0; i < input.size(); i++) {
            BinaryStdOut.write(alphabet.get(input.get(i)));
            char c = alphabet.remove((int) input.get(i));
            alphabet.add(0, c);
        }
        BinaryStdOut.close();
    }

    private static void initial() {
        alphabet = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            alphabet.add((char)i);
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")){
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
