package org.example;

import org.example.model.Point;
import org.example.model.Shape2D;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // square example
        List<Point> points1 = List.of(
            new Point(0, 0),
            new Point(1, 0),
            new Point(1, 1),
            new Point(0, 1),
            new Point(0, 0)
            );

        // invalid shape from example task
        List<Point> points2 = List.of(
            new Point(8, 0),
            new Point(8, 3),
            new Point(5, 3),
            new Point(5, 0),
            new Point(8, 0),
            new Point(0, 0),
            new Point(5, 0),
            new Point(5, 7),
            new Point(0, 7),
            new Point(0, 0),
            new Point(8, 0)
        );

        Shape2D inputShape = new Shape2D(points2);

        TheShapeFixer shapeFixer = new TheShapeFixer();
        Shape2D outputShape = shapeFixer.isValid(inputShape) ? inputShape : shapeFixer.repair(inputShape);

        System.out.println(outputShape.toString());
    }
}