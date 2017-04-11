
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017
 */
class Segment {

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private final double length;

	private final Intersection node1;
	private final Intersection node2;
	@SuppressWarnings("SpellCheckingInspection")
	private List<Location> coords;

	@SuppressWarnings("SpellCheckingInspection")
	Segment(double length, Intersection node1, Intersection node2, List<Location> coords) {
		this.length = length;
		this.node1 = node1;
		this.node2 = node2;
		this.coords = new ArrayList<>();
		this.coords.addAll(coords);
		node1.getSegments().add(this);
		node2.getSegments().add(this);
	}

	int getNode1ID() {
		return node1.nodeId;
	}

	int getNode2ID() {
		return node2.nodeId;
	}

	double getLength() {
		return length;
	}

	Intersection findOtherEnd(Intersection i){
		if(i.equals(node1))return node2;
		return node1;
	}


	void draw(int highlighted, Graphics g, Location origin, double zoom) {
		switch (highlighted) {
		case 1:
			g.setColor(Color.BLACK);
			break;
		case 2:
			g.setColor(Color.RED);
			break;
		case 3:
			g.setColor(Color.BLUE);
			break;
		}
		for (int i = 0; i < coords.size() - 1; i++) {
			Point p1 = coords.get(i).asPoint(origin, zoom);
			Point p2 = coords.get(i + 1).asPoint(origin, zoom);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}

	}
}


