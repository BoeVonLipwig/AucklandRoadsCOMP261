import java.util.*;
import java.util.ArrayList;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Road {
    public final int roadId;
    public final int type;
    public final String name;
    private boolean highlighted;
    private List<Segment> segs= new ArrayList<>();

    //some of these are not used but may be later so i have put them in regardless
    Road(int roadId, int type, String label, String city, boolean oneWay, int speed, int roadclass, boolean car, boolean pedestrain, boolean bicyle) {
        this.type = type;
        this.roadId = roadId;
        name = label;
    }

    List<Segment> getSegs() {
        return segs;
    }

    public void addSegs(Segment seg) {
        segs.add(seg);
    }

    public boolean highlight(boolean toggle){
        return highlighted=toggle;
    }

    public boolean isHighlighted() {
        return highlighted;
    }
}
