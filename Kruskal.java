package algo;
import java.util.ArrayList;
import java.util.LinkedList;

public class Kruskal {

	/**
	 * 
	 * @author Jireh Bethely
	 * My implementation of Kruskal's algorithm using an adjacency matrix to represent a graph
	 * 
	 */
	private static final int minInt = Integer.MIN_VALUE;

	//adds an edge to the adj matrix
	public static int[][] addEdge(int v1, int v2, int weight, int[][] adj) {
		int[][] toReturn = adj;
		toReturn[v1][v2] = weight;
		toReturn[v2][v1] = weight; //we must add it both ways to be consistent since it's an undirected graph

		return toReturn;
	}

	//returns the vertex with the minimum weight from the entire matrix
	public static int[] mindex(int[][] adj) {
		int mindexX = 0;
		int mindexY = 0;

		while( mindexX < adj.length && mindexY < adj[0].length  && adj[mindexX][mindexY] == minInt) { //makes sure that the index we start on is an actual edge
			mindexX++;

			if (mindexX == adj.length) {
				mindexY++;
				mindexX = 0;
			}
		}

		for (int i = mindexX; i < adj.length; i++) {

			for (int j = mindexY; j < adj[0].length; j++) {
				if (adj[i][j] > minInt && adj[mindexX][mindexY] > adj[i][j]) {
					mindexX = i;
					mindexY = j;
				}
			}
		}

		return new int[] {mindexX, mindexY};
	}

	public static int[][] getBlankArr(int len) {
		int[][] toReturn = new int[len][len];

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				toReturn[i][j] = minInt; // minInt represents no connection. [Integer.MIN_VALUE+1,Integer.MAX_VALUE) represents weight
			}
		}

		return toReturn;
	}

	//prints the graphs is an somewhat easy to read fashion
	public static void printArr(int[][] adj) {
		System.out.print("   0");

		for (int i = 1; i < adj.length;i++) {
			System.out.print("\t"+i);
		}

		System.out.print("\n----");
		for (int i = 1; i < adj.length; i++) {
			System.out.print("--------");
		}
		print("");

		for (int i = 0; i < adj.length; i++) {
			System.out.print(i + "| ");
			for (int j = 0; j < adj[0].length; j++) {
				if (adj[i][j] == minInt && i == j) {
					System.out.print("x\t");
					continue;
				}
				if (adj[i][j] == minInt) {
					System.out.print(".\t"); // minInt represents no connect.[Integer.MIN_VALUE+1,Integer.MAX_VALUE) represents weight
					continue;
				}

				System.out.print(adj[i][j] + "\t"); // minInt represents no connect. [Integer.MIN_VALUE+1,Integer.MAX_VALUE) represents weight
			}
			System.out.println();
		}
	}

	//Determines if an array of booleans contains a false
	public static boolean areAllTrue(boolean[] arr) {
		for (boolean b : arr) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	//determines where a the given adj matrix is a tree or not
	public static boolean isOneTree(int[][] adj) {
		LinkedList<Integer> queue = new LinkedList<Integer>();

		boolean visited[] = new boolean[adj.length]; //keep track of what vertices have been visited
		queue.offer(0);

		while (!queue.isEmpty()) {
			int row = queue.poll();
			visited[row] = true;

			for (int i = 0; i<adj.length; i++) {
				if (adj[row][i] > minInt && !visited[i]) {
					queue.offer(i);
				}
			}
		}
		return areAllTrue(visited);
	}

	//Gets the minimum weighted edge that goes to another tree
	public static Integer[] getMinEdge(ArrayList<Integer> tree, int[][] adj) {

		boolean inThisTree[] = new boolean[adj.length]; //keeps track of which numbers are in this tree

		for (Integer vertex : tree) { //important so that we don't use an edge that connects a vertex in the tree to another vertex in the tree
			inThisTree[vertex] = true;
		}

		ArrayList<Integer[]> allEdges = new ArrayList<Integer[]>();

		for (Integer vertex1 : tree) {
			for (int vertex2 = 0; vertex2<adj.length; vertex2++) { //finds all vertexes connected to this vertex
				if (adj[vertex1][vertex2] > minInt && !inThisTree[vertex2]) { //makes sure we don't add an edge already in this tree
					Integer arr[] = {vertex1, vertex2};
					allEdges.add(arr);
				}
			}
		}

		if (allEdges.size() == 0) { //edge case
			return new Integer[] {adj.length+1, adj.length+1}; 
		}

		//finds the min edge
		Integer[] minEdge = allEdges.get(0);

		for (int i = 1; i < allEdges.size(); i++) {
			int v1 = minEdge[0], v2 = minEdge[1];

			Integer temp[] = allEdges.get(i);
			int v3 = temp[0], v4 = temp[1];
			if (adj[v1][v2] > adj[v3][v4]) {
				minEdge = temp;
			}
		}

		return minEdge;
	}

	//Gets the max edge from a vertex
	public static int[] getMaxEdge(int vertex, int[][] adj) {

		int max = 0;

		for (int i = 1; i < adj[vertex].length; i++) {
			if (adj[vertex][i] > max) {
				max = i;
			}
		}

		return new int[] {vertex,max};
	}

	//prints array
	public static void print(String s) {
		System.out.println(s);
	}

	//gets a forest of trees from an adjacency matrix
	public static ArrayList<ArrayList<Integer>> getForest(int[][] adj) {
		//each tree is represented as an ArrayList of Integer
		ArrayList<ArrayList<Integer>> forest = new ArrayList<ArrayList<Integer>>(adj.length);

		boolean visited[] = new boolean[adj.length];

		int firstFalse = 0; //to figue out where the first vertex that has not been visited is

		while(!areAllTrue(visited)) { //iterates until all vertexes have been visited
			ArrayList<Integer> tree = new ArrayList<Integer>();

			while (firstFalse < adj.length && visited[firstFalse]) { //finds the first unvisited vertex
				firstFalse++;
			}

			LinkedList<Integer> queue = new LinkedList<Integer>();

			queue.offer(firstFalse);
			tree.add(firstFalse);

			while (!queue.isEmpty()) { //finds all the vertices in the same tree
				int vertex = queue.poll();
				visited[vertex] = true;

				for (int i = 0; i<adj.length; i++) { //finds all the vertices directly connected to this vertex
					if (adj[vertex][i] > minInt && !visited[i]) { //makes sure we have not visited before so no infinite loop
						queue.offer(i);
						tree.add(i);
					}
				}
			}

			forest.add(tree); //adds tree to forest
		}

		return forest;
	}

	//gets the degree of a vertex from an adj matrix
	public static int getDegree(int vertex, int[][] adj) {
		int degree = 0;

		for (int i = 0; i < adj.length; i++) {
			if (adj[i][vertex] != minInt) {
				degree++;
			}
		}

		return degree;
	}

	//gets the degrees of all the vertexes in a graph represented by an adj matrix
	public static int[] vertexDegrees(int[][] adj) {

		int degrees[] = new int[adj.length];

		for (int i = 0; i < adj.length; i++) {
			degrees[i] = getDegree(i, adj);
		}

		return degrees;	
	}

	//Determines if all degrees are Less than or eqaual to the max
	public static boolean areAllDegreesLTMax(int max, int[][] adj) { 
		int vertexDegrees[] = vertexDegrees(adj);

		for (int i : vertexDegrees) {
			if (i > max) {
				return false;
			}
		}

		return true;
	}

	/*------------------------------------------------------ Original Kruskal's Algorithim ------------------------------------------ */
	public static int[][][] buildMST(int[][] adj, int[][] mst) {
		while (!isOneTree(mst)) {

			ArrayList<ArrayList<Integer>> before = getForest(mst);

			int[] minEdge = mindex(adj); //finds the minimum edge at each iteration
			//Reminder: addEdge is (v1, v2, weight, adjMatrix)
			mst = addEdge(minEdge[0], minEdge[1], adj[minEdge[0]][minEdge[1]], mst); //adds the minimum edge to the graph that will become the MST

			//removes the minimum edge from the original graph
			adj[minEdge[0]][minEdge[1]] = minInt;
			adj[minEdge[1]][minEdge[0]] = minInt;

			ArrayList<ArrayList<Integer>> after = getForest(mst);

			if (before.size() == after.size()) { //if the edge we chose creates a cycle we remove it
				mst[minEdge[0]][minEdge[1]] = minInt;
				mst[minEdge[1]][minEdge[0]] = minInt;
			}

		}

		return new int[][][] {mst,adj};
	}

	public static int getTotalWeight(int[][] adj) {
		int weight = 0;

		for (int i = 0; i < adj.length; i++) {
			for (int j = i; j < adj[i].length; j++) {
				if (adj[i][j] != minInt) {
					weight += adj[i][j];
				}
			}
		}

		return weight;
	}

	public static void main(String args[]) {

			//IMPORTANT! vertices must [0,n-1] without skipping any numbers
			int[] vertices = {0,1,2,3,4,5,6,7,8};//,9,10,11};

			//important initialization step
			int[][] adj = getBlankArr(vertices.length); //original adjacency matrix for graph. It becomes the matrix of unused edges
			//int[][] adj = Prim.generateRandomGraph(200);
			//int[] vertices = new int[adj.length];

			int[][] mst = getBlankArr(vertices.length); //will become the adjacency matrix for the MST
			int[][] removedFromMST = getBlankArr(vertices.length); //will make more sense later

			int maxDegree = 4; // <------------------------ Change Max Degree Here!

			/* ----------------------------------------- Initializing Edges ------------------------------------------------- */

			/*
			//Simpler graph - not a good graph to test degree limit with
			//Syntax: addEdge(vertex1, vertex2, weight, originalArr)
			adj = addEdge(0,4,1,adj);
			adj = addEdge(0,1,5,adj);
			adj = addEdge(0,3,9,adj);

			adj = addEdge(1,4,2,adj);
			adj = addEdge(1,2,3,adj);
			adj = addEdge(1,3,1,adj);

			adj = addEdge(2,4,7,adj);
			adj = addEdge(2,3,14,adj);
		 //*/

			/*
		//more complex graph - degrees still aren't that high
		//Syntax: addEdge(vertex1, vertex2, weight, originalArr)
		adj = addEdge(0,1,6,adj);
		adj = addEdge(0,4,4,adj);
		adj = addEdge(0,5,9,adj);

		adj = addEdge(1,2,8,adj);

		adj = addEdge(2,6,7,adj);

		adj = addEdge(3,6,3,adj);
		adj = addEdge(3,7,9,adj);

		adj = addEdge(4,9,3,adj);
		adj = addEdge(4,8,7,adj); //since the edge 4-8 is the only edge that connects to edge 8, you can comment this out to ensure that this program tells you if an MST exists

		adj = addEdge(5,6,5,adj);
		adj = addEdge(5,10,2,adj);
		adj = addEdge(5,9,8,adj);

		adj = addEdge(6,7,5,adj);
		adj = addEdge(6,10,9,adj);

		adj = addEdge(7,11,4,adj);

		adj = addEdge(9,10,4,adj);
		//*/
			//"spin wheel graph" can visuaize as a circle of vertexes 1-8 with 0 in the middle.
			adj = addEdge(0,1,1, adj);
			adj = addEdge(0,2,2, adj);
			adj = addEdge(0,3,3, adj);
			adj = addEdge(0,4,4, adj);
			adj = addEdge(0,5,5, adj);
			adj = addEdge(0,6,6, adj);
			adj = addEdge(0,7,7, adj);
			adj = addEdge(0,8,8, adj);

			adj = addEdge(1,2,9, adj);

			adj = addEdge(2,3,10, adj);

			adj = addEdge(3,4,11, adj);

			adj = addEdge(4,5,12, adj);

			adj = addEdge(5,6,13, adj);

			adj = addEdge(6,7,14, adj);

			adj = addEdge(7,8,15, adj);

			adj = addEdge(8,1,16, adj);

			print("Original Graph:");
			printArr(adj); //original array

			//Starts off by finding the MST with no regard to degrees
			int[][][] temp = buildMST(adj, mst);

			mst = temp[0];
			adj = temp[1];

			//Just displays the original MST and some stats about it
			int[] vertexDegrees = vertexDegrees(mst);


			print("\nOriginal MST:");
			printArr(mst);

			print("\nOriginal MST Degrees:");

			String s = "";
			for (int d : vertexDegrees) {
				s += d + " ";
			}

			print("Original MST Total Weight: " + getTotalWeight(mst));

			print(s + "\n-------------------------------------------------------------------------------------------------");



			/*------------------------------------ Corrects For Degrees Over Max ------------------------------------------*/
			//Works by finding the vertexes with degrees over the limit, 
			//removing the heaviest edges from them, then finding the next best edges for the tree
			while (!areAllDegreesLTMax(maxDegree,mst)) {

				int degrees[] = vertexDegrees(mst);

				int start = 0;

				while (degrees[start] <= maxDegree) { //finds the first vertex that goes over the max
					start++;
				}

				for (int i = 0; i < degrees[start] - maxDegree; i++) { //removes the heaviest edges until it fits the max degree constrain
					int maxEdge[] = getMaxEdge(start, mst);

					removedFromMST = addEdge(maxEdge[0], maxEdge[1], mst[maxEdge[0]][maxEdge[1]],removedFromMST); //simply just to keep track of unused edges

					//removes the edges from the MST
					mst[maxEdge[0]][maxEdge[1]] = minInt;
					mst[maxEdge[1]][maxEdge[0]] = minInt;
				}

				//rebuilds the MST
				temp = buildMST(adj, mst); //buid MST returns both the MST and the unused edges

				mst = temp[0];
				adj = temp[1];

			}

			//printArr(adj); //Now that the MST has been found, doing this tells you the unused edges from the original MST

			//tip, if you are drawing solution out to double check it, only look at the coordinates above OR below diagonal line of x's since this is a
			//graph without directed edges
			print("New MST:");
			printArr(mst);

			print("\nNew MST Degrees:");
			vertexDegrees = vertexDegrees(mst);
			s = "";
			for (int d : vertexDegrees) {
				s += d + " ";
			}
			print(s);

			print("\nNew MST Total Weight: " + getTotalWeight(mst));
	}
}