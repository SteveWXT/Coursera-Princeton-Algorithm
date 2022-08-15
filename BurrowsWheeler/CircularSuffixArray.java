import java.util.Arrays;

public class CircularSuffixArray {

    private final int n;
    private final int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        n = s.length();
        indexes = new int[n];
        CircularSuffix[] suffixes = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(suffixes);
        for (int i = 0; i < n; i++) {
            indexes[i] = suffixes[i].ptr;
        }
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private String str;
        private int ptr;

        public CircularSuffix(String str, int ptr) {
            this.str = str;
            this.ptr = ptr;
        }

        private int charAt(int i) {
            if (ptr + i >= str.length()) {
                return str.charAt(i - (str.length() - ptr));
            }
            return str.charAt(ptr + i);
        }


        public int compareTo(CircularSuffix that) {
            if (this == that) {
                return 0;
            }
            int n = Math.min(this.str.length(), that.str.length());
            for (int i = 0; i < n; i++) {
                if (this.charAt(i) != that.charAt(i)) {
                    return this.charAt(i) - that.charAt(i);
                }
            }
            return this.str.length() - that.str.length();
        }
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(csa.length());
        for (int i = 0; i < 12; i++) {
            System.out.println(csa.index(i));
        }
    }
}
