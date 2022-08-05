import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }

        double gradientX = gradientSqaure(picture.get(x-1, y), picture.get(x+1, y));
        double gradientY = gradientSqaure(picture.get(x, y-1), picture.get(x, y+1));
        return Math.sqrt(gradientX + gradientY);
    }

    private double gradientSqaure(Color c1, Color c2) {
        return Math.pow(c2.getRed() - c1.getRed(), 2) + Math.pow(c2.getGreen() - c1.getGreen(), 2)
                +  Math.pow(c2.getBlue() - c1.getBlue(), 2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(height(), width(), false);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
       return findSeam(width(), height(), true);
    }

    private int[] findSeam(int width, int height, boolean isVertical) {
        double[][] energy = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energy[x][y] = isVertical ? energy(x, y) : energy(y, x);
            }
        }

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        for (int x = 0; x < width; x++) {
            distTo[x][0] = 1000.0;
        }
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                relax(x-1, y+1, x, y, energy, edgeTo, distTo, isVertical);
                relax(x, y+1, x, y, energy, edgeTo, distTo, isVertical);
                relax(x+1, y+1, x, y, energy, edgeTo, distTo, isVertical);
            }
        }

        int[] seam = new int[height];
        double minDist = Double.POSITIVE_INFINITY;
        int minX = -1;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height-1] < minDist) {
                minDist = distTo[x][height-1];
                minX = x;
            }
        }

        for (int y = height - 1; y >= 0; y--) {
            seam[y] = minX;
            minX = edgeTo[minX][y];
        }

        return seam;
    }

    private void relax(int x, int y, int prex, int prey, double[][] energy,
                       int[][] edgeTo, double[][] distTo, boolean isVertical) {
        if (isVertical && (x < 0 || x >= width() || y < 0 || y >= height())) {
            return;
        } else if (!isVertical && (x < 0 || x >= height() || y < 0 || y >= width())) {
            return;
        } else if (distTo[x][y] > distTo[prex][prey] + energy[x][y]) {
            distTo[x][y] = distTo[prex][prey] + energy[x][y];
            edgeTo[x][y] = prex;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1 || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        Picture newPic = new Picture(width(), height() - 1);
        int preSeam = seam[0];
        for (int x = 0; x < width(); x++) {
            if (Math.abs(preSeam - seam[x]) > 1 || seam[x] < 0 || seam[x] >= height()) {
                throw new IllegalArgumentException();
            }
            preSeam = seam[x];
            for (int y = 0; y < height(); y++) {
                if (y == seam[x]) {
                    continue;
                } else {
                    newPic.set(x, seam[x] > y ? y : y - 1, picture.get(x, y));
                }
            }
        }

        this.picture = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1 || seam.length != height()) {
            throw new IllegalArgumentException();

        }

        Picture newPic = new Picture(width() - 1, height());
        int preSeam = seam[0];
        for (int y = 0; y < height(); y++) {
            if (Math.abs(preSeam - seam[y]) > 1 || seam[y] < 0 || seam[y] >= width()) {
                throw new IllegalArgumentException();
            }
            preSeam = seam[y];
            for (int x = 0; x < width(); x++) {
                if (x == seam[y]) {
                    continue;
                } else {
                    newPic.set(seam[y] > x ? x : x -1, y, picture.get(x, y));
                }
            }
        }

        this.picture = newPic;
    }
}
