import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sdmsi
 * COMP 261
 * 16/03/2017.
 */
public class Road {
    public final int roadid;
    public final int type;
    public final String name;
    public final List<Segment> segs = new ArrayList<Segment>();

    Road(int roadid, int type, String label, String city, boolean oneway, int speed, int roadclass, boolean car, boolean pedestrain, boolean bicyle) {
        this.type = type;
        this.roadid = roadid;
        name = label;
    }

    void addSeg(double length, int node1, int node2, Set<Location> coords) {
        segs.add(new Segment(length, node1, node2, coords));
    }
}
