package top.kkoishi.stg.object;

import java.awt.Point;

public interface Collisionable {
    default strictfp boolean pound (Collisionable another) {
        final Point selfCentre = centre();
        final Point otherCentre = another.centre();
        double dx = selfCentre.x - otherCentre.x;
        double dy = selfCentre.y - otherCentre.y;
        return dx * dx + dy * dy <= radius() + another.radius();
    }

    double radius ();

    Point centre ();
}
