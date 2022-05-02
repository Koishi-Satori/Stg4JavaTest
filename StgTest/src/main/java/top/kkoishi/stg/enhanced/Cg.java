package top.kkoishi.stg.enhanced;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Cg {
    protected BufferedImage cg;

    protected Point centre;

    public BufferedImage cg () {
        return cg;
    }

    public void setCg (BufferedImage cg) {
        this.cg = cg;
    }

    public Point centre () {
        return centre;
    }

    public void setCentre (Point centre) {
        this.centre = centre;
    }
}
