package algo;
import java.util.ArrayList;

public class ChainMatrixMultiplication {

	/**
	 * Returns information about the matrix multiplication of AxB
	 * @param matrixA
	 * @param matrixB
	 * @return int[0] = number of steps need to perform multiplcation. int[1] = num of rows as result of mult., int[2] = num of cols as result from mult.
	 */
	public static int[] AxB(Integer[][] matrixA, Integer[][] matrixB) {
		if (matrixA.length == 0 || matrixB.length == 0) {
			return new int[] {0, 0, 0};
		}

		int result[] = new int[3];
		result[0] = costOfAxB(matrixA.length, matrixA[0].length, matrixB[0].length);
		result[1] = matrixA.length;
		result[2] = matrixB[0].length;
		return result;
	}

	public static int costOfAxB(int rowsOfA, int colsOfA, int colsOfB) {
		//the cost of matrix multiplication is the number of steps, which is a*b*c with Aaxb and Bbxc (A is matrix of a rows and b columns)
		return rowsOfA*colsOfA*colsOfB; 
	}
	
	public static int greedy(ArrayList<Integer[][]> matrices) {

		int cost = 0;
		int steps[];
		
		while (matrices.size() != 1) {
			//This is size-1 because this method groups neighbors together (i.e. A0,A1,A2 --> costOf( A0xA1, A1xA2 )
			 //keeps track of the number of steps to multiply consecutive matrices
			steps = new int[matrices.size()-1];
			
			//logs the number of steps to multiply consequetive matrices
			for (int i = 0; i < matrices.size()-1; i++) {
				steps[i] = AxB(matrices.get(i), matrices.get(i+1))[0];
			}
			
			//finds the minimum cost of matrix multiplication that is current available
			int mindex = 0;
			
			for (int i = 1; i<steps.length; i++) {
				if (steps[i] < steps[mindex]) {
					mindex = i;
				}
			}
			
			Integer[][] temp1 = matrices.remove(mindex);
			Integer[][] temp2 = matrices.remove(mindex); //ArrayLists readjust after removing an index. So conceptually, this is removing mindex and mindex+1
			Integer[][] temp3 = new Integer[ AxB(temp1, temp2)[1] ][ AxB(temp1, temp2)[2] ];
			
			cost+=AxB(temp1, temp2)[0];
			matrices.add(temp3);
		}

		return cost;
	}
	
	public static void main(String args[]) {
		ArrayList<Integer[][]> matrices = new ArrayList<>();

		Integer[][] temp1 = new Integer[10][100],
				temp2 = new Integer[100][5],
				temp3 = new Integer[5][50],
				temp4 = new Integer[50][1];
		
		matrices.add(temp1);
		matrices.add(temp2);
		matrices.add(temp3);
		matrices.add(temp4);
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer[][]> forGreedyMethod = (ArrayList<Integer[][]>)matrices.clone();
		System.out.println("Greedy cost: " +  greedy( forGreedyMethod) );


		/*---------------------EVERYTHING BELOW IS RELATED TO THE DYNAMIC PROGRAMMING SOLUTION--------------------------*/
		
		int numOfMatrices = matrices.size();

		String[][] order = new String[numOfMatrices][numOfMatrices];
		
		//solution[i][j] the steps needed to multiply the segment i-j on the matrix chain
		int solution[][] = new int[numOfMatrices][numOfMatrices];
		Integer subProblemDem[][][] = new Integer[numOfMatrices][numOfMatrices][2];
		/*the approach I took uses dynamic programming to traverse down the solution matrix diagnally
		 * since the optimal way to multiply A1...An is the optimum way to multiply A1...A(n-1)xAn
		 * so A0, A1, A2 are best multiplied by figuring out if (A0xA1)xA2 is better than A0x(A1xA2)
		 */

		/*this method requires that you traverse down diagnals as you can't figure out the best way to multiply
		 *  A0...A2 without knowing the best way to multiply them is by A0x(A1xA2) or (A0xA1)xA2
		 */
		int currentDiagnal = 0;
		//i is the current row, j is the current column, k is the position on the diagnal being acted on
		//k is important for knowing when to switch diagnals
		int i = 0, j = 0, k =0;

		while ( currentDiagnal != numOfMatrices ) {
			//System.out.print(i + " " + j + " (" + currentDiagnal + "): ");

			if (i == j) { 
				solution[i][j] = 0; //the trivial diagnal... the best way to multiply 1 matrix : do nothing

				i++;
				j++;
				k++;
				//checks if the iteration is on the last element of the diagnal. 
				//The length of the diagnal shrinks by 1 everytime you change
				if (k == numOfMatrices-currentDiagnal) {
					i = 0; //starts back on the top row
					j = currentDiagnal+1; //adjusts the column
					currentDiagnal++;
					k = 0; //resets the index of the current element of the diagnal
				}
				continue;
			}
			//for consecitive matrices (i.e. 0 & 1, 1 & 2, etc.)

			else if (j == i + 1) { //the best way to multiply consecutive matrices

				solution[i][j] = AxB( matrices.get(i), matrices.get(j) )[0];
				//System.out.println(AxB( matrices.get(i), matrices.get(j) )[0]);

				subProblemDem[i][j][0] = AxB( matrices.get(i), matrices.get(j) )[1];
				subProblemDem[i][j][1] = AxB( matrices.get(i), matrices.get(j) )[2];
 
				order[i][j] = "(" + i + "x" + j + ")";
				i++;
				j++;
				k++;
				//checks if the iteration is on the last element of the diagnal. 
				//The length of the diagnal shrinks by 1 everytime you change
				if (k == numOfMatrices-currentDiagnal) {
					i = 0;
					j = currentDiagnal+1;
					currentDiagnal++;
					k = 0;
				}
				continue;
			}

			//the best way to multiply segments of the chain

			//these represent the two best candiates for the best ways of multiply the segment [i,j]
			//we use these to find the minimum cost and use that one
			Integer tempA[][] = new Integer[ subProblemDem[i][j-1][0] ][ subProblemDem[i][j-1][1] ];					
			Integer tempB[][] = new Integer[ subProblemDem[i+1][j][0] ][ subProblemDem[i+1][j][1] ];					

			//the returned array from method AxB tells you the number of steps need to multiply the two matrices ( in element [0])
			int a = solution[i][j-1] + AxB(tempA, matrices.get(j))[0]; //cost of  A(tempA)xAj
			int b = solution[i+1][j] + AxB(matrices.get(i), tempB )[0]; //cost of AixA(tempB) 
			
			solution[i][j] = a < b ? a : b; //stores the valuie of the minimum cost

			if (a < b) {
				//the returned array from method AxB tells you the number of rows for the matricx that is the product of A and B ( in element [1])
				subProblemDem[i][j][0] = AxB( tempA, matrices.get(j) )[1];
				//the returned array from method AxB tells you the number of rows for the matricx that is the product of A and B ( in element [1])
				subProblemDem[i][j][1] = AxB( tempA, matrices.get(j) )[2];
				order[i][j] = "(" + order[i][j-1] + ")x" + j;
			}
			else {
				//the returned array from method AxB tells you the number of rows for the matricx that is the product of A and B ( in element [1])
				subProblemDem[i][j][0] = AxB( matrices.get(i), tempB )[1];
				//the returned array from method AxB tells you the number of rows for the matricx that is the product of A and B ( in element [1])
				subProblemDem[i][j][1] = AxB( matrices.get(i), tempB )[2];
				order[i][j] = i + "x(" + order[i+1][j] + ")";
			}

			i++;
			j++;
			k++;
			//checks if the iteration is on the last element of the diagnal. 
			//The length of the diagnal shrinks by 1 everytime you change
			if (k == numOfMatrices-currentDiagnal) {
				i = 0;
				j = currentDiagnal+1;
				currentDiagnal++;
				k = 0;
			}
		}

		System.out.println("\nDP Solution Matrix (ans in top right): ");
		//displays the solution matrix
		for (int[] row : solution) {
			for (int col : row) {
				System.out.print(col + "\t");
			}
			System.out.println();
		}
		System.out.println();

		//the final solutions will be in the top right of the array because it represents the best way to multiply the subchain [0,n]
		System.out.println("Dynamic programming cost: " + solution[0][numOfMatrices-1]);
		System.out.println("\nOrder: " + order[0][numOfMatrices-1]);

	}
}
