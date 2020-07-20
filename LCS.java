package algo;
/**
 * 
 * @author Jireh Bethely
 * Finds the longest common substring of two strings
 * Note: letters don't have to be consecutive (i.e. LCS of "aabc" and "abc" is "abc")
 */
public class LCS {

	public static void main(String args[]) {
		String string1 =  "10010101"; 
		String string2 = "010110110";
		
		char[] str1 = string1.toCharArray();
		char[] str2 = string2.toCharArray();
		
		int[][] maxLength = new int[str1.length+1][str2.length+1];
		
		for (int i = 0; i < str1.length; i++) {
			maxLength[0][i] = 0;
		}
		for (int i = 0; i < str2.length; i++) {
			maxLength[i][0] = 0;
		}
		
		for (int i = 0; i < str1.length; i++) {
			for (int j = 0; j < str2.length; j++) {
				//System.out.print(str1[i] + " " + str2[j] + "\t");
				if (str1[i] == str2[j]) {
					maxLength[i+1][j+1] = maxLength[i][j]+1;
					continue;
				}
				maxLength[i+1][j+1] = maxLength[i][j+1] > maxLength[i+1][j] ? maxLength[i][j+1] : maxLength[i+1][j];
			}
			//System.out.println();
		}
		
		for (int i[] : maxLength) {
			for (int j : i) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
		
		System.out.println("\n" + maxLength[str1.length][str2.length] + "\n");
		
		int i = maxLength.length-1, j = maxLength[0].length-1;
		//Stack<Character> result = new Stack<>();
		
		while (i > 0) {
			if (maxLength[i][j] == maxLength[i-1][j]) {
				i--;
				continue;
			}
			else if (maxLength[i][j] == maxLength[i][j-1]) {
				j--;
				continue;
			}
			/*result.push*/System.out.print(str1[i-1]);
			i--;
			j--;
		}
		//while (!result.isEmpty()) { System.out.print(result.pop()); }
 	}
}
