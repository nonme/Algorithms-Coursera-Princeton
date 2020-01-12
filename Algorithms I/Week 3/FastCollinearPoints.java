import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments;
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        Point[] pointsOrigin = Arrays.copyOf(points, points.length);
        Point[] pointsCopy = Arrays.copyOf(pointsOrigin, pointsOrigin.length);

        checkInput(pointsCopy);

        segments = new ArrayList<>();
        for (int i = 0; i < pointsCopy.length; ++i) {
            Point origin = pointsOrigin[i];
            Arrays.sort(pointsCopy);
            Arrays.sort(pointsCopy, origin.slopeOrder());

            int j = 2;
            while (j < pointsCopy.length) {
                if (Double.compare(origin.slopeTo(pointsCopy[j]), origin.slopeTo(pointsCopy[j - 1]))         == 0 &&
                        Double.compare(origin.slopeTo(pointsCopy[j - 1]), origin.slopeTo(pointsCopy[j - 2])) == 0) {
                    ArrayList<Point> collinearPoints = new ArrayList<>();
                    collinearPoints.add(pointsOrigin[i]);
                    int c = j - 1;
                    while (c < pointsCopy.length && Double.compare(origin.slopeTo(pointsCopy[c - 1]), origin.slopeTo(pointsCopy[c])) == 0) {
                        collinearPoints.add(pointsCopy[c - 1]);
                        c++;
                    }
                    collinearPoints.add(pointsCopy[c - 1]);
                    if (collinearPoints.size() >= 4) {
                        LineSegment segment = new LineSegment(collinearPoints.get(0), collinearPoints.get(collinearPoints.size() - 1));
                        if (collinearPoints.get(1).compareTo(collinearPoints.get(0)) > 0)
                            segments.add(segment);
                    }
                    j = c;
                }
                ++j;
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
