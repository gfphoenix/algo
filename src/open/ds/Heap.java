package open.ds;

import java.util.Collection;
import java.util.Iterator;

// min heap
public class Heap<T extends Comparable<T>> {
	T []array;
	int size;
	@SuppressWarnings("unchecked")
	public Heap(int cap){
		array = (T[]) new Comparable[cap];
		size = 0;
	}
	public Heap(){
		this(0);
	}
	public Heap(T []data){
		this.array = data;
		this.size = data.length;
	}
	public Heap(T []data, int size){
		this.array = data;
		this.size = size;
	}
	
	void build(){
		for(int i=size/2-1; i>=0; i--)
			filterDown_(array, size, i);
	}
	public static <T extends Comparable<T>> void sort(T []data){
		int n = data.length;
		T x;
		for(int i=n/2-1; i>=0; i--)
			filterDown_(data, n, i);
		for(int i=0, j=n-1; i<n; i++){
			x = data[0];
			data[0] = data[j];
			data[j] = x;
			filterDown_(data, --j, 0);
		}
		// reverse
		for(int i=0, j=n-1; i<j; i++, j--){
			x = data[i];
			data[i] = data[j];
			data[j] = x;
		}
	}
	// parent : i => i>0 && (i-1)/2;
	// left : 2*i+1
	// right : 2*i+2
	// i is valid >=0
	// assume that array is not null && 0<=n<=array.length && 0<=i<=n-1
	public static <T extends Comparable<T>> void filterDown_(T []array, int n, int i){
//		int n = size;
		int k = n/2;
		
		// i<k make sure `i'-th node has at least left child
		while(i<k){
			int child = 2*i+1;
			int r = child+1;
			T self = array[i];
			T v = array[child];
			// make child & v to reference the minimal child
			if(r<n && array[r].compareTo(v)<0){
				child = r;
				v = array[r];
			}
			if(self.compareTo(v)<=0)
				// stop
				break;
			array[child] = self;
			array[i] = v;
			i = child;
		}
	}
	public static <T extends Comparable<T>> void filterDown_2(T []array, int n, int i){
//		int n = size;
		int k = n/2;
		if(n<=0) return;
		T self = array[i];
		// i<k make sure `i'-th node has at least left child
		while(i<k){
			int child = 2*i+1;
			int r = child+1;
			
			T v = array[child];
			// make child & v to reference the minimal child
			if(r<n && array[r].compareTo(v)<0){
				child = r;
				v = array[r];
			}
			if(self.compareTo(v)<=0)
				// stop
				break;
			array[child] = self;
			i = child;
		}
		array[i] = self;
	}
	// move value at i up
	public static <T extends Comparable<T>> void filterUp_(T []array, int i){
		while(i>0){
			int parent = (i-1)>>1;
			T pv = array[parent];
			T self = array[i];
			if(pv.compareTo(self)<=0)
				break;
			array[i] = pv;
			array[parent] = self;
			i = parent;
		}
	}
	private void ensure_size(int d) {
		
	}
	public int size(){
		return size;
	}
	public void enqueue(T x){
		ensure_size(1);
		int n = size++;
		array[n] = x;
		filterUp_(array, n);
	}
	public void enqueueAll(Collection<T> c){
		int i = size;
		int n = c.size();
		ensure_size(n);
		Iterator<T> it = c.iterator();
		
		while(it.hasNext())
			array[i++] = it.next();
		
		size = i;
		while(n-- >0)
			filterUp_(array, --i);
		
	}
	public T peek(){
		return array[0];
	}
	public T dequeue(){
		T x = array[0];
		array[0] = array[--size];
		filterDown_(array, size, 0);
		return x;
	}
	
	
	static int nochild(int n){
		return (n)/2;
	}
	
	public static void main(String []args) {
//		int []d = GenData.genInt(33, a, b)
	}
}
