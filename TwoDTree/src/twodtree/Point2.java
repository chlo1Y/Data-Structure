package twodtree;

public class Point2 {
	public static final double EPSILON = 0.0000000001;
	public double x;
	public double y;

	public Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2(Point2 p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (object == null)
			return false;
		if (this.getClass() != object.getClass())
			return false;
		Point2 other = (Point2) object;
		return this.distanceTo(other) < EPSILON;
	}

	@Override
	public String toString() {
		return String.format("(%4.2f,%4.2f)", x, y);
	}

	public double distanceTo(Point2 other) {
		double deltaX = this.x - other.x;
		double deltaY = this.y - other.y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

}
