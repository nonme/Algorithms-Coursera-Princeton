import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private class Node {
        Point2D point;
        Node left;
        Node right;

        Node(Point2D point) {
            this.point = point;
            this.left = null;
            this.right = null;
        }

        double x() {
            return point.x();
        }
        double y() {
            return point.y();
        }
    }
    private Node root;
    private int size;
    private final int dimension;


    public KdTree() {
        root = null;
        size = 0;
        dimension = 2; // Can be changed for generalization
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, p, 0);
    }

    private Node insert(Node parent, Point2D p, int depth) {
        if (parent == null) {
            size++;
            return new Node(p);
        }
        if (parent.point.equals(p))
            return parent;

        if (lesser(p, parent.point, depth % dimension))
            parent.left  = insert(parent.left, p, depth + 1);
        else
            parent.right = insert(parent.right, p, depth + 1);
        return parent;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return false;
        else {
            Node node = contains(root, p, 0);
            return node != null;
        }
    }

    private Node contains(Node parent, Point2D p, int depth) {
        if (parent == null)
            return null;
        if (Double.compare(p.x(), parent.x()) == 0 && Double.compare(p.y(), parent.y()) == 0)
            return parent;
        if (lesser(p, parent.point, depth % dimension))
            return contains(parent.left, p, depth + 1);
        else
            return contains(parent.right, p, depth + 1);
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1), 0);
    }

    private void draw(Node node, RectHV rect, int depth) {
        if (node != null) {
            StdDraw.setPenRadius(0.01);
            if (depth % dimension == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            node.point.draw();
            if (depth % dimension == 0) {
                draw(node.left, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()), depth + 1);
                draw(node.right, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()), depth + 1);
            } else {
                draw(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()), depth + 1);
                draw(node.right, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()), depth + 1);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Bag<Point2D> points = new Bag<>();
        range(root, rect, points, 0);
        return points;
    }

    private void range(Node node, RectHV rect, Bag<Point2D> points, int depth) {
        if (node == null)
            return;
        if (rect.contains(node.point))
            points.add(node.point);

        if (depth % dimension == 0) {
            if (node.x() > rect.xmax())
                range(node.left, rect, points, depth + 1);
            else if (node.x() < rect.xmin())
                range(node.right, rect, points, depth + 1);
            else {
                range(node.left, rect, points, depth + 1);
                range(node.right, rect, points, depth + 1);
            }
        } else {
            if (node.y() > rect.ymax())
                range(node.left, rect, points, depth + 1);
            else if (node.y() < rect.ymin())
                range(node.right, rect, points, depth + 1);
            else {
                range(node.left, rect, points, depth + 1);
                range(node.right, rect, points, depth + 1);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;
        return nearest(root, root.point, p, new RectHV(0, 0, 1, 1), 0);
    }

    /*
        I'd like to know is it possible to make this code look better?
     */
    private Point2D nearest(Node node, Point2D currentNearest, Point2D point, RectHV rect, int depth) {
        if (node.point.equals(point))
            return node.point;
        Point2D nearestPoint = node.point;
        if (currentNearest.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
            nearestPoint = currentNearest;
        Point2D leftPoint = null;
        Point2D rightPoint = null;
        if (node.left != null &&
                (lesser(point, node.point, depth % dimension) || node.right == null)) {
            if (depth % dimension == 0) {
                RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                if (leftRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    leftPoint = nearest(node.left, nearestPoint, point, leftRect, depth + 1);
            }
            else {
                RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                if (bottomRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    leftPoint = nearest(node.left, nearestPoint, point, bottomRect, depth + 1);
            }
            if (leftPoint != null && leftPoint.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                nearestPoint = leftPoint;

            if (node.right != null) {
                if (depth % dimension == 0) {
                    RectHV rightRect = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    if (rightRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                      rightPoint = nearest(node.right, nearestPoint, point, rightRect, depth + 1);
                }
                else {
                    RectHV topRect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                    if (topRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                        rightPoint = nearest(node.right, nearestPoint, point, topRect, depth + 1);
                }

                if (rightPoint != null && rightPoint.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    nearestPoint = rightPoint;
            }
        } else if (node.right != null) {
            if (depth % dimension == 0) {
                RectHV rightRect = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                if (rightRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    rightPoint = nearest(node.right, nearestPoint, point, rightRect, depth + 1);
            }
            else {
                RectHV topRect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                if (topRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    rightPoint = nearest(node.right, nearestPoint, point, topRect, depth + 1);
            }

            if (rightPoint != null && rightPoint.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                nearestPoint = rightPoint;

            if (node.left != null) {
                if (depth % dimension == 0) {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                    if (leftRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                        leftPoint = nearest(node.left, nearestPoint, point, leftRect, depth + 1);
                }
                else {
                    RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                    if (bottomRect.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                        leftPoint = nearest(node.left, nearestPoint, point, bottomRect, depth + 1);
                }
                if (leftPoint != null && leftPoint.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point))
                    nearestPoint = leftPoint;
            }
        }
        return nearestPoint;
    }

    private boolean lesser(Point2D a, Point2D b, int depth) {
        if (depth == 0)
            return a.x() < b.x();
        else
            return a.y() < b.y();
    }

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();

        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.625, 0.9375));
        kdtree.insert(new Point2D(0.3125, 0.5625));
        kdtree.insert(new Point2D(1.0, 0.0625));
        kdtree.insert(new Point2D(0.9375, 0.0));
        kdtree.insert(new Point2D(0.75, 0.1875));
        kdtree.insert(new Point2D(0.375, 0.375));
        kdtree.insert(new Point2D(0.4375, 0.5));
        kdtree.insert(new Point2D(0.8125, 0.625));
        kdtree.insert(new Point2D(0.125, 0.25));
        kdtree.insert(new Point2D(0.5625, 0.3125));

        kdtree.draw();
        StdDraw.show();

        System.out.println(kdtree.nearest(new Point2D(0.25, 1)));

    }
}
