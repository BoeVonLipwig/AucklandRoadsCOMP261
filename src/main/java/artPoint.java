/**
 * Created by Shaun Sinclair
 * COMP261
 * 13/04/2017.
 */
public class artPoint {
	private AStarNode cur;
	int count;
	private AStarNode root;

	public artPoint(AStarNode cur, int count, AStarNode root) {
		this.cur = cur;
		this.count = count;
		this.root = root;
	}

	public AStarNode getCur() {
		return cur;
	}

	public AStarNode getRoot() {
		return root;
	}
}
