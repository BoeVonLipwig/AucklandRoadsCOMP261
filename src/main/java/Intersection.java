import java.awt.*;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
class Intersection {
    final Location location;
    final int nodeId;

    Intersection(Location location, int nodeId){
        this.location=location;
        this.nodeId=nodeId;
    }

    void draw(boolean highlighted, Graphics g, Location origin, double zoom) {
        g.setColor(highlighted ? Color.RED : Color.GREEN);
        Point p=location.asPoint(origin,zoom);
        g.drawRect(p.x,p.y,2,2);
    }
}
