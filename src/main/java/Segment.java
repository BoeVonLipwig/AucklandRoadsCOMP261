
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017
 */
class Segment {

    public final double length;
    public final int node1;
    public final int node2;
    public List<Location> coords = new ArrayList<>();

    Segment(double length, int node1, int node2, List<Location> coords) {
        this.length = length;
        this.node1 = node1;
        this.node2 = node2;
        this.coords.addAll(coords);
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }

    public void draw(boolean highlighted, Graphics g, Location orign, double zoom) {
        g.setColor(highlighted ? Color.GREEN : Color.BLACK);
        for (int i = 0; i < coords.size()-1; i ++) {
            Point p1 = coords.get(i).asPoint(orign, zoom);
            Point p2 = coords.get(i + 1).asPoint(orign, zoom);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

    }
}


