import java.awt.*;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Intersection {
    public final Location location;
    public final int nodeid;

    Intersection(Location location, int nodeid){
        this.location=location;
        this.nodeid=nodeid;
    }
    public void draw(boolean highlighted, Graphics g, Location orign, double zoom) {
        g.setColor(highlighted ? Color.RED : Color.BLACK);
        Point p=location.asPoint(orign,zoom);
        g.drawRect(p.x,p.y,2,2);
    }
}
