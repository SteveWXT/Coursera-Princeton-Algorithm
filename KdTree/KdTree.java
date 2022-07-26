import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node left;
        private Node right;
        private boolean isVertical;

        public Node(Point2D p, RectHV rect, Node left, Node right, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.left = left;
            this.right = right;
            this.isVertical = isVertical;
        }

        public RectHV leftOrBelowRect() {
            if (isVertical) {
                return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            }
        }

        public RectHV rightOrAboveRect() {
            if (isVertical) {
                return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    private Node root;
    private int size;

    // used by nearest
    private double minDist;
    private Point2D nearestPoint;

    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        root = insert(p, root, null, true);
    }

    private Node insert(Point2D p, Node n, Node pre, boolean isLeft) {

        if (n == null) {
            size++;
            if (pre == null) {
                return new Node(p, new RectHV(0, 0, 1, 1), null, null, true);
            } else if (isLeft) {
                return new Node(p, pre.leftOrBelowRect(), null, null, !pre.isVertical);
            } else {
                return new Node(p, pre.rightOrAboveRect(), null, null, !pre.isVertical);
            }
        }

        if (n.p.equals(p)) {
            return n;
        } else if (n.isVertical) {
            if (p.x() < n.p.x()) {
                n.left = insert(p, n.left, n, true);
            } else {
                n.right = insert(p, n.right, n, false);
            }
        } else {
            if (p.y() < n.p.y()) {
                n.left = insert(p, n.left, n, true);
            } else {
                n.right = insert(p, n.right, n, false);
            }
        }

        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return contains(p, root);
    }

    private boolean contains(Point2D p, Node n) {
        if (n == null) {
            return false;
        }

        if (n.p.equals(p)) {
            return true;
        } else if (n.isVertical) {
            if (p.x() < n.p.x()) {
                return contains(p, n.left);
            } else {
                return contains(p, n.right);
            }
        } else {
            if (p.y() < n.p.y()) {
                return contains(p, n.left);
            } else {
                return contains(p, n.right);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) {
            return;
        }

        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());
        draw(n.left);
        draw(n.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> ans = new LinkedList<>();
        range(rect, root, ans);
        return ans;
    }

    private void range(RectHV rect, Node node, List<Point2D> lst) {
        if (node == null) {
            return;
        }

        if (rect.contains(node.p)) {
            lst.add(node.p);
        }

        if (node.isVertical) {
            if (rect.xmax() < node.p.x()) {
                range(rect, node.left, lst);
            } else if (rect.xmin() > node.p.x()) {
                range(rect, node.right, lst);
            } else {
                range(rect, node.left, lst);
                range(rect, node.right, lst);
            }
        } else {
            if (rect.ymax() < node.p.y()) {
                range(rect, node.left, lst);
            } else if (rect.ymin() > node.p.y()) {
                range(rect, node.right, lst);
            } else {
                range(rect, node.left, lst);
                range(rect, node.right, lst);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        minDist = Double.MAX_VALUE;
        nearestPoint = null;
        nearest(p, root);
        return nearestPoint;
    }

    private void nearest(Point2D p, Node n) {
        if (n == null) {
            return;
        }

        if (p.distanceSquaredTo(n.p) < minDist) {
            minDist = p.distanceSquaredTo(n.p);
            nearestPoint = n.p;
        }

        if (n.isVertical) {
            if (p.x() < n.p.x()) {
                if (n.left != null && needSearch(p, n.left)) {
                    nearest(p, n.left);
                }
                if (n.right != null && needSearch(p, n.right)) {
                    nearest(p, n.right);
                }
            } else {
                if (n.right != null && needSearch(p, n.right)) {
                    nearest(p, n.right);
                }
                if (n.left != null && needSearch(p, n.left)) {
                    nearest(p, n.left);
                }
            }
        } else {
            if (p.y() < n.p.y()) {
                if (n.left != null && needSearch(p, n.left)) {
                    nearest(p, n.left);
                }
                if (n.right != null && needSearch(p, n.right)) {
                    nearest(p, n.right);
                }
            } else {
                if (n.right != null && needSearch(p, n.right)) {
                    nearest(p, n.right);
                }
                if (n.left != null && needSearch(p, n.left)) {
                    nearest(p, n.left);
                }
            }
        }
    }

    private boolean needSearch(Point2D p, Node n) {
        if (n.rect.distanceSquaredTo(p) >= minDist) {
            return false;
        } else {
            return true;
        }
    }
}
