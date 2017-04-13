import java.util.List;
import java.util.Set;
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
	private double costFromStart;
	private AStarNode parent;

	AStarNode(AStarNode parent, double costFromStart, double heuristicCost, Intersection i) {
		this.parent = parent;
		this.costFromStart = costFromStart;
		this.heuristicCost = heuristicCost;
		this.i = i;
		actualCost = this.costFromStart + this.heuristicCost;
	}

	List<AStarNode> getNeighbours() {
		return i.getSegments().stream()
				.map(seg -> seg.findOtherEnd(i))
				.map(node -> new AStarNode(this, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, node))
				.collect(Collectors.toList());
	}

	Set<Segment> getSegments(){
		return i.getSegments();
	}

	//getters and setters
	Intersection getI() {
		return i;
	}

	double getActualCost() {
		return actualCost;
	}

	void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}

	double getHeuristicCost() {
		return heuristicCost;
	}

	void setHeuristicCost(double heuristicCost) {
		this.heuristicCost = heuristicCost;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((i == null) ? 0 : i.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AStarNode other = (AStarNode) obj;
		if (i == null) {
			if (other.i != null)
				return false;
		} else if (!i.equals(other.i))
			return false;
		return true;
	}

	@Override
	public int compareTo(AStarNode o) {
		if (this.actualCost>o.actualCost){
			return 1;
		}else if(this.actualCost>o.actualCost){
			return -1;
		}
		return 0;
	}
}
