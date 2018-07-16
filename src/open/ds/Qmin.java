package open.ds;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Qmin<T extends Comparable<T>> {
	private static class Qinfo<T extends Comparable<T>> {
		int count;
		T value;

		private Qinfo(T x) {
			count = 1;
			value = x;
		}
	}

	Queue<T> q = new LinkedList<T>();
	ArrayList<Qinfo<T>> q_min = new ArrayList<>();

	public boolean empty() {
		return q.isEmpty();
	}

	public void enqueue(T x) {
		int n;
		q.add(x);
		Qinfo<T> tmp;

		// min
		// can do binary search, since q_min is ordered already
		while ((n = q_min.size()) > 0
				&& q_min.get(n - 1).value.compareTo(x) > 0)
			q_min.remove(n - 1);

		if ((n = q_min.size()) > 0
				&& (tmp = q_min.get(n - 1)).value.compareTo(x) == 0) {
			tmp.count++;
		} else {
			q_min.add(new Qinfo<T>(x));
		}
	}

	public T dequeue() {
		T x = q.poll();
		Qinfo<T> mm;

		mm = q_min.get(0);
		if (mm.value.compareTo(x) == 0) {
			if (--mm.count == 0)
				q_min.remove(0);
		}
		return x;
	}

	public T peek() {
		return q.peek();
	}

	public T min() {
		return q_min.get(0).value;
	}


static class MyList<T> {
	T []data;
	int size_;
	public MyList(){
		this(4);
	}
	public MyList(int cap){
		size_ = 0;
		T[] ts = (T[]) new Object[cap+2];
		data = ts;
	}
	T get(int i){
		return data[i];
	}
	void set(int i, T x){
		check_index(i, "set");
		data[i] = x;
	}
	int size(){
		return size_;
	}
	void resize(int n){
		if(n<0) return;
		if(n<=size_){
			size_ = n;
			return ;
		}
		grow(n);
		size_ = n;
	}
	void add(T x){
		int n = size_;
		grow(n+1);
		data[n] = x;
		size_++;
	}
	void check_index(int i, String msg){
		
	}
	void grow(int n){
		final int m = data.length;
		if(n <= m) return;
		n = Math.max(n+2, m*2);
		T []tmp = (T[]) new Object[n];
		System.arraycopy(data, 0, tmp, 0, m);
		data = tmp;
	}
}

// need a lot of work to do
// use binary search
	static class Dq<T> {
		ArrayList<T> q1 = new ArrayList<>();
		ArrayList<T> q2 = new ArrayList<>();
		int i0;

		public Dq() {
			i0 = 0;
		}

		public int size() {
			return q1.size() - i0 + q2.size();
		}
		public void trimSize(int n){
			final int m = size();
			final int n1 = q1.size(), n2 = q2.size();
			if(n<0 ||n>=m) return;
			if(n>=n2){
				q2.clear();
				n -= n2;
				// pop last n items from q1
			}else{
				int d = n2 - n;
				// pop last d items from q2
			}
		}
		public T get(int i) {
			final int n = q1.size();
			i = i + i0;
			return i < n ? q1.get(i) : q2.get(i - n);
		}

		public void set(int i, T v) {
			final int n = q1.size();
			i = i + i0;
			if (i < n)
				q1.set(i, v);
			else
				q2.set(i - n, v);
		}

		public void enqueue(T x) {
			q2.add(x);
		}

		public T dequeue() {
			final int n = q1.size();
			final int ii = i0;
			T x;
			if (i0 == n) {
				ArrayList<T> tmp = q1;
				q1 = q2;
				q2 = tmp;
				i0 = 0;
			}
			x = q1.get(ii);
			q1.set(ii, null);
			i0++;
			return x;
		}

	}
	
}
