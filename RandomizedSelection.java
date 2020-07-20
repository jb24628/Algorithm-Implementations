package algo;
import java.util.Arrays;
import java.util.LinkedList;

public class RandomizedSelection {

	public static int findIthSmallest(LinkedList<Integer> l, int ith) { //indexing starts at 1 (1th = smallest, 2th = 2nd smallest ...
		int size = l.size();
		int pivotPos = (int) (Math.random()*size);
		int pivot = (int)l.get(pivotPos);

		LinkedList<Integer> smaller = new LinkedList<Integer>(), 
				larger = new LinkedList<Integer>();

		for (int i = 0; i <= size; i++) {
			if (i != pivotPos) {
				int temp = (int)l.poll();

				if ( temp <= pivot ) {
					smaller.offer(temp);
				}
				else {
					larger.offer(temp);
				}
			}
		}

		int sSize = smaller.size();

		if (ith == sSize) {
			return pivot;
		}
		else if ( ith < sSize ) {
			return findIthSmallest(smaller, ith);
		}
		return findIthSmallest(larger,ith-sSize);
	}

	public static void main(String[] args) {

		int[] list = {8,4,6,5,2,3,7,0};
		LinkedList<Integer> ll = new LinkedList<Integer>();
		//I chose to use a linkedlist to minimize adding O(n) time due for insertions/deletions
		for (int i : list) { ll.add(i); }
		
		Arrays.parallelSort(list);
		for (int i : list) { System.out.print(i+ "\t");}
		System.out.println();
		
		//For some reason, I had to clone the objects or else ll gets altered and the 2nd and 3rd calls to findIthSmallest don't work
		System.out.println(/*"Smallest element: " + */findIthSmallest( (LinkedList<Integer>)ll.clone(),1) );
		System.out.println(/*"Median element: " + */findIthSmallest( (LinkedList<Integer>)ll.clone(),4) );
		System.out.println(/*"Largest element: " + */findIthSmallest( (LinkedList<Integer>)ll.clone(),8) );
		
		//System.out.println("Median element: " + findIthSmallest(ll,4) );
		//System.out.println("Largest element: " + findIthSmallest(ll,8) );
		
	}
}