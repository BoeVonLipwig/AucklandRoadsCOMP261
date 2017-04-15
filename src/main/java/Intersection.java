import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Intersection {
	final Location location;
	final int nodeId;
	private int highlighted = 2;
	private Set<Segment> segments = new HashSet<>();
	//These fields are specific to the articulation points
	int count = Integer.MAX_VALUE;
	int reachBack=0;
	Queue<Intersection> children;

	Intersection(Location location, int nodeId) {
		this.location = location;
		this.nodeId = nodeId;
	}

	List<Intersection> getNeighbours() {
		return segments.stream()
				.map(seg -> seg.findOtherEnd(this))
				.distinct()
				.collect(Collectors.toList());
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
			case 4:
				g.setColor(Color.ORANGE);
				break;
		}
		Point p = location.asPoint(origin, zoom);
		g.drawRect(p.x, p.y, 2, 2);
	}

	void setHighlighted(int highlighted) {
		this.highlighted = highlighted;
	}

	Set<Segment> getSegments() {
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
