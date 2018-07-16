package open.ds;
import java.util.LinkedList;
import java.util.Queue;

public class Qmm<T extends Comparable<T>> {
	private static class Qinfo<T extends Comparable<T>> {
		int count;
		T value;
		private Qinfo(T x) {
			count = 1;
			value = x;
		}
	}
	
	Queue<T> q = new LinkedList<T>();
	LinkedList<Qinfo<T>> q_min = new LinkedList<>();
	LinkedList<Qinfo<T>> q_max = new LinkedList<>();
	
	public boolean empty(){
		return q.isEmpty();
	}
	public void enqueue(T x){
		q.add(x);
		Qinfo<T> tmp;
		
		// max 
		while(q_max.size()>0 && q_max.getLast().value.compareTo(x)<0)
			q_max.pollLast();

		if(q_max.size()>0 && (tmp=q_max.getLast()).value.compareTo(x)==0){
			tmp.count++;
		}else{
			q_max.add(new Qinfo<T>(x));
		}
		
		// min
		while(q_min.size()>0 && q_min.getLast().value.compareTo(x)>0)
			q_min.pollLast();
		
		if(q_min.size()>0 && (tmp=q_min.getLast()).value.compareTo(x)==0){
			tmp.count++;
		}else{
			q_min.add(new Qinfo<T>(x));
		}
	}
	public T dequeue(){
		T x = q.poll();
		Qinfo<T> mm;
		
		mm = q_min.peekFirst();
		if(mm.value.compareTo(x)==0){
			if(--mm.count == 0)
				q_min.pollFirst();
		}
		mm = q_max.peekFirst();
		if(mm.value.compareTo(x)==0){
			if(--mm.count == 0)
				q_max.pollFirst();
		}
		return x;
	}
	public T peek(){
		return q.peek();
	}
	public T min(){
		return q_min.peekFirst().value;
	}
	public T max(){
		return q_max.peekFirst().value;
	}
}
