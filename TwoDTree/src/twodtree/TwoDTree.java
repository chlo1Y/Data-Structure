package twodtree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A 2D Tree implementation.
 * 
 * @author Matt Boutell and TODO: You!
 */
public class TwoDTree {
	/*
	 * TODO: Directions: Implement the methods below. See the specifications for
	 * details.
	 */
	private Node root;
	private Point2 nearestFound = null;
	public final Node NULL_NODE = new Node();

	/** For drawing the plane. */
	public static final double DOT_RADIUS = 5;
	private int planePanelWidth;
	private int planePanelHeight;

	// For drawing the tree
	private static final int MARGIN = 30;
	private static final double RADIUS_SCALE_FACTOR = 0.75;
	private static final double FONT_SCALE_FACTOR = 1.5;
	private static final double DIRECTION_TYPE_SCALE_FACTOR = 1.1;
	private int treePanelWidth;
	private int treePanelHeight;
	// The number of pixels horizontally and vertically between nodes.
	private int xStep, yStep;
	private double radius;
	// font to use for labeling nodes
	private Font font;
	private int fontSize;

	/**
	 * Constructs an empty tree.
	 * 
	 */
	public TwoDTree() {
		this(0, 0, 0, 0); // When called within params, it won't be visualized
	}

	/**
	 * Constructs an empty tree.
	 * 
	 */
	public TwoDTree(int planePanelWidth, int planePanelHeight, int treePanelWidth, int treePanelHeight) {
		root = NULL_NODE;
		this.planePanelWidth = planePanelWidth;
		this.planePanelHeight = planePanelHeight;
		this.treePanelWidth = treePanelWidth;
		this.treePanelHeight = treePanelHeight;
	}

	/**
	 * Inserts the given point into the tree
	 * 
	 * @param p
	 *            A point to insert.
	 */
	public void insert(Point2 p, String label) {
		//helper method in Node class
		this.root = this.root.insertHelper(p, label, Direction.X, new RectHV(0, 0, 1, 1), 0);
	}

	/**
	 * Checks to see if the given query point is in this tree.
	 * 
	 * @param q
	 *            Query point.
	 * @return True if and only if the query point is in this tree.
	 * 
	 */
	public boolean contains(Point2 q) {
		//herlper method in Node class
		return this.root.containsHelper(q);
	}

	/**
	 * Finds the closest point in the tree to the query point.
	 * 
	 * @param q
	 *            The query point
	 * @throws IllegalStateException.
	 *             If the tree is empty.
	 * @return The closest point
	 */
	public Point2 nearestNeighbor(Point2 q) throws IllegalStateException {
		if (this.root == NULL_NODE) {
			throw new IllegalStateException();
		}
		// assume the frist Node is the pt looking for 
		Point2 cloestPt = this.root.p;
		// pass in helper method and make comparesion
		return this.root.nearestNeighborHelper(q, cloestPt);

	}

	public void drawTree(Graphics2D g) {
		int nodeCountPlusOne = root.setInOrderNumbers(1);
		this.xStep = (this.treePanelWidth - 2 * MARGIN) / nodeCountPlusOne;
		this.yStep = (this.treePanelHeight - 2 * MARGIN) / (height() + 2);
		this.radius = ((xStep < yStep) ? xStep : yStep) * RADIUS_SCALE_FACTOR;
		this.fontSize = (int) (radius * FONT_SCALE_FACTOR * 96 / 72);
		this.font = new Font("Monospaced", Font.BOLD, fontSize);
		root.drawTree(g, -1, -1);
	}

	public void clear() {
		root = NULL_NODE;
		nearestFound = null;
	}

	@Override
	public String toString() {
		if (root == NULL_NODE) {
			return "()";
		}
		StringBuilder sb = new StringBuilder();
		root.buildString(sb);
		return sb.toString();
	}

	public void draw(Graphics2D g2, double minX, double maxX, double minY, double maxY) {
		root.drawInPlane(g2, minX, maxY, minY, maxY);

		if (nearestFound != null) {
			Ellipse2D.Double nodeDot = new Ellipse2D.Double(screenX(nearestFound.x) - DOT_RADIUS,
					screenY(nearestFound.y) - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
			g2.setColor(Color.RED);
			g2.fill(nodeDot);
		}
	}

	private int screenX(double x) {
		return (int) (x * planePanelWidth);
	}

	private int screenY(double y) {
		return (int) (y * planePanelHeight);
	}

	private int height() {
		return root.height();
	}

	/**
	 * The direction of each node is given in this enumeration. The root always
	 * splits the plane depending on its point's x-coordinate, so has direction
	 * of Direction.X. This is shown using a vertical line (see node A in the
	 * screenshot in the specification) since splitting based on X splits the
	 * plane using a vertical line. The root's children split the plane
	 * depending on the y-coordinate, so will be of type Direction.Y (the
	 * horizontal lines on E and B in the screenshot).
	 */
	enum Direction {
		X, Y
	}

	public class Node {
		// The two data fields: a label and a point
		public String label;
		public Point2 p;

		// Children
		Node topLeft;
		Node bottomRight;

		// The enumeration above.
		public Direction dir;

		// Each node knows the bounds of the rectangle it is part of. Helpful
		// when searching. See the spec for details.
		public RectHV bounds;

		// For tree drawing
		// depth is used by drawTree below to place the nodes correctly when
		// drawing the tree.
		// You need to set it when you insert a node.
		private int depth;
		// inOrderNumber is both calculated and used by code below. You can
		// ignore it.
		private int inOrderNumber;

		// This one is used by the NULL_NODE.
		public Node() {
			// do nothing
		}
		
		public Point2 nearestNeighborHelper(Point2 pt, Point2 container) {
			// if current Node direction is split on the X
			if (this.dir == Direction.X) {
				// compare x 
				if (pt.x < this.p.x) {
					// if there is no node whose x is smaller than current x
					// update the current containr to the current node
					if (this.topLeft == NULL_NODE) {
						container = this.p;
					} else {
						// if there is still more, check to see if there is anything closer
						// if so update the container
						container = this.topLeft.nearestNeighborHelper(pt, container);
						if (this.p.distanceTo(pt) < container.distanceTo(pt))
							container = this.p;
					}
					// after the above check is done, check to see of there could be anything 
					// on the bounary which is close 
					// if so, search the closer bondary
					if(this.topLeft!=NULL_NODE && this.bottomRight!=NULL_NODE){
						double checkingBounds = this.bottomRight.bounds.distanceTo(pt);
						if (checkingBounds <= container.distanceTo(pt)) {
							Point2 temp = this.bottomRight.nearestNeighborHelper(pt, container);
							if (temp.distanceTo(pt) < container.distanceTo(pt))
								container = temp;
						}
					}

				} else {
					// compare x, if it's larger than cureent x
					if (this.bottomRight == NULL_NODE) {
						container = this.p;
					} else {
						// if there is still more, check to see if there is anything closer
						// if so update the container
						container = this.bottomRight.nearestNeighborHelper(pt, container);
						if (this.p.distanceTo(pt) < container.distanceTo(pt))
							container = this.p;
					}
					// after the above check is done, check to see of there could be anything 
					// on the bounary which is close 
					// if so, search the closer bondary
					if(this.topLeft!=NULL_NODE && this.bottomRight!=NULL_NODE){
						double checkingBounds = this.topLeft.bounds.distanceTo(pt);
						if (checkingBounds <= container.distanceTo(pt)) {
							Point2 temp = this.topLeft.nearestNeighborHelper(pt, container);
							if (temp.distanceTo(pt) < container.distanceTo(pt))
								container = temp;
						}
					}

				}
			} else {
				// if there is no node whose y is smaller than current y
				// update the current containr to the current node
				if (pt.y < this.p.y) {
					if (this.topLeft == NULL_NODE) {
						container = this.p;
					} else {
						// if there is still more, check to see if there is anything closer
						// if so update the container
						container = this.topLeft.nearestNeighborHelper(pt, container);
						if (this.p.distanceTo(pt) < container.distanceTo(pt))
							container = this.p;
					}
					// after the above check is done, check to see of there could be anything 
					// on the bounary which is close 
					// if so, search the closer bondary
					if(this.topLeft!=NULL_NODE && this.bottomRight!=NULL_NODE){
						double checkingBounds = this.bottomRight.bounds.distanceTo(pt);
						if (checkingBounds <= container.distanceTo(pt)) {
							Point2 temp = this.bottomRight.nearestNeighborHelper(pt, container);
							if (temp.distanceTo(pt) < container.distanceTo(pt))
								container = temp;
						}
					}

				} else {
					if (this.bottomRight == NULL_NODE) {
						container = this.p;
					} else {
						// if there is still more, check to see if there is anything closer
						// if so update the container
						container = this.bottomRight.nearestNeighborHelper(pt, container);
						if (this.p.distanceTo(pt) < container.distanceTo(pt))
							container = this.p;
					}
					// after the above check is done, check to see of there could be anything 
					// on the bounary which is close 
					// if so, search the closer bondary
					if(this.topLeft!=NULL_NODE && this.bottomRight!=NULL_NODE){
						double checkingBounds = this.topLeft.bounds.distanceTo(pt);
						if (checkingBounds <= container.distanceTo(pt)) {
							Point2 temp = this.topLeft.nearestNeighborHelper(pt, container);
							if (temp.distanceTo(pt) < container.distanceTo(pt))
								container = temp;
						}
					}
				}
			}
			return container;
		}

		public boolean containsHelper(Point2 p) {
			// if it doesn;t exit 
			if (this == NULL_NODE) {
				return false;
			}
			// if such node is found
			if (this.p.equals(p)) {
				return true;
			}
			// if current direction is x, search the x axis 
			if (this.dir == Direction.X) {
				if (p.x < this.p.x) {
					// smaller, search on the smaller side
					return this.topLeft.containsHelper(p);
				}//search on the large side 
				return this.bottomRight.containsHelper(p);
			}
			// search on the y side if current direction is y 
			if (p.y < this.p.y) {
				// same as above 
				return this.topLeft.containsHelper(p);
			}
			return this.bottomRight.containsHelper(p);
		}

		public Node insertHelper(Point2 p, String label, Direction dir, RectHV bounds, int depth) {
			// if a null node is reached, do the insertion 
			if (this == NULL_NODE)
				return new Node(p.x, p.y, label, dir, bounds, depth);
			// if current durection is X, check on the x level
			if (dir == Direction.X) {
				// if inserting pt is smaller
				if (this.p.x > p.x) {
					// find a bound frist 
					RectHV bound1 = new RectHV(this.bounds.xmin(), this.bounds.ymin(), this.p.x, this.bounds.ymax());
					// recurse and continue find on the left side 
					this.topLeft = this.topLeft.insertHelper(p, label, Direction.Y, bound1, depth);
				} else {
					// if it's greater, find it on the right side 
					RectHV bound2 = new RectHV(this.p.x, this.bounds.ymin(), this.bounds.xmax(), this.bounds.ymax());
					this.bottomRight = this.bottomRight.insertHelper(p, label, Direction.Y, bound2, depth);
				}
			} else {
				// if the node is split on direction y
				if (this.p.y > p.y) {
					// same as above
					RectHV bound3 = new RectHV(this.bounds.xmin(), this.bounds.ymin(), this.bounds.xmax(), this.p.y);
					this.topLeft = this.topLeft.insertHelper(p, label, Direction.X, bound3, depth);
				} else {
					RectHV bound4 = new RectHV(this.bounds.xmin(), this.p.y, this.bounds.xmax(), this.bounds.ymax());
					this.bottomRight = this.bottomRight.insertHelper(p, label, Direction.X, bound4, depth);
				}
			}
			// return the node
			return this;
		}

		public Node(Point2 p) {
			if (p == null) {
				return;
			}
			this.p = new Point2(p);
			this.topLeft = NULL_NODE;
			this.bottomRight = NULL_NODE;
			this.bounds = null;
		}

		// You will probably use this when writing insert()
		public Node(double x, double y, String label, Direction dir, RectHV bounds, int depth) {
			this.p = new Point2(x, y);
			this.label = label;
			this.dir = dir;
			this.topLeft = NULL_NODE;
			this.bottomRight = NULL_NODE;
			this.bounds = bounds;
			this.depth = depth;
		}

		private void buildString(StringBuilder sb) {
			if (this == NULL_NODE) {
				return;
			}
			if (topLeft != NULL_NODE) {
				sb.append("(");
				topLeft.buildString(sb);
				sb.append(")");
			}
			sb.append(String.format("%s(%4.2f,%4.2f)", label, p.x, p.y));
			if (bottomRight != NULL_NODE) {
				sb.append("(");
				bottomRight.buildString(sb);
				sb.append(")");
			}
		}

		private void drawInPlane(Graphics2D g2, double minX, double maxX, double minY, double maxY) {
			if (this == NULL_NODE) {
				return;
			}

			// Dot
			Ellipse2D.Double nodeDot = new Ellipse2D.Double(screenX(p.x) - DOT_RADIUS, screenY(p.y) - DOT_RADIUS,
					DOT_RADIUS * 2, DOT_RADIUS * 2);
			g2.fill(nodeDot);

			// Label
			int xOffset = this.dir == Direction.X ? 10 : 0;
			int yOffset = this.dir == Direction.X ? 0 : 20;
			g2.drawString(label, (int) screenX(p.x) + xOffset, (int) screenY(p.y) + yOffset);

			if (dir == Direction.X) {
				// Draw vertical line from (x, minY) to (x, maxY)
				Line2D.Double line = new Line2D.Double(new Point2D.Double(screenX(p.x), screenY(minY)),
						new Point2D.Double(screenX(p.x), screenY(maxY)));
				g2.draw(line);
				topLeft.drawInPlane(g2, minX, p.x, minY, maxY);
				bottomRight.drawInPlane(g2, p.x, maxX, minY, maxY);
			} else {
				// VERTICAL separation, so draw horizontal line
				Line2D.Double line = new Line2D.Double(new Point2D.Double(screenX(minX), screenY(p.y)),
						new Point2D.Double(screenX(maxX), screenY(p.y)));
				g2.draw(line);
				topLeft.drawInPlane(g2, minX, maxX, minY, p.y);
				bottomRight.drawInPlane(g2, minX, maxX, p.y, maxY);
			}
		}

		private void drawTree(Graphics2D g, double parentX, double parentY) {
			if (this == NULL_NODE) {
				return;
			}

			double centerX = this.inOrderNumber * xStep + MARGIN;
			double centerY = (this.depth + 1) * yStep + MARGIN;

			if (parentX > 0 && parentY > 0) {
				// Draws line
				g.setColor(Color.BLACK);
				double deltaX = parentX - centerX;
				double deltaY = parentY - centerY;
				double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
				double xOffset = deltaX * radius / distance;
				double yOffset = deltaY * radius / distance;
				Point2D.Double selfEdge = new Point2D.Double(centerX + xOffset, centerY + yOffset);
				Point2D.Double parentEdge = new Point2D.Double(parentX - xOffset, parentY - yOffset);
				g.draw(new Line2D.Double(selfEdge, parentEdge));
			}

			// Draws the circle
			Ellipse2D.Double circ = new Ellipse2D.Double(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
			g.setColor(Color.WHITE);
			g.fill(circ);
			g.setColor(Color.BLACK);
			g.draw(circ);

			// Labels the circle
			g.setFont(font);
			// coefficients on radius determined experimentally
			g.drawString(label.toString(), (int) (centerX - 0.5 * radius), (int) (centerY + 0.6 * radius));

			// Direction
			Point2D.Double first = new Point2D.Double(centerX, centerY);
			Point2D.Double second = new Point2D.Double(centerX, centerY);
			if (dir == Direction.X) {
				first.y -= radius * DIRECTION_TYPE_SCALE_FACTOR;
				second.y += radius * DIRECTION_TYPE_SCALE_FACTOR;
			} else {
				first.x -= radius * DIRECTION_TYPE_SCALE_FACTOR;
				second.x += radius * DIRECTION_TYPE_SCALE_FACTOR;
			}
			g.setStroke(new BasicStroke(2));
			g.draw(new Line2D.Double(first, second));

			// Draw children
			topLeft.drawTree(g, centerX, centerY);
			bottomRight.drawTree(g, centerX, centerY);
		}

		// The next two are helpers for the drawTree.
		private int height() {
			if (this == NULL_NODE) {
				return -1;
			}
			return Math.max(topLeft.height(), bottomRight.height()) + 1;
		}

		private int setInOrderNumbers(int nextNumber) {
			if (this == NULL_NODE) {
				return nextNumber;
			}
			nextNumber = topLeft.setInOrderNumbers(nextNumber);
			this.inOrderNumber = nextNumber++;
			return bottomRight.setInOrderNumbers(nextNumber);
		}
	}
}