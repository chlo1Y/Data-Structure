package anagram;

import java.util.Arrays;

/**
 * This utility class can test whether two strings are anagrams.
 *
 * @author Claude Anderson.
 */
public class Anagram {

	/**
	 * We say that two strings are anagrams if one can be transformed into the
	 * other by permuting the characters (and ignoring case).
	 * 
	 * For example, "Data Structure" and "Saturated Curt" are anagrams,
	 * as are "Elvis" and "Lives".
	 * 
	 * @param s1
	 *            first string
	 * @param s2
	 *            second string
	 * @return true iff s1 is an anagram of s2
	 */
	public static boolean isAnagram(String s1, String s2) {
		// Done: implement this method
		char[] temp1=s1.toLowerCase().toCharArray();
		char[] temp2= s2.toLowerCase().toCharArray();
		//change all string to lower case array 
		
		Arrays.sort(temp1);
		Arrays.sort(temp2);
		//sort both array
		//if different lengths, which means it's not anagram for sure 
		if (temp1.length!=temp2.length){
			return false;
		}
		
		//compare each byte 
		for(int i=0; i<temp1.length; i++){
			if(temp1[i]!=temp2[i]){
				return false;
			}
			return true;
		}
		return false;
	}
}
