package open.ds;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

// two Queue to implement a stack
public class Q2Stack {
    Queue<Integer> q_ = new LinkedList<Integer>();
    Queue<Integer> h_ = new LinkedList<>();
    // Push element x onto stack.
    public void push(int x) {
    	if(q_.isEmpty()){
    		q_.add(x);
    		return ;
    	}
        h_.add(x);
        while(!q_.isEmpty())
        	h_.add(q_.poll());
        Queue<Integer> tmp = h_;
        h_ = q_;
        q_ = tmp;
    }

    // Removes the element on top of the stack.
    public void pop() {
        q_.poll();
    }

    // Get the top element.
    public int top() {
        return q_.peek();
    }

    // Return whether the stack is empty.
    public boolean empty() {
        return q_.isEmpty();
    }
    
    //////////////////////////////////////////////////////////////////
    ///  other code
    public int[] productExceptSelf(int[] nums) {
    	int n = nums.length;
        int []out = new int [n];
        int v = nums[n-1];
        out[n-1] = 1;
        for(int i=n-2; i>=0; i--)
        	out[i] = out[i+1] * nums[i+1];
        v = nums[0];
        for(int i=1; i<n-1; i++){
        	out[i] = v * out[i];
        	v = v * nums[i];
        }
        out[n-1] = v;
        
        return out;
    }
    
    public boolean containsNearbyDuplicate(int[] nums, int k) {
    	if(nums==null || nums.length==0 || k<1) return false;
    	HashSet<Integer> set = new HashSet<>(2*k);
    	int i=0, j=1, v, n = nums.length;
    	set.add(nums[0]);
    	for(int m = Math.min(k, n); j<m; j++){
    		v = nums[j];
    		if(set.contains(v))
    			return true;
    		set.add(v);
    	}
    	for(; j<n; j++, i++){
    		v = nums[j];
    		if(set.contains(v))
    			return true;
    		set.remove(nums[i]);
    		set.add(v);
    	}
    	return false;
    }
    
    static class Qinfo{
    	int count;
    	int value;
    	public Qinfo(int val) {
    		this.count = 1;
    		this.value = val;
		}
    }
    private static class LinkNode {
    	LinkNode next;
    	int value;
    	LinkNode(int val){
    		this.next = null;
    		this.value = val;
    	}
    }
    private static class LinkQ {
    	int count=0;
    	final LinkNode fake_head = new LinkNode(0);
    	LinkNode tail;
    	LinkQ(){
    		this.tail = fake_head;
    	}
    	int size() { return count; }
    	boolean isEmpty(){
    		return !(count>0 && fake_head.next!=null);
    	}
    	void clear(){
    		count = 0;
    		fake_head.next = null;
    		tail = fake_head;
    	}
    	void add(int x){
    		this.tail.next = new LinkNode(x);
    		this.tail = this.tail.next;
    	}
    	boolean check(String func){
    		if(count<1 || fake_head.next==null){
    			// throw
    			throw new RuntimeException(func+" on empty linkQ: size="+count+", pointer="+fake_head.next);
    		}
    		return true;
    	}
    	int poll(){
    		check("poll");
    		LinkNode node = fake_head.next;
    		fake_head.next = node.next;
    		if(--count == 0)
    			tail = fake_head;
    		return node.value;
    	}
    	int peek(){
    		check("peek");
    		return fake_head.next.value;
    	}
    }
    static class MaxQ{
    	LinkedList<Qinfo> q_info = new LinkedList<>();
//    	Queue<Integer> q = new LinkedList<>();
    	LinkQ q = new LinkQ();
		
		public void clear() {
			q.clear();
			q_info.clear();
		}
		
		public boolean isEmpty() {
			return q.isEmpty();
		}
		
		public int size() {
			return q.size();
		}
		
		public Integer peek() {
			return q.peek();
		}
		public void enqueue(int x){
			q.add(x);
			while(q_info.size()>0 && q_info.getLast().value < x)
				q_info.pollLast();
			Qinfo tmp;
			if(q_info.size()>0 && (tmp=q_info.getLast()).value == x){
				tmp.count++;
			}else{
				q_info.add(new Qinfo(x));
			}
		}
		public int dequeue(){
			int v = q.poll();
			Qinfo info = q_info.peek();
			if(info.value==v){
				if(--info.count == 0)
					q_info.poll();
			}
			return v;
		}
		private void checkmax(){
			final int m = max();
			int v = m-1;
			
		}
		public int max(){
			return q_info.peek().value;
		}
    }
    
}