
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
  Created by Shaun Sinclair
  COMP 261
  15/03/2017.
 **/

public class MapDraw extends GUI {
    List<Intersection> intersections = new ArrayList<>();
    HashMap<Integer, Road> roads = new HashMap<>();

    public MapDraw() {
        intersections.clear();
        redraw();
    }

    protected void redraw(Graphics g) {

    }

    protected void onClick(MouseEvent e) {

    }

    protected void onSearch() {

    }

    protected void onMove(Move m) {

    }

    protected void onLoad(File nodes, File roadlist, File segments, File polygons) {
        String line;
        String[] tokens;
        Set<Location> locations = null;
        //try catch surrounding input to stop program dieing when incorrect info is passed
        try {
            BufferedReader f = new BufferedReader(new FileReader(nodes));
            while ((line = f.readLine()) != null) {
                //tokens are separated by tabs
                tokens = line.split("/t");
                intersections.add(new Intersection(new Location(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])), Integer.parseInt(tokens[0])));
            }
            f.close();
            f = new BufferedReader(new FileReader(roadlist));
            //skips first line as that contains the titles
            f.readLine();
            while ((line = f.readLine()) != null) {
                tokens = line.split("/t");
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
            while ((line = f.readLine()) != null) {
                tokens = line.split("/t");
                for (int i = 3; tokens[i + 2] != null; i += 2) {
                    locations.add(new Location(Double.parseDouble(tokens[i]), Double.parseDouble(tokens[i + 1])));
                }
                roads.get(Integer.parseInt(tokens[0])).addSeg(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), locations);
            }
            f.close();
            if (polygons != null) {
                f = new BufferedReader(new FileReader(polygons));
                while ((line = f.readLine()) != null) {
                    tokens = line.split("/t");
                }
                f.close();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        new MapDraw();
    }
}
