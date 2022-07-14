/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private List<LineSegment> lsList = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        int n = points.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        if (n < 4) {
            return;
        }


        Point[] tmp = Arrays.copyOf(points, n);
        Arrays.sort(tmp);

        for (Point p : points) {
            Arrays.sort(tmp, p.slopeOrder());
            for (int i = 1; i < n; ) {
                int j = i + 1;
                while (j < n && p.slopeTo(tmp[i]) == p.slopeTo(tmp[j])) {
                    j++;
                }

                if (j - i >= 3 && tmp[0].compareTo(minPoint(tmp, i, j-1)) < 0) {
                    lsList.add(new LineSegment(tmp[0], maxPoint(tmp, i, j-1)));
                }

                if (j == n) {
                    break;
                }

                i = j;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lsList.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lsList.toArray(new LineSegment[0]);
    }

    // Helper Method
    private static Point minPoint(Point[] arr, int lo, int hi) {
        if (lo > hi || arr == null) {
            throw new IllegalArgumentException();
        }

        Point min = arr[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (min.compareTo(arr[i]) > 0) {
                min = arr[i];
            }
        }

        return min;
    }

    private static Point maxPoint(Point[] arr, int lo, int hi) {
        if (lo > hi || arr == null) {
            throw new IllegalArgumentException();
        }

        Point max = arr[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (max.compareTo(arr[i]) < 0) {
                max = arr[i];
            }
        }

        return max;
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
