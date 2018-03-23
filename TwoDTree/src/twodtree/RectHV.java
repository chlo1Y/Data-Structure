package twodtree;

/**
 * A 2D axis-aligned rectangle based on Princeton's RectHV class.
 * 
 * @author Matt Boutell
 */
public class RectHV {
	private final double xmin, ymin; // minimum x- and y-coordinates
	private final double xmax, ymax; // maximum x- and y-coordinates

	/**
	 * Create an axis-aligned rectangle [xmin, xmax] x [ymin, ymax]
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 */
	public RectHV(double xmin, double ymin, double xmax, double ymax) {
		if (xmax < xmin || ymax < ymin) {
			throw new IllegalArgumentException("Invalid rectangle");
		}
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public double xmin() {
		return xmin;
	}

	public double ymin() {
		return ymin;
	}

	public double xmax() {
		return xmax;
	}

	public double ymax() {
		return ymax;
	}

	// width and height of rectangle
	public double width() {
		return xmax - xmin;
	}

	public double height() {
		return ymax - ymin;
	}

	// does this axis-aligned rectangle intersect that one?
	public boolean intersects(RectHV that) {
		return this.xmax >= that.xmin && this.ymax >= that.ymin && that.xmax >= this.xmin && that.ymax >= this.ymin;
	}

	/**
	 * distance from p to closest point on this axis-aligned rectangle
	 * 
	 * @param p
	 * @return The distance from the given point p to this rectangle.
	 */
	public double distanceTo(Point2 p) {
		return Math.sqrt(this.distanceSquaredTo(p));
	}

	// distance squared from p to closest point on this axis-aligned rectangle
	public double distanceSquaredTo(Point2 p) {
		double dx = 0.0, dy = 0.0;
		if (p.x < xmin)
			dx = p.x - xmin;
		else if (p.x > xmax)
			dx = p.x - xmax;
		if (p.y < ymin)
			dy = p.y - ymin;
		else if (p.y > ymax)
			dy = p.y - ymax;
		return dx * dx + dy * dy;
	}

	// does this axis-aligned rectangle contain p?
	public boolean contains(Point2 p) {
		return (p.x >= xmin) && (p.x <= xmax) && (p.y >= ymin) && (p.y <= ymax);
	}

	/**
	 * Are these two axis-aligned rectangles equal?
	 */
	public boolean equals(Object y) {
		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;
		RectHV that = (RectHV) y;
		if (this.xmin != that.xmin)
			return false;
		if (this.ymin != that.ymin)
			return false;
		if (this.xmax != that.xmax)
			return false;
		if (this.ymax != that.ymax)
			return false;
		return true;
	}

	// return a string representation of this axis-aligned rectangle
	@Override
	public String toString() {
		return "[" + xmin + ", " + xmax + "] x [" + ymin + ", " + ymax + "]";
	}

}
