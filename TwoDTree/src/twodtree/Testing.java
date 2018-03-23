package twodtree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import twodtree.TwoDTree.Direction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Testing {

	private static int correctnessPoints = 0;
	private static int maxCorrectnessPoints = 60;
	private static int efficiencyPoints = 0;
	private static int maxEfficiencyPoints = 10;

	private Point2[] points = new Point2[] { new Point2(0.5, 0.7), new Point2(0.75, 0.5), new Point2(0.7, 0.15),
			new Point2(0.8, 0.25), new Point2(0.45, 0.4), new Point2(0.9, 0.15), };

	String[] labels = new String[] { "A", "B", "C", "D", "E", "F" };

	private Point2[] otherPoints = new Point2[] { new Point2(0.49, 0.6), new Point2(0.72, 0.49), new Point2(0.72, 0.14),
			new Point2(0.81, 0.26), new Point2(0.44, 0.34), new Point2(0.91, 0.14), };

	@Test
	public void testInsert() {
		TwoDTree t = new TwoDTree();
		assertEquals("()", t.toString());
		String[] answers = new String[] { "A(0.50,0.70)", "A(0.50,0.70)(B(0.75,0.50))",
				"A(0.50,0.70)((C(0.70,0.15))B(0.75,0.50))", "A(0.50,0.70)((C(0.70,0.15)(D(0.80,0.25)))B(0.75,0.50))",
				"(E(0.45,0.40))A(0.50,0.70)((C(0.70,0.15)(D(0.80,0.25)))B(0.75,0.50))",
				"(E(0.45,0.40))A(0.50,0.70)((C(0.70,0.15)((F(0.90,0.15))D(0.80,0.25)))B(0.75,0.50))" };

		for (int i = 0; i < answers.length; i++) {
			t.insert(points[i], labels[i]);
			assertEquals(answers[i], t.toString());
			correctnessPoints += 3;
		}
	}

	@Test
	public void testContains() {
		TwoDTree t = new TwoDTree();
		assertFalse(t.contains(new Point2(0.4, 0.6)));
		correctnessPoints += 2;

		for (int i = 0; i < points.length; i++) {
			t.insert(points[i], labels[i]);
		}

		for (int i = 0; i < points.length; i++) {
			assertTrue(t.contains(points[i]));
		}
		correctnessPoints += 2;

		for (int i = 0; i < otherPoints.length; i++) {
			assertFalse(t.contains(otherPoints[i]));
		}
		correctnessPoints += 2;
	}

	@Test
	public void testContainsRandomTree() {
		TwoDTree t = new TwoDTree();

		Random r = new Random(17); // seed so reproducible
		int nPoints = 10;
		List<Point2> points = new ArrayList<Point2>();
		for (int i = 0; i < nPoints; i++) {
			points.add(new Point2(r.nextDouble(), r.nextDouble()));
		}
		for (Point2 point : points) {
			t.insert(point, "X");
		}

		for (int i = 0; i < points.size(); i++) {
			assertTrue(t.contains(points.get(i)));
		}
		correctnessPoints += 3;

		for (int i = 0; i < nPoints; i++) {
			assertFalse(t.contains(new Point2(r.nextDouble(), r.nextDouble())));
		}
		correctnessPoints += 3;
	}

	@Test
	public void testNearest() {
		TwoDTree t = new TwoDTree();
		for (int i = 0; i < points.length; i++) {
			t.insert(points[i], labels[i]);
		}

		Point2 query;
		Point2 expected;

		query = new Point2(0.6, 0.75);
		expected = points[0];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 1;

		query = new Point2(0.8, 0.4);
		expected = points[1];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 1;

		query = new Point2(0.75, 0.7);
		expected = points[1];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.75, 0.15);
		expected = points[2];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.60, 0.05);
		expected = points[2];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.85, 0.35);
		expected = points[3];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.4, 0.5);
		expected = points[4];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.95, 0.10);
		expected = points[5];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;

		query = new Point2(0.88, 0.20);
		expected = points[5];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;
	}

	@Test
	public void testNearestNeedToLookBothSidesSimpleLeft() {
		TwoDTree t = new TwoDTree();
		Point2[] morePoints = new Point2[] { new Point2(0.5, 0.2), new Point2(0.75, 0.5), new Point2(0.4, 0.75) };

		for (int i = 0; i < morePoints.length; i++) {
			t.insert(morePoints[i], labels[i]);
		}

		Point2 query;
		Point2 expected;

		query = new Point2(0.55, 0.75);
		expected = morePoints[2];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;
	}

	@Test
	public void testNearestNeedToLookBothSidesSimpleRight() {
		TwoDTree t = new TwoDTree();
		Point2[] morePoints = new Point2[] { new Point2(0.5, 0.2), new Point2(0.25, 0.5), new Point2(0.6, 0.75) };

		for (int i = 0; i < morePoints.length; i++) {
			t.insert(morePoints[i], labels[i]);
		}

		Point2 query;
		Point2 expected;

		query = new Point2(0.45, 0.75);
		expected = morePoints[2];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;
	}

	@Test
	public void testNearestNeedToLookBothSidesMore() {

//		private Point2[] points = new Point2[] { new Point2(0.5, 0.7), new Point2(0.75, 0.5), new Point2(0.7, 0.15),
//				new Point2(0.8, 0.25), new Point2(0.45, 0.4), new Point2(0.9, 0.15), };

		TwoDTree t = new TwoDTree();
		for (int i = 0; i < points.length; i++) {
			t.insert(points[i], labels[i]);
		}
		
		Point2 query;
		Point2 expected;

		query = new Point2(0.51, 0.4);
		expected = points[4];
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;
	
		Point2 g = new Point2(0.99, 0.27); // below D and to right
		t.insert(g, "G");
		query = new Point2(0.99, 0.23); // above D, very close to G
		expected = g; 
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 2;
		
		Point2 h = new Point2(0.69, 0.4); // just left of C and down
		t.insert(h, "H");
		query = new Point2(0.71, 0.4); // right of C, close to H
		expected = h;
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 3;
		
		Point2 j = new Point2(0.75, 0.75); 
		t.insert(j, "J");
		Point2 k = new Point2(0.6, 0.51); 
		t.insert(k, "K");
		query = new Point2(0.6, 0.49); 
		expected = k;
		assertEquals(expected, t.nearestNeighbor(query));
		correctnessPoints += 3;
	}

	public static TwoDTree buildTest(TwoDTree t, int nLevels) {
		double x1 = 0.5;
		double y1 = 0.5;
		double nX = 1;
		double nY = 1;
		double deltaX = 1;
		double deltaY = 1;
		TwoDTree.Direction direction = TwoDTree.Direction.X;

		for (int level = 1; level <= nLevels; level++) {
			populateLevel(t, x1, y1, nX, nY, deltaX, deltaY, direction, level);
			// Alternate which direction we are splitting in.
			if (direction == TwoDTree.Direction.X) {
				deltaX /= 2;
				x1 /= 2;
				nX *= 2;
				direction = TwoDTree.Direction.Y;
			} else {
				deltaY /= 2;
				y1 /= 2;
				nY *= 2;
				direction = TwoDTree.Direction.X;
			}
		}
		return t;
	}

	private static void populateLevel(TwoDTree t, double x1, double y1, double nX, double nY, double deltaX,
			double deltaY, Direction direction, int level) {
		double x = x1;
		for (int nXCreated = 0; nXCreated < nX; nXCreated++) {
			double y = y1;
			for (int nYCreated = 0; nYCreated < nY; nYCreated++) {
				Point2 point = new Point2(x, y);
				t.insert(point, "" + level);
				y += deltaY;
			}
			x += deltaX;
		}
	}

	/**
	 * The funny test name is used in conjunction with FixMethodOrder to present
	 * the tests in ascending order. (The order of tests in Java 7 is
	 * nondeterministic). The ZZZ means that this test runs after the
	 * correctness points have been determined.
	 */
	@Test
	public void testZZZNearestEfficient() {
		if (correctnessPoints != maxCorrectnessPoints) {
			return;
		}

		TwoDTree tree = new TwoDTree();
		buildTest(tree, 20);
		int NUM_TO_SEARCH = 100;
		long startTime;
		double averageElapsedTime;
		startTime = System.currentTimeMillis();

		Random random = new Random(); // add seed for debugging
		for (int i = 0; i < NUM_TO_SEARCH; i++) {
			Point2 query = new Point2(random.nextDouble(), random.nextDouble());
			tree.nearestNeighbor(query);
		}

		averageElapsedTime = (System.currentTimeMillis() - startTime) / (double) NUM_TO_SEARCH;
		System.out.println(averageElapsedTime + " milliseconds");
		if (averageElapsedTime < 5) {
			efficiencyPoints = maxEfficiencyPoints;
		}
	}

	@AfterClass
	public static void displayPoints() {
		int totalPoints = 0;
		int maxTotalPoints = maxCorrectnessPoints + maxEfficiencyPoints;
		if (correctnessPoints == maxCorrectnessPoints) {
			totalPoints = correctnessPoints + efficiencyPoints;
			System.out.println("All basic tests passed");
			if (efficiencyPoints > 0) {
				System.out.println("Nearest was efficient");
			} else {
				System.out.println("Nearest was not efficient");
			}
		} else {
			System.out.println("Some basic tests failed. No efficiency points possible.");
			totalPoints = correctnessPoints;
		}

		System.out.printf("%2d/%2d points earned on unit tests.\n", totalPoints, maxTotalPoints);
	}
}
