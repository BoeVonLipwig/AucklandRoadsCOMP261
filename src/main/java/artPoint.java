/**
 * Created by Shaun Sinclair
 * COMP261
 * 13/04/2017.
 */
public class artPoint {
	private Intersection cur;
	int count;
	private Intersection root;

	public artPoint(Intersection cur, int count, Intersection root) {
		this.cur = cur;
		this.count = count;
		this.root = root;
	}

	public Intersection getCur() {
		return cur;
	}

	public Intersection getRoot() {
		return root;
	}
}
