
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Shaun Sinclair
 * COMP 261
 * 15/03/2017.
 **/

public class MapDraw extends GUI {
    private List<Intersection> intersections = new ArrayList<>();
    private HashMap<Integer, Road> roads = new HashMap<>();
    private Set<Segment> segs = new HashSet<>();
    private Location origin = new Location(-7, 0);
    private double zoom =50;

    public MapDraw() {
        intersections.clear();
        roads.clear();
        segs.clear();
        redraw();
    }

    protected void redraw(Graphics g) {
        segs.forEach(s -> s.draw(true, g, origin, zoom));
        intersections.forEach(i -> i.draw(true, g, origin, zoom));
    }

    protected void onClick(MouseEvent e) {

    }

    protected void onSearch() {

    }

    protected void onMove(Move m) {
        int moveBy = 5;
        switch (m) {
            case NORTH:
                origin = origin.moveBy(0, moveBy /(zoom/4));
                break;
            case EAST:
                origin = origin.moveBy(moveBy /(zoom/4), 0);
                break;
            case SOUTH:
                origin = origin.moveBy(0, -moveBy /(zoom/4));
                break;
            case WEST:
                origin = origin.moveBy(-moveBy /(zoom/4), 0);
                break;
            case ZOOM_IN:
                zoom += 2;
                break;
            case ZOOM_OUT:
                zoom -= 2;
                break;
        }
    }

    protected void onLoad(File nodes, File roadlist, File segments, File polygons) {
        String line;
        String[] tokens;
        List<Location> locations = new ArrayList<>();
        //try catch surrounding input to stop program dieing when incorrect info is passed
        try {
            BufferedReader f = new BufferedReader(new FileReader(nodes));
            while ((line = f.readLine()) != null) {
                //tokens are separated by tabs
                tokens = line.split("\t");
                intersections.add(new Intersection(Location.newFromLatLon(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])), Integer.parseInt(tokens[0])));
            }
            f.close();
            //Adds roads to the roads hash map based on their road id
            f = new BufferedReader(new FileReader(roadlist));
            //skips first line as that contains the titles
            f.readLine();
            while ((line = f.readLine()) != null) {
                tokens = line.split("\t");
                roads.put(Integer.parseInt(tokens[0]), new Road(Integer.parseInt(tokens[0]),
                        Integer.parseInt(tokens[1]),
                        tokens[2],
                        tokens[3],
                        tokens[4].equals("0"),
                        Integer.parseInt(tokens[5]),
                        Integer.parseInt(tokens[6]),
                        tokens[7].equals("0"),
                        tokens[8].equals("0"),
                        tokens[9].equals("0")));
            }
            f.close();
            f = new BufferedReader(new FileReader(segments));
            f.readLine();
            Segment tempSeg;
            List<Segment> curSegs= new ArrayList<>();
            while ((line = f.readLine()) != null) {
                tokens = line.split("\t");
                for (int i = 4; i < tokens.length - 1; i += 2) {
                    locations.add(Location.newFromLatLon(Double.parseDouble(tokens[i]), Double.parseDouble(tokens[i + 1])));
                }
                tempSeg=new Segment(Double.parseDouble(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), locations);
                roads.get(Integer.parseInt(tokens[0])).addSegs(tempSeg);
                segs.add(tempSeg);
                locations.clear();
            }
            f.close();
            if (polygons != null) {
                f = new BufferedReader(new FileReader(polygons));
                while ((line = f.readLine()) != null) {
                    tokens = line.split("\t");
                }
                f.close();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MapDraw();
    }
}
