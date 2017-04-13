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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intersection other = (Intersection) obj;
		return nodeId == other.nodeId;
	}

	public void addSegments(Segment segments) {
		this.segments.add(segments);
	}
}
