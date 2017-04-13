import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Road {
	private final int roadId;
	private final int type;
	int highlighted = 1;
	private final String name;
	private List<Segment> segs = new ArrayList<>();

	//some of these are not used but may be later so i have put them in regardless
	@SuppressWarnings("unused")
	Road(int roadId, int type, String label, String city, boolean oneWay, int speed, int roadClass, boolean car, boolean pedestrian, boolean bicycle) {
		this.type = type;
		this.roadId = roadId;
		name = city + "\n" + label;
	}

	void draw(int highlighted, Graphics g, Location origin, double zoom) {
		this.highlighted = highlighted;
		segs.forEach(segment -> segment.draw(highlighted, g, origin, zoom));
	}

	@SuppressWarnings("SpellCheckingInspection")
	List<Segment> getSegs() {
		return segs;
	}


	@SuppressWarnings("SpellCheckingInspection")
	void addSegs(Segment seg) {
		segs.add(seg);
	}

	void highlight(boolean toggle) {
		if (toggle) highlighted = 2;
		else highlighted = 1;

	}

	String getName() {
		return name;
	}

	public int getRoadId() {
		return roadId;
	}
}
