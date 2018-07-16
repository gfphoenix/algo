package net.graph.algos;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by wuhao on 8/14/16.
 */
public class BHeap<T> implements PriorityQueue<T> {
    Comparator<T> cmp;
    PrivatePosition<T> []data;
    int len;
    private static class PrivatePosition<T> implements Position {
        private T item;
    // prevent this position used to another queue, mark queue owner
        private BHeap parent;
        private int index;
        PrivatePosition(BHeap parent, T x){
            this.parent = parent;
            item = x;
        }
    }
    public BHeap(Comparator<T> cmp){
        init(cmp, 8);
    }
    public BHeap(Comparator<T> cmp, int cap) {
        init(cmp, cap);
    }
    BHeap(Comparator<T> cmp, PrivatePosition<T> []data){
        this.cmp = cmp;
        this.data = data==null ? new PrivatePosition[8] : data;
        this.len = 0;
    }
    protected void init(Comparator<T> cmp, int cap) {
        this.cmp = cmp;
        this.len = 0;
        this.data = new PrivatePosition[cap];
    }
    @Override
    public int size() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len==0;
    }
    @Override
    public Comparator<T> getComparator() {
        return cmp;
    }

    public Position add(T x) {
        return push(x);
    }
    @Override
    public Position push(T x) {
        int len = this.len++;
        ensureCap(len+1);
        PrivatePosition pos = new PrivatePosition(this, x);
        data[len] = pos;
        up(len);
        return pos;
    }
    @Override
    public T peek(Position position) {
        return checkPosition(position).item;
    }

    @Override
    public T findMin() {
        if (isEmpty()) throw new UnderflowException("findMin for empty queue");
        return data[0].item;
    }

    @Override
    public T deleteMin() {
        if (isEmpty()) throw new UnderflowException("deleteMin on empty queue");
        PrivatePosition<T> tmp = data[0];
        PrivatePosition<T> pos = data[--len];
        data[0] = pos;
        data[len] = null;
        if (len>0) down(0);

        tmp.parent = null;
        tmp.index = -1;
        return tmp.item;
    }
    public T delete(Position position) {
        PrivatePosition<T> pos = checkPosition(position);
        T result = pos.item;
        int index = pos.index;
        pos.parent = null;
        pos.index = -1;
        len--;
        if (index == len)
            return result;
        pos = data[len]; // last item
        data[index] = pos;
        pos.index = index;
        updatePriority(pos);
        return result;
    }
    @Override
    public boolean updatePriority(Position p) {
        PrivatePosition<T> pos = checkPosition(p);
        int index = pos.index;
        if (index>0 && cmp.compare(pos.item, data[(index-1)/2].item)<=0) up(index);
        else down(index);
        return true;
    }

    private void ensureCap(int n) {
        if (data.length<n)
            doubleArray();
    }
    private void doubleArray(){
        PrivatePosition []data = this.data;
        PrivatePosition []tmp = (new PrivatePosition[data.length*2]);
        System.arraycopy(data, 0, tmp, 0, data.length);
        this.data = tmp;
    }
    public boolean isValidPosition(Position position) {
        boolean ok = position != null;
        ok = ok && (position instanceof PrivatePosition);
        if (ok) {
            PrivatePosition pos = (PrivatePosition)position;
            ok = pos.parent == this;
        }
        return ok;
    }
    private PrivatePosition<T> checkPosition(Position pos) {
        if (pos == null)
            throw new NullPointerException("pos must not be null");
        if (!(pos instanceof PrivatePosition))
            throw new InvalidPositionException("invalid position: not inner object");
        PrivatePosition pp = (PrivatePosition)pos;
        if (pp.parent != this)
            throw new InvalidPositionException("invalid position: out of this queue");
        return pp;
    }
    public PrivatePosition<T> up(int i) {
        final PrivatePosition<T> val = data[i];
        PrivatePosition<T> pv;
        int parent;
        while (i>0) {
            parent = (i-1)/2;
            pv = data[parent];
            if (cmp.compare(pv.item, val.item) <= 0) break;
            data[i] = pv;
            pv.index = i;
            i = parent;
        }
        data[i] = val;
        val.index = i;
        return val;
    }
    public void down(int i) {
        int len = this.len;
        int l;
        PrivatePosition<T> val = data[i];
        PrivatePosition<T> mv;
        while ((l=i+i+1)<len) {
            if ((l+1)<len && cmp.compare(data[l+1].item, data[l].item)<0)
                l++;
            mv = data[l];
            if (cmp.compare(val.item, mv.item)<=0) break;
            data[i] = mv;
            mv.index = i;
            i = l;
        }
        data[i] = val;
        val.index = i;
    }

    private void checkIndex() {
        for (int i=0, n=len; i<n; i++) {
            PrivatePosition<T> pos = data[i];
            if (pos.index != i) throw new IllegalStateException("index is wrong");
        }
    }
    private void checkOrder() {
        final int n = this.len;
        for (int i=n/2; i>=0; i--) {
            PrivatePosition<T> c = data[i];
            int li = 2*i + 1;
            if (li<n && cmp.compare(data[li].item, c.item)<0)
                    throw new IllegalStateException("left child is less than parent");
            li++;
            if (li<n && cmp.compare(data[li].item, c.item)<0)
                throw new IllegalStateException("right child is less than parent");
        }
    }
    static int rand(int n) {
        return (int)(Math.random()*n);
    }
    public static void main(String []args) {
        Comparator<Integer> ci = new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return a.intValue() - b.intValue();
            }
        };
        BHeap<Integer> heap = new BHeap<>(ci, 1);
        final int L = 1092;
        int []data = new int[L];
        for (int i=0; i<L; i++) {
            data[i] = rand(30000);
            PrivatePosition<Integer> pos = (PrivatePosition<Integer>)heap.add(data[i]);
            if (pos.parent != heap) throw new IllegalStateException("parent is wrong");
            if (heap.data[pos.index] != pos) throw new IllegalStateException("index wrong");
            if (pos.item.intValue() != data[i]) throw new IllegalStateException("value is wrong");
        }
        System.out.println("stage 1 is right");
        Arrays.sort(data);
        for (int i=0; i<L; i++) {
            int val = rand(30000);
            PrivatePosition<Integer> pos = heap.data[i];
            pos.item = val;
            System.out.println("loop = "+i);
            heap.updatePriority(pos);

            heap.checkIndex();
            heap.checkOrder();
        }
        System.out.println("All OK !!!");
    }
}
