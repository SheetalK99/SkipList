
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


//Purpose: Skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
	Entry<T> head, tail; // dummy nodes
	int size, maxLevel;
	Entry[] last;// used by find()
	static Random random;

	static class Entry<E extends Comparable<? super E>> {
		E element;
		Entry[] next;
		Entry prev;
		int span[]; // for indexing
		int skip;
		/**
		 * Constructor for the class Entry
		 * @param x : The value to be stored
		 * @param lev: The no of pointers the current node stores
		 */
		public Entry(E x, int lev) {
			element = x;
			next = new Entry[lev];
			span = new int[lev];
			prev = null;


		}

		public E getElement() {
			return element;
		}

	}

	// Constructor
	public SkipList() {
// head and tail are dummy nodes containing no value
		
		head = new Entry<T>(null, PossibleLevels); // -infinity
		tail = new Entry<T>(null, PossibleLevels);// +infinity
		size = 0;
		maxLevel = 1;
		last = new Entry[PossibleLevels];

		for (int i = 0; i < PossibleLevels; i++) {
			last[i] = tail;
			head.next[i] = tail; // head and tail are dummy nodes containing no value
		
		}

		random = new Random();

	}

	/**
	* helper method to search for x
	* @param x : The element to be searched for
	*/
	public void find(T x) {
		Entry<T> p = head;
		int skip;
		for (int i = maxLevel - 1; i >= 0; i--) {
			skip = 0;

			while (p.next[i] != null && p.next[i] != tail && p.next[i].element.compareTo(x) < 0) {
				p = p.next[i];
				skip = skip + p.span[i]; // add distance skipped

			}
			p.skip = skip; //total skipped distance at level i
			last[i] = p;  // last[i] stores the location where x can be added at ith level


		}

	}

	/**
	 * Method to Add x to list. If x already exists, it is rejected. Returns true if new node is added to list
	 * @param x : Element to be added to the List
	 * @return : Returns true if new element is added or returns false
	 */
	public boolean add(T x) {

		if (contains(x)) {
			return false; // reject duplicate
		} else {

			int lev = chooseLevel();// length of next[] for x's entry
			Entry<T> ent = new Entry<T>(x, lev);

			for (int i = 0; i <= lev - 1; i++) {
			// For span calculation
			
				if (i == 0) {
					ent.span[i] = 1;
				}
				else {
					for (int j = i; j < lev; j++) {
						ent.span[j] = ent.span[j] + last[i - 1].skip;
					}
					ent.span[i]++;
				}
				
				//link new entry to skip list at each level
				ent.next[i] = last[i].next[i];
				last[i].next[i] = ent;

			}

			ent.next[0].prev = ent;// prev pointer maintained at 0th level for backward traversal 
			ent.prev = last[0];
			
			
			// increase width for upper nodes
			for(int i=lev;i<maxLevel;i++) {
				if (last[i].next[i] != null) {
					last[i].next[i].span[i]++;
				}
			
			}
			//Adjust width of next nodes
			for (int i = 1; i < lev; i++) {
				Entry p = ent.next[i];
				if (p != null) {
					p.span[i] = p.span[i] - ent.span[i] - 1;
				}
			}

		}

		size++;

		return true;
	}

	/**
	  * @return : Returns a Random number between 1 & maxLevel + 1
	 */
	private int chooseLevel() {
		// random level generation
		int lev = 1 + Integer.numberOfTrailingZeros(random.nextInt());
		// lev=Math.min(lev, maxLevel+1);
		if (lev > maxLevel) {// set maxlevel to new level if its greater
			maxLevel = lev;

		}

		return lev;
	}

	/**
	 * Method to find smallest element that is greater or equal to x
	 * @param x : Element
	 * @return : returns smallest element that is greater or equal to x
	 */
	public T ceiling(T x) {

	// if list contains x then ceiling is x	
		if (contains(x)) {
			return x;
		} else {
		// if x not in list then ceiling is smallest element greater than x(next entry)
			
			if (last[0].next[0] != null && last[0].next[0].element.compareTo(x) > 0) {
				return (T) last[0].next[0].element;
			} else {
			
			// if there is no entry >=x
				
				return null;

			}
		}

	}

	/**
	 * Checks the list for element x
	 * @param x : Element to be searched
	 * @return true, if it element is present in the list, else false
	 */
	public boolean contains(T x) {

		find(x);
		if (last[0].next[0].element == null) {
			return false;
		} else {

			return last[0].next[0].element.compareTo(x) == 0;
		}

	}
	/**
	 *Returns first element of list 
	 *@return element
	 */
	public T first() {
		if (size > 0) {
			return (T) head.next[0].element;
		} else {
			return null;
		}
	}

	/**
	 * Method to find largest element that is less than or equal to x
	 * @param x - element
	 * @return returns largest element that is less than or equal to x
	 */
	public T floor(T x) {
//if list contains x then floor of x is x
		if (contains(x)) {
			return x;
		} else {
		// if not then greatest element less than x(current entry of last)
			if (last[0].element != null) {
				return (T) last[0].element;
			} else {
			// if there is no entry <=x
				return null;
			}
		}

	}

	/**
	 * Returns element at index n of list. 
	 * @param n : Index of the element to be returned, index starts at 0
	 * @return : Returns the element at index n
	 */
	public T get(int n) {

		return getLinear(n);

	}
	// O(n) algorithm for get(nv)
	public T getLinear(int n) {
		if (n < 0 || n > size - 1) {
			return (T) new NoSuchElementException();
		} else {
		// traverse through list at lev 0 till index n
			Entry<T> p = head;

			for (int i = 0; i <= n; i++) {
				p = p.next[0];
			}
			return (T) p.element;
		}

	}

	// O(log n) expected time for get(n)
	public T getLog(int n) {
		Entry<T> p = head;
		// n = n - 1;/// ignore head as step
		for (int i = maxLevel - 1; i >= 0; i--) {
			while (p.next[i] != null && n -p.next[i].span[i]>=0 && p.next[i] != tail) {
				n = n - p.next[i].span[i];
				p = p.next[i];
			}
		}
		return p.element;
	}

	/**
	 *  @return : True if the list is empty else false
	 */
	public boolean isEmpty() {

		return size == 0;
	}

	/**
	 * Iterates through the elements of list in sorted order
	 */
	public Iterator<T> iterator() {

		return new SkipListIterator();
	}
	/*
	 * Custom iterator for SkipList
	 */
	private class SkipListIterator implements Iterator<T> {
		Entry<T> cursor;

		SkipListIterator() {
			cursor = head;
		}

		public boolean hasNext() {
			return cursor.next[0] != tail;
		}

		public T next() {
			T el = (T) cursor.next[0].element;
			cursor = cursor.next[0];
			return el;
		}
		
		 public void remove() {
		        SkipList.this.remove(cursor.element);
		    }

	}

	/**
	 * @return last element of list
	 */
	public T last() {

		if (size > 0) {
			return (T) tail.prev.element;
		} else {
			return null;
		}

	}

	/**
	 * Reorders the elements of the list into a perfect skip list
	 * The elements of the list are taken into array with head at index 0 .
	 * The elements in the odd index have only one pointer in their array. 
	 * The no of pointers in even indexed nodes is calculated and the SkipList is ordered 
	 */
	public void rebuild() {
			Entry[] entryArray = new Entry[this.size() + 2];			
			Entry<T> e = head;
			int i = 1;
			while(e != null && e != tail && i < this.size()+1){
				entryArray[i++] = e.next[0];
				e = e.next[0];
			}
			entryArray[0] = head;
			entryArray[this.size()+1] = tail;						
			i = 0;		
			while(i < this.size() + 1){
				if(i % 2 != 0){					//when node is in odd index, it has only one pointer 					  
					e = entryArray[i];
					e.next = new Entry[1];
					e.span = new int[1];
					e.next[0] = entryArray[i + 1];
					e.span[0] = 1;
				} else {								//	If node is in even index, the no of pointers it holds is calculated by repeatedly dividing it by 2 perfectly. # of divisions gives the size of the next array		
					int i2 = i;										
					int c = 1;																
					if(i2 == 0){								//For head of the SkipList							
						int limit = (int) (Math.log(this.size()) / Math.log(2)) + 1;	//Maximum level needed
						maxLevel = limit;												//Changing the maxLevel
						c = limit;
					}
					while(i2 != 0 && i2 % 2 == 0){					//size of the next array based on its position in the array
						i2 = i2 / 2;
						c++;
					}
					e = entryArray[i];
					e.next = new Entry[c];
					e.span = new int[c];
					for(int j = 0; j < c; j++){						//Pointing to the corresponding nodes of the new next array
						int p = (int)Math.pow(2, j);
						int index = (int) (i + p);					//Calculating the index to the new pointer			
						if(index > this.size()){					//If the index exceeds the List size, it points to the tail			
							e.next[j] = tail;
							e.span[j] = this.size() + 1 - i;
						} else {
							e.next[j] = entryArray[index];
							e.span[j] = p;
						}
					}
				}
				i++;
			}		
			for(int j = maxLevel; j < head.next.length; j++){			//the remaining pointers of head are set to tail				
				head.next[j] = tail;
			}

		}

	

	/**
	 * Removes x from list and returns it. Returns null if x is not in list
	 * @param x to be removed
	 * @return x or returns null if x is not in list
	 */
	public T remove(T x) {
		if (!contains(x)) {
			return null;
		}

		else {
// create entry node of element to be removed
			
			Entry<T> ent = last[0].next[0];
			// for span calculation

			
			for (int i = ent.next.length ; i < maxLevel ; i++) {
			
			// descrese width of next nodes
				if (last[i].next[i] != null) {
					last[i].next[i].span[i]--;
				}
			}
			
			for (int i = 0; i <= ent.next.length - 1; i++) {
			
				// Decrease width of nodes above
				if(ent.next[i]!=null) {
				
				ent.next[i].span[i] = ent.next[i].span[i] + ent.span[i] - 1;
				}
			
				// bypass ent. set the next of prev entry to x's next at all levels
				last[i].next[i] = ent.next[i];

			}

			size--;
			return ent.element;
		}

	}

	/**
	 * @return # of elements in the list
	 */
	public int size() {
		return size;
	}
}
