import java.util.Set;

/**
 * Created by sdmsi
 * CGRA 151
 * 16/03/2017.
 */
public class Segment {

    public final double length;
    public final int node1;
    public final int node2;
    public final Set<Location> coords;

    Segment(double length, int node1, int node2, Set<Location> coords){
        this.length=length;
        this.node1=node1;
        this.node2=node2;
        this.coords=coords;
    }

}
