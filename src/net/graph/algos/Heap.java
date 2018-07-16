package net.graph.algos;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by wuhao on 8/14/16.
 */
public class Heap<T> {
    T []data;
    Comparator<T> cmp;
    int len;
    public Heap(Comparator<T> cmp, int cap) {
        this.cmp = cmp;
        data = (T[])(new Object[cap+1]);
    }
    public boolean isEmpty() {
        return len==0;
    }
    public int cap(){
        return data.length;
    }
    public int size(){
        return len;
    }
    private void ensureCap(int n) {
        if (data.length<n)
            doubleArray();
    }
    private void doubleArray(){
        T []data = this.data;
        T []tmp = (T[])(new Object[data.length*2]);
        System.arraycopy(data, 0, tmp, 0, data.length);
        this.data = tmp;
    }
    // after append, you must call build
    public Heap append(T ...items) {
        int len = this.len;
        ensureCap(len + items.length);
        System.arraycopy(items, 0, data, len, items.length);
        this.len += items.length;
        return this;
    }
    public Heap append(T x) {
        int i = len++;
        ensureCap(len);
        data[i] = x;
        return this;
    }
    // return index of x
    public int add(T x) {
        int i = len++;
        ensureCap(len);
        data[i] = x;
        return up(i);
    }
    public Heap build() {
        for (int i=len/2; i>=0; i--)
            down(i);
        return this;
    }
    public int up(int i) {
        T val = data[i];
        T pv;
        int parent;
        while (i>0) {
            parent = (i-1)/2;
            pv = data[parent];
            if (cmp.compare(pv, val) <= 0) break;
            data[i] = pv;
            i = parent;
        }
        data[i] = val;
        return i;
    }
    public int down(int i) {
        int len = this.len;
        int l;
        T val = data[i];
        T mv;
        while ((l=i+i+1)<len) {
            if ((l+1)<len && cmp.compare(data[l+1], data[l])<0)
                l++;
            mv = data[l];
            if (cmp.compare(val, mv)<=0) break;
            data[i] = mv;
            i = l;
        }
        data[i] = val;
        return i;
    }
    public T deque() {
        if (len==0) throw new ArrayIndexOutOfBoundsException("deque on empty Q");
        T x = data[0];
        data[0] = data[len-1];
        data[--len] = null;
        down(0);
        return x;
    }

    static int rand(int n) {
        return (int)(Math.random()*n);
    }
    // after this sort, T[] should be dec
    public T[] testHeapSort() {
        int L = size();
        build();
        T []tmp = (T[])(new Object[L]);
        for (int i=0; i<L; i++) {
            tmp[i] = this.deque();
        }
        return tmp;
    }
    public static void main(String []args) {
        Comparator<Integer> ci = new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return a.intValue() - b.intValue();
            }
        };
        Heap<Integer> heap = new Heap<>(ci, 1);
        final int L = 1092;
        int []data = new int[L];
        for (int i=0; i<L; i++) {
            data[i] = rand(30000);
            heap.add(data[i]);
        }
        Object []ii = heap.testHeapSort();
        Arrays.sort(data);
        for (int i=0; i<L; i++) {
            if (((Integer)ii[i]).intValue() != data[i])
                throw new RuntimeException("not equal");
        }
        System.out.println("All OK !!!");
    }
}
