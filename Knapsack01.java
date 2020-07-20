package algo;
import java.util.ArrayList;

public class Knapsack01 {

	public static void main(String args[]) {
		//Note: pairs must be {benefit, weight}
		int all[][] = { {3,2}, {4,3}, {5,4}, {6,5} };
		int maxWeight = 5;

		int[][] solution = new int[all.length][maxWeight+1]; //Obviously, indexing starts at 0, so it must go to weight+1 to be complete

		for (int i = 0; i < all.length; i++) {
			for (int j = 0; j <= maxWeight; j++) { 
				if ( all[i][1] > j) { //sees if the weight of the item is too big
					if (i == 0) { //since I did not use a row of 0s, this is necessary for the first row
						solution[i][j] = 0;
						continue;
					}
					//basically the "else" for the if statement above
					solution[i][j] = solution[i-1][j]; //uses dynamic programing to find the optimum benefit from the previous row
					continue;
				}

				if (i == 0) { //since we know the temp max weight is greater than the items weight and there is no previous row, we just add it
					solution[i][j] = all[i][0]; 
					continue;
				}
				else if ( all[i][1] > j) { //item is to heavy so we don't even consider adding it
					solution[i][j] = solution[i-1][j];
					continue;
				}
				//gets the benefit from adding by adding its weight along with the already calculated max benefit for the subproblem of a knapsack with max size of the current weight minus this element's weight
				int benefitWith = all[i][0] + solution[i-1][j-all[i][1]];
				int benefitWithout = solution[i-1][j];

				solution[i][j] = benefitWith > benefitWithout ? benefitWith : benefitWithout;
			}
		}
		
		for (int r[] : solution) { //prints out the solution matrix
			for (int c : r) {
				System.out.print(c + "  ");
			}
			System.out.println();
		}
		
		ArrayList<Integer> items = new ArrayList<>(all.length);
		
		int i = solution.length-1, j = solution[0].length-1;
		
		while (i > -1) { //finds what weights were added. Starts from the last cell and works its way back
			if (i == 0) {
				if (solution[i][j] != 0) { //checks if the element in the top row was added
					items.add(all[i][1]);
				}
				break;
			}
			if (solution[i-1][j] == solution[i][j]) { //checks if the cell got its number from the cell above it because in this case, we did not the weight from that row
				i--; //increments so it will go to the previous row at the same colymn
				continue;
			}
			
			items.add(all[i][1]); //adds the weight to the list of included weights
			j-=all[i][1]; //The weight was included so we go to the previous row and go to the left (currentColumn - theBenefitOfThisItem) columns
			i--;
			
		}
		
		System.out.println("\nMax benefit: " + solution[solution.length-1][solution[0].length-1]);
		for (Integer temp : items) {
			System.out.print(temp + "\t");
		}
		
	}
}
