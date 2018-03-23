package twodtree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer {

	public static final int PANEL_HEIGHT = 800;
	public static final int PANEL_WIDTH = 800;
	public static final int BUTTON_WIDTH = 300;
	private String label;
	private TwoDTree tree;
	private Random randomizer = new Random();
	private static int nRandom = 2;
	private static int nLevels = 1;
	private int currentStep = 0;
	private TreePanel treePanel;
	private DrawingPanel drawingPanel;
	
	// For demo and test purposes.
	private Point2[] points = new Point2[] { 
			new Point2(0.5, 0.7), 
			new Point2(0.75, 0.5), 
			new Point2(0.7, 0.15),
			new Point2(0.8, 0.25),
			new Point2(0.45, 0.4),
			new Point2(0.9, 0.15),
	};
	private String[] labels = new String[] { "A", "B", "C", "D", "E", "F" };
	
	public static void main(String[] args) {
		new Visualizer();
	}

	public Visualizer() {
		label = "A";
		tree = new TwoDTree(PANEL_WIDTH, PANEL_HEIGHT, BUTTON_WIDTH, BUTTON_WIDTH);

		JFrame frame = new JFrame("2D-Tree Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(BUTTON_WIDTH, PANEL_HEIGHT));

		JButton resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(200, 50));
		buttonPanel.add(resetButton);

		JButton stepButton = new JButton("Step");
		stepButton.setPreferredSize(new Dimension(200, 50));
		buttonPanel.add(stepButton);

		JButton randomButton = new JButton("Many Random");
		randomButton.setPreferredSize(new Dimension(200, 50));
		buttonPanel.add(randomButton);
		
		JButton perfectLevelButton = new JButton("Add perfect level");
		perfectLevelButton.setPreferredSize(new Dimension(200, 50));
		buttonPanel.add(perfectLevelButton);
		
		// Add tree visualizer
		treePanel = new TreePanel(tree);
		treePanel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_WIDTH));
		buttonPanel.add(treePanel);
		
		// Add panel to draw coordinate plane. Top-left = (0,0) down to bottom-right (1,1)
		drawingPanel = new DrawingPanel(tree, this, treePanel);
		drawingPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		
		// Show demo points
		for (int i = 0; i < points.length; i++) {
			tree.insert(points[i], labels[i]);
		}
		
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentStep < points.length) {
					tree.insert(points[currentStep], labels[currentStep]);
					drawingPanel.repaint();
					treePanel.repaint();
					currentStep++;
				}
			}
		});

		randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Adding random points");
				for (int i = 0; i < nRandom; i++) {
					tree.insert(new Point2(randomizer.nextDouble(), randomizer.nextDouble()), "");
				}
				nRandom *= 2;
				drawingPanel.repaint();
			}
		});
		
		perfectLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Adding perfect level");
				Testing.buildTest(tree, nLevels);
				nLevels++;
				drawingPanel.repaint();
			}
		});
		
		frame.add(buttonPanel, BorderLayout.WEST);
		frame.add(drawingPanel, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	protected void reset() {
		label = "A";
		drawingPanel.clear();
		treePanel.clear();
		currentStep = 0;
	}

	public String nextLabel() {
		String toReturn = label;
		char firstChar = label.charAt(0);
		char successor = (char)(((int)firstChar) + 1);
		label = Character.toString(successor);
		return toReturn;
	}
}
