import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 16/03/2017.
 */
public class Road {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int roadId;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int type;
    private final String name;
    private boolean highlighted=false;
    @SuppressWarnings("SpellCheckingInspection")
    private List<Segment> segs= new ArrayList<>();

    //some of these are not used but may be later so i have put them in regardless
    @SuppressWarnings("unused")
    Road(int roadId, int type, String label, String city, boolean oneWay, int speed, int roadClass, boolean car, boolean pedestrian, boolean bicycle) {
        this.type = type;
        this.roadId = roadId;
        name = city +"\n"+ label;
    }

    void draw(Graphics g, Location origin, double zoom){
        segs.forEach(segment -> segment.draw(highlighted, g ,origin, zoom));
    }

    @SuppressWarnings("SpellCheckingInspection")
    List<Segment> getSegs() {
        return segs;
    }


    @SuppressWarnings("SpellCheckingInspection")
    void addSegs(Segment seg) {
        segs.add(seg);
    }

    boolean highlight(boolean toggle){
        return highlighted=toggle;
    }

    String getName() {
        return name;
    }
}
