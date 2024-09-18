package org.example;

import org.example.model.Shape2D;

import java.awt.geom.Point2D;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // square example
        List<Point2D> points1 = List.of(
            new Point2D.Double(0.0d, 0.0d),
            new Point2D.Double(1.0d, 0.0d),
            new Point2D.Double(1.0d, 1.0d),
            new Point2D.Double(0.0d, 1.0d),
            new Point2D.Double(0.0d, 0.0d)
            );

        // invalid shape from example task
        List<Point2D> points2 = List.of(
            new Point2D.Double(8.0d, 0.0d),
            new Point2D.Double(8.0d, 3.0d),
            new Point2D.Double(5.0d, 3.0d),
            new Point2D.Double(5.0d, 0.0d),
            new Point2D.Double(8.0d, 0.0d),
            new Point2D.Double(0.0d, 0.0d),
            new Point2D.Double(5.0d, 0.0d),
            new Point2D.Double(5.0d, 7.0d),
            new Point2D.Double(0.0d, 7.0d),
            new Point2D.Double(0.0d, 0.0d),
            new Point2D.Double(8.0d, 0.0d)
        );

        Shape2D inputShape = new Shape2D(points2);

        TheShapeFixer shapeFixer = new TheShapeFixer();
        Shape2D outputShape = shapeFixer.isValid(inputShape) ? inputShape : shapeFixer.repair(inputShape);

        System.out.println(outputShape.toString());
    }
}