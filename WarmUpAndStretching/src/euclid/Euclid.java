package euclid;

public class Euclid {
	/**
	 * Implementation requirement: must do recursively, as given in the spec.
	 * @param a First integer
	 * @param b Second integer
	 * @return The greatest common divisor of a and b using Euclid's recursive algorithm. 
	 */
	public static long gcd(long a, long b) {
		// Done: Write this.
		//two base case when a or b is 0, which means the 
		//other one is the gcd
		if (a==0){
			return b;
		}
		if (b==0){
			return a;
		}
		
		
		//have the remainder and the samller number to continue gcd calculation 
		if(a>b){
			return gcd(a%b,b );
		}
		return gcd(a, b%a);
		
	}

}
