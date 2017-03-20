import trie.Trie;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP 261
 * 15/03/2017.
 **/

public class MapDraw extends GUI {

    private Trie trie = new Trie();
    private List<Intersection> intersections = new ArrayList<>();
    private HashMap<Integer, Road> roads = new HashMap<>();
    private HashMap<String, Integer> roadNames = new HashMap<>();
    private List<Road> highlighted = new ArrayList<>();
    private Set<Segment> segments = new HashSet<>();
    private Intersection selectedIntersection;
    private Location origin = new Location(-7, 0);
    private double zoom = 50;
    private static final int moveBy = 5;

    public MapDraw() {
        redraw();
    }

    protected void redraw(Graphics g) {
        roads.values().forEach(r -> r.draw(g, origin, zoom));
        intersections.forEach(i -> i.draw(true, g, origin, zoom));
        if (selectedIntersection != null) {
            String output = printIntersectionInfo();
            getTextOutputArea().setText(output != null ? output : "");
            selectedIntersection.draw(false, g, origin, zoom);
        }
    }

    protected void onClick(MouseEvent e) {
        Point temp = new Point(e.getX(), e.getY());
        Location mouseLocation = Location.newFromPoint(temp, origin, zoom);
        double lowestDistance = intersections.get(0).location.distance(mouseLocation);
        for (Intersection intersection : intersections) {
            if (intersection.location.isClose(mouseLocation, lowestDistance)) {
                selectedIntersection = intersection;
                lowestDistance = selectedIntersection.location.distance(mouseLocation);
            }
        }
    }

    private String printIntersectionInfo() {
        int id = selectedIntersection.nodeId;
        Road r;
        List<Segment> segs;
        for (Map.Entry<Integer, Road> entry : roads.entrySet()) {
            r = entry.getValue();
            segs = r.getSegs();
            for (Segment seg : segs) {
                if (seg.getNode1() == id || seg.getNode2() == id) {
                    return r.getName();
                }
            }
        }
        return null;
    }

    protected void onSearch() {
        highlighted.forEach(road -> road.highlight(false));
        highlighted.clear();
        String output="";
        selectedIntersection=null;
        for (String roadName : trie.find(getSearchBox().getText())) {
            output+=roadName+"\n";
            Road r = roads.get(roadNames.get(roadName));
            highlighted.add(r);
            r.highlight(true);
        }
        getTextOutputArea().setText(output);
    }

    protected void onMove(Move m) {
        switch (m) {
            case NORTH:
                origin = origin.moveBy(0, moveBy / (zoom / 4));
                break;
            case EAST:
                origin = origin.moveBy(moveBy / (zoom / 4), 0);
                break;
            case SOUTH:
                origin = origin.moveBy(0, -moveBy / (zoom / 4));
                break;
            case WEST:
                origin = origin.moveBy(-moveBy / (zoom / 4), 0);
                break;
            case ZOOM_IN:
                zoom += 1.4;
                break;
            case ZOOM_OUT:
                zoom -= 1.4;
                break;
        }
    }

    protected void onLoad(File nodes, File roadList, File segments, File polygons) {
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
            f = new BufferedReader(new FileReader(roadList));
            //skips first line as that contains the titles
            f.readLine();
            while ((line = f.readLine()) != null) {
                tokens = line.split("\t");
                trie.add(tokens[2]);
                roadNames.put(tokens[2],Integer.parseInt(tokens[0]));
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
            while ((line = f.readLine()) != null) {
                tokens = line.split("\t");
                for (int i = 4; i < tokens.length - 1; i += 2) {
                    locations.add(Location.newFromLatLon(Double.parseDouble(tokens[i]), Double.parseDouble(tokens[i + 1])));
                }
                tempSeg = new Segment(Double.parseDouble(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), locations);
                roads.get(Integer.parseInt(tokens[0])).addSegs(tempSeg);
                this.segments.add(tempSeg);
                locations.clear();
            }
            f.close();
//            if (polygons != null) {
//                f = new BufferedReader(new FileReader(polygons));
//                while ((line = f.readLine()) != null) {
//                    tokens = line.split("\t");
//                }
//                f.close();
//            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new MapDraw();
    }
}
