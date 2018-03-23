package twodtree;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {

	private TwoDTree tree;

	public TreePanel(TwoDTree tree) {
		this.tree = tree;
	}

	public void clear() {
		tree.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		tree.drawTree(g2);
	}
}
