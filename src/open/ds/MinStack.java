package open.ds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

//import org.junit.Test;

public class MinStack {
	static class Info {
		int min;
		int count;
		Info(int x){
			this.min = x;
			this.count = 1;
		}
	}
	Stack<Integer> stk = new Stack<>();
	ArrayList<Integer> list = new ArrayList<>();
	Stack<Info> infos = new Stack<>();
    /** initialize your data structure here. */
    public MinStack() {
        
    }
    
    public void push(int x) {
        list.add(x);
        if(infos.isEmpty()){
        	infos.add(new Info(x));
        	return ;
        }
        Info info = infos.peek();
        if(info.min == x){
        	info.count++;
        }else if(info.min > x){
        	infos.add(new Info(x));
        }
    }
    
    public void pop() {
        int x = list.remove(list.size()-1);
        Info info = infos.peek();
        if(x == info.min){
        	if(--info.count == 0){
        		infos.pop();
        	}
        }
    }
    
    public int top() {
        return list.get(list.size()-1);
    }
    
    public int getMin() {
        return infos.peek().min;
    }
    
    
    boolean isPrime(ArrayList<Integer> list, int n) {
    	int N = (int)Math.sqrt(n);
    	int ii=0;
    	
    	list.add(n);
    	return true;
    }
    public int countPrimes(int n) {
        if(n<=2) return 0;
        ArrayList<Integer> list = new ArrayList<>();
//        list.add(2);
//        list.add(3);
        // 2, 3, 5, 7, 11, 13, 17, 19
        int c = 0;
        for(int i=2; i<n; i++){
        	if(isPrime(list, i))
        		c++;
        }
        return c;
    }
    
    public int removeElement(int[] nums, int val) {
        int i=0, j=0;
        int n = nums.length;
        while(j<n){
        	if(nums[j]!=val){
        		if(i!=j)
        			nums[i] = nums[j];
        		i++;
        	}
        	j++;
        }
        return i;
    }
    // generate pascal triangle
    public List<List<Integer>> generate(int numRows) {
    	List<List<Integer>> lists = new ArrayList<>(numRows);
    	if(numRows==0) return lists;
    	ArrayList<Integer> l, l0 = new ArrayList<>(2);
    	l0.add(1);
    	l0.add(0);
    	lists.add(l0);
    	for(int i=1; i<numRows; i++){
    		int N = i+1, j, n;
    		List<Integer> prev = lists.get(i-1);
    		l = new ArrayList<>(N);
    		l.add(1);
    		for(j=1, n=(N+1)/2; j<n; j++){
    			l.add(prev.get(j-1)+prev.get(j));
    		}
    		for(int k=N-1-j; j<N; j++){
    			l.add(l.get(k--));
    		}
    		lists.add(l);
    	}
    	l0.remove(1);
    	
    	return lists;
    }
//    @Test
    public void pascal(){
    	List<List<Integer>> lists = generate(5);
    	System.out.println("[");
    	for(List<Integer> list : lists){
    		System.out.print("[ ");
    		for(int x : list){
    			System.out.print(""+x +", ");
    		}
    		System.out.println("]");
    	}
    }
    // <= & >
    // return a valid index in [lo, hi]
    // ret_nums[ret] = pivot = nums[index]
    int partition(int []nums, int lo, int hi, int index){
    	int val = nums[hi];
    	int i = lo;
    	for(int k=lo; k<hi; k++){
    		if(nums[k]<=val){
//    			if(i!=k){
	    			swap(nums, i, k);
//    			}
    			i++;
    		}
    	}
    	swap(nums, i, hi);
    	return i;
    }
    void swap(int []a, int i, int j){
    	int tmp = a[i];
    	a[i] = a[j];
    	a[j] = tmp;
    }
    int part2(int []nums, int lo, int hi, int index){
    	int pivot = nums[hi];
    	int i=lo;
    	for(int j=lo; j<hi; j++){
    		if(nums[j]<=pivot){
    			swap(nums, i, j);
    			i++;
    		}
    	}
    	swap(nums, i, hi);
    	return i;
    }
    void sortQ(int []data, int i, int j){
    	if(i>=j) return;
    	int idx = partition(data, i, j, (i+j)/2);
    	sortQ(data, 0, idx-1);
    	sortQ(data, idx+1, j);
    }
    void sortQ(int []data){
    	sortQ(data, 0, data.length-1);
    }
    int []genArray(int n, int a, int b){
    	int []data = new int[n];
    	for(int i=0; i<data.length; i++)
    		data[i] = (int) (Math.random()*(b-a)+a);
    	return data;
    }
    int []copy(int []d){
    	int []i = new int [d.length];
    	System.arraycopy(d, 0, i, 0, i.length);
    	return i;
    }
//    @Test
    public void testq(){
    	int []data = genArray(17, -100, 100);
    	int []cp = copy(data);
    	int k = (int) (Math.random()*data.length);
    	int v = findKvalue(cp, k);
    	
    	
    	Arrays.sort(data);
    	
    	sortQ(cp);
    	System.out.print("compare result "+(v==data[k])+"\n");
    	for(int i:data)
    		System.out.printf(" %3d,  ",i);
    	System.out.println();
    	System.out.printf("k=%d  =>  value = %d\n", k, v);
    	for(int i:cp)
    		System.out.printf(" %3d,  ",i);
    	System.out.println();
    }
    // k in [0,1,..., nums.length)
    // find the kth value in the sorted array
    int findKvalue(int []nums, int k){
    	int n = nums.length;
    	int i=0, j=n-1;
    	
    	while(i<=j){
    		int idx = part2(nums, i, j, (i+j)/2);
        	if(idx==k) return nums[idx];
        	if(idx<k){
        		i = idx+1;
        	}else{
        		j = idx-1;
        	}
    	}
    	return k<0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }
    // median value
    public int majorityElement(int[] nums) {
    	int i=0, j=nums.length-1;
        
        int idx = partition(nums, i, j, j/2);
        
        int a = idx-1;
        int b = idx+1;
        
        return 0;
    }
}