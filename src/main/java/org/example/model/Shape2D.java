package org.example.model;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.StringJoiner;

public class Shape2D {

    private final List<Point2D> points;

    public Shape2D(List<Point2D> points) {
        this.points = points;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "Shape2D{", "}");
        for (Point2D point : points) {
            sj.add("[" + point.getX() + ", " + point.getY() + "]");
        }
        return sj.toString();
    }
}
