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
		start.count = 0;
		int numSubtrees = 0;
		for (int i = 0; i < start.getNeighbours().size(); i++) {
			if (start.getNeighbours().get(i).count == Integer.MAX_VALUE) {
				iterArtPts(start.getNeighbours().get(i), 1, start);
				numSubtrees++;
			}
		}
		if (numSubtrees > 1) {
			articulationPoints.add(start);
		}
		return articulationPoints;
	}

	private Stack<artPoint> iterArtPts(Intersection first, int count, Intersection root) {
		Stack<artPoint> working = new Stack<>();
		working.push(new artPoint(first, count, root));
		while (!working.empty()) {
			artPoint cur = working.peek();
			if (cur.getCur().count == Integer.MAX_VALUE) {
				cur.getCur().count = count;
				cur.getCur().reachBack = count;
				cur.getCur().children = new ArrayDeque<>();
				for (Intersection i : cur.getCur().getNeighbours()) {
					if (i!=cur.getRoot()){
						cur.getCur().children.add(i);
					}
				}
			}else if(!first.children.isEmpty()){
				Intersection child=cur.getCur().children.poll();
				if (cur.count<Integer.MAX_VALUE){
					cur.getCur().reachBack=(Integer.min(cur.getCur().reachBack, child.count));
				}else {
					working.push(new artPoint(cur.getCur(), count+1, root ));
				}
			}else {
				if(cur.getCur()!=first){
					if(cur.getRoot().reachBack>=cur.getRoot().count){
						articulationPoints.add(cur.getRoot());
					}
					cur.getRoot().reachBack=(Integer.min(cur.getRoot().reachBack,cur.getCur().count));
				}
				working.pop();
			}
		}
		return working;
	}

	private double calcHur(Intersection a, Intersection b) {
		return a.location.distance(b.location);
	}

}
