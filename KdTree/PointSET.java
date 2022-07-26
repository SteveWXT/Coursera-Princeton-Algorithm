import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;
import java.util.List;

public class PointSET {

    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> internalPoints = new LinkedList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                internalPoints.add(p);
            }
        }

        return internalPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D ans = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D point : points) {
            if (p.distanceTo(point) < minDist) {
                ans = point;
                minDist = p.distanceTo(point);
            }
        }

        return ans;
    }
}
