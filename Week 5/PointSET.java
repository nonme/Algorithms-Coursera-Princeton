import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> set;
    public PointSET() {
        this.set = new SET<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!set.contains(p))
            set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Bag<Point2D> points = new Bag<>();
        for (Point2D point : set) {
            if (rect.contains(point))
                points.add(point);
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        Point2D nearestPoint = set.max();
        double minDistance = set.max().distanceSquaredTo(p);
        for (Point2D point : set) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDistance) {
                nearestPoint = point;
                minDistance = distance;
            }
        }
        return nearestPoint;
    }
    public static void main(String[] args) {
        PointSET kdtree = new PointSET();
        kdtree.insert(new Point2D(0.1, 0.1));
        kdtree.insert(new Point2D(0.3, 0.3));
        kdtree.insert(new Point2D(0.5, 0.5));
        kdtree.insert(new Point2D(0.7, 0.7));
        kdtree.insert(new Point2D(0.9, 0.9));
        for (Point2D point : kdtree.range(new RectHV(0.35, 0.35, 0.8, 0.8))) {
            System.out.println(point);
        }
        System.out.println(kdtree.contains(new Point2D(0.3, 0.3)));
        System.out.println(kdtree.contains(new Point2D(0.35, 0.3)));
    }
}
