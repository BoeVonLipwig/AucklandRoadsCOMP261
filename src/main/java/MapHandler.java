import java.util.*;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 11/04/2017.
 */
public class MapHandler {
	private List<Intersection> intersections;
	private List<Intersection> articulationPoints = new ArrayList<>();
	MapHandler(List<Intersection> intersections) {
		this.intersections = intersections;
	}

	List<Intersection> path(Intersection start, Intersection end) {
		Queue<AStarNode> workingPath = new PriorityQueue<>();
		Set<AStarNode> visited = new HashSet<>();
		AStarNode Start = new AStarNode(null, 0, calcHur(start, end), start);
		workingPath.add(Start);
		while (!workingPath.isEmpty()) {
			AStarNode cur = workingPath.poll();
			if (cur.getI().equals(end)) {
				return retrace(cur);
			}
			for (AStarNode node : cur.getNeighbours()) {
				if (visited.contains(node)) {
					continue;
				}
				int nID = node.getI().nodeId;
				Segment pair = null;
				Set<Segment> curSegs = cur.getSegments();
				for (Segment curSeg : curSegs) {
					if (nID == curSeg.getNode1ID() || nID == curSeg.getNode2ID()) {
						pair = curSeg;
						break;
					}
				}
				@SuppressWarnings("ConstantConditions")
				double totalCost = cur.getActualCost() + pair.getLength();
				if (!workingPath.contains(node)) {
					workingPath.add(node);
				} else if (totalCost >= node.getActualCost()) {
					continue;
				}
				node.setParent(cur);
				node.setActualCost(totalCost);
				node.setHeuristicCost(calcHur(node.getI(), end));
				node.setCostFromStart(node.getActualCost() + node.getHeuristicCost());
			}
			visited.add(cur);
		}
		return null;
	}

	private List<Intersection> retrace(AStarNode goal) {
		List<Intersection> path = new ArrayList<>();
		while (goal != null) {
			path.add(goal.getI());
			goal = goal.getParent();
		}
		return path;
	}

	public List<Intersection> findArtPoints() {
		Intersection start = intersections.get(0);
		for (Intersection intersection : intersections) {
			intersection.count = Integer.MAX_VALUE;
		}
		start.count = 0;
		int numSubtrees = 0;
		for (int i = 0; i < start.getNeighbours().size(); i++) {
			if (start.getNeighbours().get(i).count == Integer.MAX_VALUE) {
				iterArtPts(start.getNeighbours().get(i), start);
				numSubtrees++;
			}
		}
		if (numSubtrees > 1) {
			articulationPoints.add(start);
		}
		return articulationPoints;
	}

	private void iterArtPts(Intersection first, Intersection root) {
		Stack<artPoint> working = new Stack<>();
		working.push(new artPoint(first, 1, root));
		while (!working.isEmpty()) {
			artPoint current = working.peek();
			if (current.getCur().count == Integer.MAX_VALUE) {
				current.getCur().count = current.count;
				current.getCur().reachBack = current.count;
				current.getCur().children = new ArrayDeque<>();
				for (Intersection i : current.getCur().getNeighbours()) {
					if (!(i.equals(current.getRoot()))){
						current.getCur().children.add(i);
					}
				}
			}else if(!current.getCur().children.isEmpty()){
				Intersection child=current.getCur().children.poll();
				if (child.count<Integer.MAX_VALUE){
					current.getCur().reachBack=(Integer.min(current.getCur().reachBack, child.count));
				}else {
					working.push(new artPoint(child, current.getCur().count+1, current.getCur()));
				}
			}else {
				if(!(current.getCur().equals(first))){
					if(current.getCur().reachBack>=current.getRoot().count){
						articulationPoints.add(current.getRoot());
						current.getRoot().reachBack=(Integer.min(current.getRoot().reachBack,current.getCur().reachBack));
					}
				}
				System.out.println("poping");
				working.pop();
			}
		}
	}

	private double calcHur(Intersection a, Intersection b) {
		return a.location.distance(b.location);
	}

}
