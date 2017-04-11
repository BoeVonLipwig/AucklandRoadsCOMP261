import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 11/04/2017.
 */

class AStarNode implements Comparable<AStarNode>{
	private Intersection i;
	private double actualCost;
	private double heuristicCost;
	double costFromStart;
	private AStarNode parent;

	AStarNode(AStarNode parent, double actualCost, double heuristicCost, Intersection i) {
		this.parent = parent;
		this.actualCost = actualCost;
		this.heuristicCost = heuristicCost;
		this.i = i;
		costFromStart = actualCost + heuristicCost;
	}

	List<AStarNode> getNeighbours() {
		return i.getSegments().stream()
				.map(seg -> seg.findOtherEnd(i))
				.map(node -> new AStarNode(this, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, node))
				.collect(Collectors.toList());
	}

	List<Segment> getSegments(){
		ArrayList<Segment> cur= new ArrayList<>();
		cur.addAll(i.getSegments());
		return cur;
	}

	//getters and setters
	Intersection getI() {
		return i;
	}

	double getActualCost() {
		return actualCost;
	}

	double getHeuristicCost() {
		return heuristicCost;
	}

	void setHeuristicCost(double heuristicCost) {
		this.heuristicCost = heuristicCost;
	}

	double getCostFromStart() {
		return costFromStart;
	}

	void setCostFromStart(double costFromStart) {
		this.costFromStart = costFromStart;
	}

	AStarNode getParent() {
		return parent;
	}

	void setParent(AStarNode parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(AStarNode o) {
		if (actualCost>o.actualCost){
			return 1;
		}else if(actualCost>o.actualCost){
			return -1;
		}
		return 0;
	}
}
