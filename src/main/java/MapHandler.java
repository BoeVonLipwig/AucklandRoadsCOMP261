import java.util.*;

/**
 * Created by Shaun Sinclair
 * COMP261
 * 11/04/2017.
 */
public class MapHandler {
	MapHandler() {
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

	private double calcHur(Intersection a, Intersection b) {
		return a.location.distance(b.location);
	}

}
