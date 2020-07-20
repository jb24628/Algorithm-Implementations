package algo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
/**
 * 
 * @author Jireh Bethely
 * My implementation of Boruvka's algorithm using an adjacency matrix to represent a graph
 * 
 */
public class Boruvka {

	private static final int minInt = Integer.MIN_VALUE;
	
	//adds an edge to the adj matrix
	public static int[][] addEdge(int v1, int v2, int weight, int[][] adj) {
		int[][] toReturn = adj;
		toReturn[v1][v2] = weight;
		toReturn[v2][v1] = weight; //we must add it both ways to be consistent since it's an undirected graph

		return toReturn;
	}

	//returns the vertex with the minimum weight from a single vertex
	public static int mindex(int[] row) {
		int mindex = 0;

		while( mindex < row.length && row[mindex] < 0 ) { //makes sure that the index we start on is an actual edge
			mindex++;
		}

		for (int i = mindex+1; i < row.length; i++) {
			if (row[i] > minInt && row[mindex] > row[i]) {
				mindex = i;
			}
		}

		return mindex;
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
	
	public static void print(String s) {
		System.out.println(s);
	}
	
	//Gets degree of vertex
	public int getDegree(int vertex, int[][] adj) {
		int degree = 0;
		
		for (int i = 0; i < adj.length; i++) {
			if (adj[i][vertex] == minInt) {
				degree++;
			}
		}
		
		return degree;
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
		int[] vertices = {0,1,2,3,4,5,6,7,8,9,10,11};

		//important initialization step
		int[][] adj = getBlankArr(vertices.length); //original adjacency matrix for graph. It becomes the matrix of unused edges
		int[][] mst = getBlankArr(vertices.length); //will become the adjacency matrix for the MST
		
		/* ----------------------------------------- Initializing Edges ------------------------------------------------- */
		
		/*
		//Simpler graph
		//Syntax: addEdge(vertex1, vertex2, weight, originalArr)
		adj = addEdge(0,4,1,adj);
		adj = addEdge(0,1,5,adj);
		adj = addEdge(0,3,9,adj);

		adj = addEdge(1,4,2,adj);
		adj = addEdge(1,2,3,adj);
		adj = addEdge(1,3,1,adj);

		adj = addEdge(2,4,7,adj);
		adj = addEdge(2,3,14,adj);
		*/
		
		// /*
		//more complex graph
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
		// */
		print("Original Graph:");
		printArr(adj); //original array

		/* ------------------------------------------- Forest initialization ------------------------------------------- */
		
		Stack<Integer[]> edges2Rmv = new Stack<Integer[]>();
		
		for (int vertex = 0; vertex < vertices.length; vertex++) {
			int minCostEdge = mindex( adj[vertex] ); //mindex finds the vertex/index with the minimum weight

			if (minCostEdge >= vertices.length) { continue; };
			
			mst = addEdge(vertex, minCostEdge, adj[vertex][minCostEdge], mst);

			Integer[] edge = {vertex, minCostEdge};
			edges2Rmv.push(edge); //we don't want to remove the edges from the list imediately
		}

		while(!edges2Rmv.isEmpty()) { //removes the edges from the forest initialization step
			Integer[] edge = edges2Rmv.pop(); //edge[0] and edge[1] are the vertexes of the edge
			adj[edge[0]][edge[1]] = minInt;
			adj[edge[1]][edge[0]] = minInt;
		}
		
		/* ----------------------------------------- Main Part Of Algorithm ------------------------------------------- */
		
		while( !isOneTree(mst) ) { //runs until the forest becomes a tree
			ArrayList<ArrayList<Integer>> forest = getForest(mst);
			int size = forest.size();
			
			for (int i = 0; i < size; i++) {
				forest = getForest(mst); //updated each iteration to insure we don't double connect a tree twice in the same run
				
				Integer[] edge = getMinEdge(forest.get(i), adj);
				
				if (edge[0] > adj.length || edge[1] > adj.length) {	continue; } //edge case
				
				//edge[0] and edge[1] are the vertexes of the edge
				mst = addEdge(edge[0], edge[1], adj[edge[0]][edge[1]], mst); //adj[edge[0]][edge[1]] is the weight of the edge
				adj[edge[0]][edge[1]] = minInt; //removes the edge from the original adj matrix
				adj[edge[1]][edge[0]] = minInt; //removes the edge from the original adj matrix
				
				size--; //makes sure we don't iterate too many times
			}
			
			if (forest.size() == size) { //if the size of the forest doesn't change and there is still 2+ trees, we know that there aren't edges that connect any of the 2+ trees
				print("\nNo MST exists");
				System.exit(1);
			}
		}

		//printArr(adj); //Now that the MST has been found, doing this tells you the unused edges
		
		//tip: if you are drawing solution out to double check it, only look at the coordinates above OR below diagonal line of x's since this is a
		//graph without directed edges
		print("\nMST:");
		printArr(mst);
		print("\nMST Tota Weight: " + getTotalWeight(mst));
	}
}