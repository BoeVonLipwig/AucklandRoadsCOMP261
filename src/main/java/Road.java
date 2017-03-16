import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Road {
    public final int roadId;
    public final int type;
    public final String name;
    public final List<Segment> segs = new ArrayList<Segment>();

    //some of these are not used but may be later so i have put them in regardless
    Road(int roadId, int type, String label, String city, boolean oneWay, int speed, int roadclass, boolean car, boolean pedestrain, boolean bicyle) {
        this.type = type;
        this.roadId = roadId;
        name = label;
    }

    void addSeg(double length, int node1, int node2, Set<Location> coords) {
        segs.add(new Segment(length, node1, node2, coords));
    }
}
