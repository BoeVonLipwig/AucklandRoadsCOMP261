import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
class Intersection {
    final Location location;
    final int nodeId;
    private int highlighted=2;
    private Set<Segment> segments=new HashSet<>();

    Intersection(Location location, int nodeId){
        this.location=location;
        this.nodeId=nodeId;
    }

    void draw(Graphics g, Location origin, double zoom) {
		switch (highlighted) {
			case 1:
				g.setColor(Color.RED);
				break;
			case 2:
				g.setColor(Color.GREEN);
				break;
			case 3:
				g.setColor(Color.BLUE);
				break;
		}
        Point p=location.asPoint(origin,zoom);
        g.drawRect(p.x,p.y,2,2);
    }

	public void setHighlighted(int highlighted) {
		this.highlighted = highlighted;
	}

	public Set<Segment> getSegments() {
		return segments;
	}

	public void addSegments(Segment segments) {
		this.segments.add(segments);
	}
}
