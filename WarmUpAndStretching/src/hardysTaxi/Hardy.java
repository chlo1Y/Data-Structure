package hardysTaxi;

import static hardysTaxi.TaxicabNumber.cube;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the static method hardySolutionsLessThan(N) which finds all
 * "taxicab numbers" that are less than n.  These are represented as TaxicabNumber objects:
 * each includes the sum and two different ways of writing that sum as
 * a sum of two cubes.
 * 
 * @author anderson  November, 2010.
 *
 */
public class Hardy {
	
	/**
	 * Returns floor of the cube root of n.
	 * Can you see why this method is useful for this problem?
	 * This is not a very efficient implementation.  
	 * (Not required) Can you think of a more efficient approach?
	 * 
	 * @param n a positive integer
	 * @return integer cube root of n
	 */
	public static int cubeRootFloor(int n){
		// Very inefficient, but quick to write.
		int i=0;
		while (cube(i) <= n)
			i++;
		return i-1; 
	}
		
	/**
	 * Computes and returns a sorted list of all taxicab numbers less than n.
	 * @param n a positive integer
	 * @return a List<TaxicabNumber>  object.  Each object contains the sum and two different ways to write it as a sum of cubes.
	 */
	
	public static List<TaxicabNumber> hardySolutionsLessThan(int n) {
		List<TaxicabNumber> result = new ArrayList<TaxicabNumber>();  // You are to populate this list.
		int limit = cubeRootFloor(n);
       /* Done: fill in the calculations */
		for(int b=1; b<=limit; b++){
			for(int a=1; a<=b; a++){
				if(cube(a)+cube(b)>=n){
					break;
				}
				for(int d=a; d<=limit; d++){
					for(int c=a; c<=d; c++){
						if(cube(a)+cube(b)==cube(c)+cube(d) && a!=c && b!=d && a!=d && b!=c){
							result.add(new TaxicabNumber(cube(a)+cube(b),a,b,c,d));
						}
					}
				}
			}
		}
		java.util.Collections.sort(result);
		return result;
	}
	
	/**
	 * main() is provided in case you want to test your code other than 
	 * with unit tests. Just put try various arguments in the method call below.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(hardySolutionsLessThan(1730));
	}

}
