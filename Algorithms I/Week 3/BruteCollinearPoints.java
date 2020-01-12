import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments;
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        checkInput(pointsCopy);

        segments = new ArrayList<>();
        for (int i = 0; i < pointsCopy.length; ++i) {
            for (int j = i + 1; j < pointsCopy.length; ++j) {
                for (int k = j + 1; k < pointsCopy.length; ++k) {
                    if (Double.compare(pointsCopy[i].slopeTo(pointsCopy[j]), pointsCopy[j].slopeTo(pointsCopy[k])) == 0) {
                        for (int c = k + 1; c < pointsCopy.length; ++c) {
                            if (Double.compare(pointsCopy[i].slopeTo(pointsCopy[c]), pointsCopy[j].slopeTo(pointsCopy[c])) == 0)
                                segments.add(new LineSegment(pointsCopy[i], pointsCopy[c]));
                        }
                    }
                }
            }
        }
    }
    private void checkInput(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        for (int i = 1; i < points.length; ++i) {
            if (points[i].compareTo(points[i-1]) == 0)
                throw new IllegalArgumentException();
        }
    }
    public int numberOfSegments() {
        return segments.size();
    }
    public LineSegment[] segments() {
        LineSegment[] temp = new LineSegment[numberOfSegments()];
        return segments.toArray(temp);
    }
}
