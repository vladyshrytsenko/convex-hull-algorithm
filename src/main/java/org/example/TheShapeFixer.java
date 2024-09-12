package org.example;

import org.example.model.Point;
import org.example.model.Shape2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.*;

public class TheShapeFixer {

    public boolean isValid(Shape2D shape2D) {
        List<Point> points = shape2D.getPoints();
        int n = points.size();

        for (int i = 2; i < n; i++) {
            Point P = points.get(i);

            if ( (i == n - 1) && P.equals(points.getFirst()) ) {
                continue;
            }

            for (int j = 0; j < i - 1; j++) {
                Point A = points.get(j);
                Point B = points.get(j + 1);

                if (isPointOnSegment(A, B, P)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Jarvis march
    public Shape2D repair(Shape2D shape2D) {
        List<Point> points = shape2D.getPoints();

        int size = points.size();
        if (size < 3) {
            return null;
        }

        List<Point> outputHull = new ArrayList<>();

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
        List<Integer> validIndices = new ArrayList<>();
        Map<Integer, Set<Integer>> internalPointsForAdding = new HashMap<>();

        for (int i = 0; i < size; i++) {
            if (orientation(points.get(currentPoint), points.get(i), points.get(nextPoint)) == 2) {
                candidateIndices.add(i);
                nextPoint = i;
            }
        }

        currentPoint = nextPoint;
        for (;;) {
            Point pointObj = points.get(currentPoint);
            outputHull.add(pointObj);

            nextPoint = (currentPoint + 1) % size;

            for (int i = 0; i < size; i++) {
                if (orientation(pointObj, points.get(i), points.get(nextPoint)) == 2) {
                    candidateIndices.add(i);
                    nextPoint = i;
                }
            }

            validIndices.add(nextPoint);

            // if next X and Y change at the same time
            if (isDiagonalSegment(pointObj, points.get(nextPoint))) {
                internalPointsForAdding.put(outputHull.size(), candidateIndices);
            }

            currentPoint = nextPoint;
            if (currentPoint == leftPoint) {
                pointObj = points.get(currentPoint);
                outputHull.add(pointObj);
                break;
            }
        }

        internalPointsForAdding.forEach((key, value) -> {
            validIndices.forEach(value::remove);

            value.forEach(i -> {
                Point point = points.get(i);
                if (!outputHull.contains(point)) {
                    outputHull.add(i+1, point);
                }
            });
        });

        return new Shape2D(outputHull);
    }

    private boolean isDiagonalSegment(Point A, Point B) {
        return (A.getX() != B.getX() && A.getY() != B.getY());
    }

    private boolean isPointOnSegment(Point A, Point B, Point P) {
        int collinearityRes = checkCollinearity(A, B, P);

        if (collinearityRes != 0) {
            return false;
        }

        boolean withinX = min(A.getX(), B.getX()) <= P.getX() && P.getX() <= max(A.getX(), B.getX());
        boolean withinY = min(A.getY(), B.getY()) <= P.getY() && P.getY() <= max(A.getY(), B.getY());

        return withinX && withinY;
    }

    private int orientation(Point A, Point B, Point P) {
        int collinearityRes = checkCollinearity(A, B, P);

        if (collinearityRes == 0) {
            return 0;
        }
        return collinearityRes > 0 ? 1 : 2;
    }

    private int checkCollinearity(Point A, Point B, Point P) {
        return (B.getX() - A.getX()) * (P.getY() - A.getY()) -
               (P.getX() - A.getX()) * (B.getY() - A.getY());
    }
}
