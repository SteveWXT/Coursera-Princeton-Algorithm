import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    // Using 26-way trie to store dictionary
    private Node root;
    private boolean[][] marked;
    private Set<String> validWords;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            put(word);
        }
    }

    private static class Node {
        private boolean exist = false;
        private Node[] next = new Node[26];
    }

    private void put(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word cannot be null");
        }
        root = put(word, root, 0);
    }

    private Node put(String word, Node x, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == word.length()) {
            x.exist = true;
            return x;
        } else {
            char c = word.charAt(d);
            x.next[c - 'A'] = put(word, x.next[c - 'A'], d+1);
            return x;
        }
    }

    private boolean get(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word cannot be null");
        }
        return get(word, root, 0);
    }

    private boolean get(String word, Node x, int d) {
        if (x == null) {
            return false;
        }
        if (d == word.length()) {
            return x.exist;
        } else {
            char c = word.charAt(d);
            return get(word, x.next[c - 'A'], d+1);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new HashSet<>();
        int n = board.rows();
        int m = board.cols();

        if (root == null) {
            return null;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                marked = new boolean[n][m];
                dfs(board, i, j, new StringBuilder(), root);
            }
        }
        return validWords;
    }

    private void dfs(BoggleBoard board, int row, int col, StringBuilder word, Node node) {
        marked[row][col] = true;
        char c = board.getLetter(row, col);
        node = node.next[c - 'A'];
        if (node == null) {
            marked[row][col] = false;
            return;
        }
        word.append(c);

        if (c == 'Q') {
            node = node.next['U' - 'A'];
            if (node == null) {
                word.deleteCharAt(word.length()-1);
                marked[row][col] = false;
                return;
            }
            word.append('U');
        }

        if (node.exist && word.length() >= 3) {
            validWords.add(word.toString());
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                if (inBoard(board, row+x, col+y) && !marked[row+x][col+y]) {
                    dfs(board, row+x, col+y, word, node);
                }
            }
        }

        marked[row][col] = false;
        word.deleteCharAt(word.length() - 1);
        if (c == 'Q') {
            word.deleteCharAt(word.length() - 1);
        }
    }

    private boolean inBoard(BoggleBoard board, int row, int col) {
        return row >= 0 && row < board.rows() && col >= 0 && col < board.cols();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(word)) {
            return 0;
        }
        int n = word.length();
        if (n == 3 || n == 4) {
            return 1;
        } else if (n == 5) {
            return 2;
        } else if (n == 6) {
            return 3;
        } else if (n == 7) {
            return 5;
        } else if (n >= 8) {
            return 11;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
