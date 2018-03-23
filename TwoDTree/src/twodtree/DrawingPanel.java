package twodtree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel {
	private TwoDTree tree;
	private Dimension size;
	private Ellipse2D.Double currentDot;

	public DrawingPanel(final TwoDTree tree, final Visualizer visualizer, final TreePanel treePanel) {
		this.tree = tree;

		this.size = new Dimension(Visualizer.PANEL_WIDTH, Visualizer.PANEL_HEIGHT);
		this.currentDot = null;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point2 click = new Point2(((double) e.getX()) / size.getWidth(),
						((double) e.getY()) / size.getHeight());
				if (e.getButton() == MouseEvent.BUTTON1) {
					tree.insert(click, visualizer.nextLabel());

				} else {
					try {
						currentDot = new Ellipse2D.Double(e.getX() - TwoDTree.DOT_RADIUS,
								e.getY() - TwoDTree.DOT_RADIUS, TwoDTree.DOT_RADIUS * 2, TwoDTree.DOT_RADIUS * 2);
						System.out.println(click + "," + tree.nearestNeighbor(click));

					} catch (IllegalStateException exception) {
						// Ignore if they right click before putting any points
						// down.
					}
				}

				repaint();
				treePanel.repaint();
			}
		});
	}

	/**
	 * Clear everything in this panel.
	 */
	public void clear() {
		tree.clear();
		currentDot = null;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Draw the outline
		g2.setColor(Color.BLACK);
		Rectangle2D.Double box = new Rectangle2D.Double(0, 0, Visualizer.PANEL_WIDTH, Visualizer.PANEL_HEIGHT);
		g2.draw(box);

		// Draw the tree
		tree.draw(g2, 0, size.getWidth(), 0, size.getHeight());

		// Draw the point pressed
		g2.setColor(Color.BLUE);
		if (currentDot != null) {
			g2.fill(currentDot);
		}
	}
}
