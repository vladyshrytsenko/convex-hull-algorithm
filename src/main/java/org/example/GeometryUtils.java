package org.example;

import java.awt.geom.Point2D;

public class GeometryUtils {

    public static double checkCollinearity(Point2D A, Point2D B, Point2D P) {
        return (B.getX() - A.getX()) * (P.getY() - A.getY()) -
               (P.getX() - A.getX()) * (B.getY() - A.getY());
    }

    public static double getTriangleArea(Point2D A, Point2D B, Point2D C) {
        return Math.abs(
            (A.getX() * (B.getY() - C.getY()) +
             B.getX() * (C.getY() - A.getY()) +
             C.getX() * (A.getY() - B.getY())) / 2.0
        );
    }

    public static boolean arePointsCollinear(Point2D prevPoint, Point2D nextPoint, Point2D point) {
        return checkCollinearity(prevPoint, nextPoint, point) == 0;
    }

}
