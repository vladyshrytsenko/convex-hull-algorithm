package org.example;

import org.example.model.Shape2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.example.GeometryUtils.*;

public class TheShapeFixer {

    public boolean isValid(Shape2D shape2D) {
        List<Point2D> points = shape2D.getPoints();
        int n = points.size();

        if (!points.getFirst().equals(points.get(n - 1))) {
            return false;
        }

        for (int i = 0; i < n-1; i++) {
            Point2D A = points.get(i);
            Point2D B = points.get(i + 1);

            for (int j = i+2; j < n-1; j++) {
                if (i == 0 && j == n-2) {
                    continue;
                }

                Point2D C = points.get(j);
                Point2D D = points.get(j + 1);

                if (segmentsIntersect(A, B, C, D)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Jarvis march
    public Shape2D repair(Shape2D shape2D) {
        List<Point2D> points = shape2D.getPoints();

        int size = points.size();
        if (size < 3) {
            return null;
        }

        List<Point2D> outputHull = new ArrayList<>();

        int leftPoint = 0;
        for (int i = 0; i < size; i++) {
            if (points.get(i).getX() < points.get(leftPoint).getX()) {
                leftPoint = i;
            }
        }

        int currentPoint = leftPoint, nextPoint;
        outputHull.add(points.get(currentPoint));

        nextPoint = (currentPoint + 1) % size;

        Set<Integer> candidateIndices = new HashSet<>();
        Map<Integer, Set<Integer>> internalPointsForAdding = new HashMap<>();

        for (int i = 0; i < size; i++) {
            if (orientation(points.get(currentPoint), points.get(i), points.get(nextPoint)) == 2) {
                candidateIndices.add(i);
                nextPoint = i;
            }
        }

        currentPoint = nextPoint;
        boolean isDiagonalSegmentExist = false;

        for (;;) {
            Point2D pointObj = points.get(currentPoint);
            outputHull.add(pointObj);

            nextPoint = (currentPoint + 1) % size;

            for (int i = 0; i < size; i++) {
                if (orientation(pointObj, points.get(i), points.get(nextPoint)) == 2) {
                    candidateIndices.add(i);
                    nextPoint = i;
                }
            }

            // if next X and Y change at the same time
            if (isDiagonalSegment(pointObj, points.get(nextPoint))) {
                isDiagonalSegmentExist = true;
            }

            currentPoint = nextPoint;
            if (currentPoint == leftPoint) {
                pointObj = points.get(currentPoint);
                outputHull.add(pointObj);
                break;
            }
        }

        if (isDiagonalSegmentExist) {
            candidateIndices.forEach(i -> {
                Point2D point = points.get(i);

                for (int k = 0; k < outputHull.size() - 1; k++) {
                    Point2D prevPoint = outputHull.get(k);
                    Point2D nextPoint2 = outputHull.get(k + 1);

                    if (!outputHull.contains(point)) {
                        if (isPositionValidToInsert(point, prevPoint, nextPoint2)) {
                            outputHull.add(k + 1, point);
                            break;
                        } else {
                            if (isPointInsideTriangle(prevPoint, nextPoint2, point)) {
                                if (!isPointCollinearWithAllPairs(point, outputHull)) {
                                    outputHull.add(k + 1, point);
                                    break;
                                } else if (isDiagonalSegment(prevPoint, nextPoint2)) {
                                    outputHull.add(k + 1, point);
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }

        return new Shape2D(outputHull);
    }

    private boolean isDiagonalSegment(Point2D A, Point2D B) {
        return (A.getX() != B.getX() && A.getY() != B.getY());
    }

    private boolean isPointOnSegment(Point2D A, Point2D B, Point2D P) {
        double collinearityRes = checkCollinearity(A, B, P);

        if (collinearityRes != 0) {
            return false;
        }

        boolean withinX = min(A.getX(), B.getX()) <= P.getX() && P.getX() <= max(A.getX(), B.getX());
        boolean withinY = min(A.getY(), B.getY()) <= P.getY() && P.getY() <= max(A.getY(), B.getY());

        return withinX && withinY;
    }

    private int orientation(Point2D A, Point2D B, Point2D P) {
        double collinearityRes = checkCollinearity(A, B, P);

        if (collinearityRes == 0) {
            return 0;
        }
        return collinearityRes > 0 ? 1 : 2;
    }

    private boolean segmentsIntersect(Point2D A, Point2D B, Point2D C, Point2D D) {
        int o1 = orientation(A, B, C);
        int o2 = orientation(A, B, D);
        int o3 = orientation(C, D, A);
        int o4 = orientation(C, D, B);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && isPointOnSegment(A, B, C)) return true;
        if (o2 == 0 && isPointOnSegment(A, B, D)) return true;
        if (o3 == 0 && isPointOnSegment(C, D, A)) return true;
        if (o4 == 0 && isPointOnSegment(C, D, B)) return true;

        return false;
    }
    private boolean isPositionValidToInsert(Point2D point, Point2D prevPoint, Point2D nextPoint) {
        int orientationPrev = orientation(prevPoint, point, nextPoint);
        return orientationPrev != 1;
    }

    private boolean isPointInsideTriangle(Point2D prevPoint, Point2D nextPoint, Point2D point) {
        double triangleArea = getTriangleArea(prevPoint, nextPoint, new Point2D.Double(0, 0));

        double area1 = getTriangleArea(point, prevPoint, nextPoint);
        double area2 = getTriangleArea(point, prevPoint, new Point2D.Double(0, 0));
        double area3 = getTriangleArea(point, nextPoint, new Point2D.Double(0, 0));

        return (triangleArea == area1 + area2 + area3);
    }

    private boolean isPointCollinearWithAllPairs(Point2D point, List<Point2D> outputHull) {
        for (int i = 0; i < outputHull.size(); i++) {
            Point2D prevPoint = outputHull.get(i);
            Point2D nextPoint = outputHull.get((i + 1) % outputHull.size());

            if (arePointsCollinear(prevPoint, nextPoint, point)) {
                return true;
            }
        }
        return false;
    }
}
