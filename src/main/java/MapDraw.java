import trie.Trie;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	private HashMap<Integer, Intersection> interMap = new HashMap<>();
	private HashMap<Integer, Road> roads = new HashMap<>();
	private HashMap<String, Integer> roadNames = new HashMap<>();
	private List<Road> highlighted = new ArrayList<>();
	private Intersection preSelectedIntersection;
	private Intersection selectedIntersection;
	private Location origin = new Location(-7, 0);
	private int zoom = 50;
	private static final double moveBy = 1.4;
	private List<Intersection> path = null;
	private MapHandler MH = new MapHandler(intersections);
	private List<Intersection> artPoints;
	private boolean showArt = false;
	private double totalLength = 0, totalTime = 0;

	//may need to be public for testing
	private MapDraw() {
		redraw();
	}

	protected void redraw(Graphics g) {
		totalTime=0;
		totalLength=0;
		g.translate(
				getDrawingAreaDimension().width / 2,
				getDrawingAreaDimension().height / 2);
		roads.values().forEach(r -> r.draw(1, g, origin, zoom));
		intersections.forEach(i -> i.draw(g, origin, zoom));
		if (selectedIntersection != null) {
			StringBuilder output = printIntersectionInfo();
			getTextOutputArea().setText(output.toString());
			selectedIntersection.setHighlighted(1);
			selectedIntersection.draw(g, origin, zoom);
			if (preSelectedIntersection != null) {
				preSelectedIntersection.setHighlighted(3);
				preSelectedIntersection.draw(g, origin, zoom);
				preSelectedIntersection.setHighlighted(2);
			}
		}
		if (path != null) {
			for (int i = 0; i < path.size() - 1; i++) {
				path.get(i).draw(g, origin, zoom);
				for (Segment cur : path.get(i).getSegments()) {
					if (cur.findOtherEnd(path.get(i)).equals(path.get(i + 1))) {
						cur.draw(3, g, origin, zoom);
					}
					totalLength += cur.getLength();
					totalTime += calcTime(cur);
				}
				path.get(i).setHighlighted(0);
			}
			printRoute();
		}
		if (showArt) {
			for (Intersection artPoint : artPoints) {
				artPoint.setHighlighted(4);
				artPoint.draw(g, origin, zoom);
			}
		}
	}

	private void printRoute(){
		String unit="Hours: ";
		int dp=2;
		if(totalTime<1){
			totalTime*=60;
			unit="Minutes: ";
			dp=1;
		}
		getTextOutputArea().setText("");
		Map<String, Double> timeOnRoad=new HashMap<>();
		for (int i = 0; i < path.size()-1; i++) {
			List<Segment> segs=new ArrayList<>(path.get(i).getSegments());
			for (Segment seg : segs) {
				if (seg.findOtherEnd(path.get(i)).equals(path.get(i + 1))) {
					String roadName = seg.getRoad().getName();
					if (timeOnRoad.containsKey(roadName)) {
						timeOnRoad.put(roadName, timeOnRoad.get(roadName) + seg.getLength());
					} else {
						timeOnRoad.put(roadName, seg.getLength());
					}
				}
			}
		}
		for (Map.Entry<String, Double> entry : timeOnRoad.entrySet()) {
			getTextOutputArea().append(entry.getKey() + ": " + round(entry.getValue(), 3) + "km \n");
		}
		getTextOutputArea().append("\n Total time traveling: "+round(totalLength,dp)+" "+unit);
	}

	// helper function taken from stackoverflow: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private double calcTime(Segment cur) {
		double roadSpeed = cur.getRoad().speed;
		if(roadSpeed==0){
			roadSpeed=5;
		}else roadSpeed*=20;
		return cur.getLength()/roadSpeed;
	}

	public void toggleArt() {
		showArt = !showArt;
		getTextOutputArea().setText("There are " + artPoints.size() + " articulation points.");
	}


	protected void onClick(MouseEvent e) {
		if (selectedIntersection != null) {
			preSelectedIntersection = selectedIntersection;
		}
		Point temp = new Point(e.getX() - (getDrawingAreaDimension().width / 2), e.getY() - (getDrawingAreaDimension().height / 2));
		Location mouseLocation = Location.newFromPoint(temp, origin, zoom);
		double lowestDistance = intersections.get(0).location.distance(mouseLocation);
		for (Intersection intersection : intersections) {
			if (intersection.location.isClose(mouseLocation, lowestDistance)) {
				selectedIntersection = intersection;
				lowestDistance = selectedIntersection.location.distance(mouseLocation);
			}
		}
	}

	private StringBuilder printIntersectionInfo() {
		int id = selectedIntersection.nodeId;
		Road r;
		List<Segment> segs;
		int count = 0;
		String[] toRoads = new String[20];
		StringBuilder roadNames = new StringBuilder();
		for (Map.Entry<Integer, Road> entry : roads.entrySet()) {
			r = entry.getValue();
			segs = r.getSegs();
			for (Segment seg : segs) {
				if (seg.getNode1ID() == id || seg.getNode2ID() == id) {
					if (!Arrays.asList(toRoads).contains(r.getName())) {
						toRoads[count++] = r.getName();
					}
				}
			}
		}
		for (String toRoad : toRoads) {
			if (toRoad != null) {
				roadNames.append("\n\n").append(toRoad);
			} else break;
		}
		return roadNames;
	}

	protected void onSearch() {
		if (Objects.equals(getSearchBox().getText(), null) || Objects.equals(getSearchBox().getText(), "")) return;
		highlighted.forEach(road -> road.highlight(false));
		highlighted.clear();
		StringBuilder output = new StringBuilder();
		selectedIntersection = null;
		for (String roadName : trie.find(getSearchBox().getText())) {
			output.append(roadName).append("\n");
			Road r = roads.get(roadNames.get(roadName));
			highlighted.add(r);
			r.highlight(true);
		}
		getTextOutputArea().setText(output.toString());
	}

	protected void onMove(Move m) {
		switch (m) {
			case NORTH:
				origin = origin.moveBy(0, moveBy);
				break;
			case EAST:
				origin = origin.moveBy(moveBy, 0);
				break;
			case SOUTH:
				origin = origin.moveBy(0, -moveBy);
				break;
			case WEST:
				origin = origin.moveBy(-moveBy, 0);
				break;
			case ZOOM_IN:
				zoom += 2;
				break;
			case ZOOM_OUT:
				zoom -= 2;
				break;
			default:
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
				Intersection temp = new Intersection(Location.newFromLatLon(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])), Integer.parseInt(tokens[0]));
				interMap.put(temp.nodeId, temp);
				intersections.add(temp);
			}
			f.close();
			//Adds roads to the roads hash map based on their road id
			f = new BufferedReader(new FileReader(roadList));
			//skips first line as that contains the titles
			f.readLine();
			while ((line = f.readLine()) != null) {
				tokens = line.split("\t");
				trie.add(tokens[2]);
				roadNames.put(tokens[2], Integer.parseInt(tokens[0]));
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
				tempSeg = new Segment(roads.get(Integer.parseInt(tokens[0])), Double.parseDouble(tokens[1]), interMap.get(Integer.parseInt(tokens[2])), interMap.get(Integer.parseInt(tokens[3])), locations);
				roads.get(Integer.parseInt(tokens[0])).addSegs(tempSeg);
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
		artPoints = MH.findArtPoints();
	}

	@Override
	protected void findPath() {
		if (selectedIntersection != null || preSelectedIntersection != null) {
			assert selectedIntersection != null;
			path = MH.path(preSelectedIntersection, selectedIntersection);
			for (int i = 0; i < path.size() - 1; i++) {
				path.get(i).setHighlighted(3);
			}
		}
	}


	public static void main(String[] args) {
		new MapDraw();
	}
}
