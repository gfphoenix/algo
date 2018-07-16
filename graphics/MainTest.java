package graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wuhao on 7/25/16.
 */
public class MainTest {
    public static void main0(String[] args) {
        FloatBuffer buffer0 = FloatBuffer.allocate(218440);
        ByteBuffer bb = ByteBuffer.allocate(1024);
        ShortBuffer ssb = bb.asShortBuffer();
        for (int i = 0; i < 20; i++) {
            ssb.put((short) i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("float pos=").append(ssb.position()).append(", limit = ").append(ssb.limit()).append("\n");
        sb.append("byte pos =").append(bb.position()).append(", limit = ").append(bb.limit()).append("\n");
        System.out.println(sb);

        ssb.flip();
        sb.setLength(0);
        sb.append("float pos=").append(ssb.position()).append(", limit = ").append(ssb.limit()).append("\n");
        sb.append("byte pos =").append(bb.position()).append(", limit = ").append(bb.limit()).append("\n");
        System.out.println(sb);

        bb.position(ssb.limit() * (Short.SIZE >> 3));
        bb.flip();
        sb.append("float pos=").append(ssb.position()).append(", limit = ").append(ssb.limit()).append("\n");
        sb.append("byte pos =").append(bb.position()).append(", limit = ").append(bb.limit()).append("\n");
        System.out.println(sb);

        byte[] raw = new byte[bb.limit()];
        bb.get(raw);
        short[] raws = new short[ssb.limit()];
        ssb.get(raws);

        sb.setLength(0);
        sb.append("[ ").append(String.format("%02x", raw[0])).append(String.format("%02x", raw[1]));
        for (int i = 2; i < raw.length; i += 2) {
            sb.append(", ").append(String.format("%02x", raw[i])).append(String.format("%02x", raw[i + 1]));
        }
        sb.append(" ]\n[ ");
        sb.append(String.format("%04x", raws[0]));
        for (int i = 1; i < raws.length; i++) {
            sb.append(", ").append(String.format("%04x", raws[i]));
        }
        sb.append(" ]\n");
        System.out.println(sb);

        sb.setLength(0);
        sb.append("int ").append(Integer.SIZE).append(", long ").append(Long.SIZE).append("\n");
        System.out.println(sb);

        {
            String a = "abcdefg";
            String b = new String(a);
            System.out.println(b);

            for (int i = 0; i < 26; i++) {
                System.out.printf("sqrt(%d) => %d\n", i, mySqrt(i));
            }
            System.out.println("max = " + Integer.MAX_VALUE + ", " + Math.sqrt(2147483647));
            int M = (int) Math.sqrt(Integer.MAX_VALUE);
            System.out.println("M = " + M + ", MM=" + (M * M));
            System.out.println(mySqrt(2147395599));

        }
        {
            double x = 3;
            for (int i = 0; i < 10; i++) {
                double y0 = Math.pow(x, i);
                double y1 = myPow(x, i);
                double dd = y1 / y0;
                System.out.println("" + i + "  =>  " + dd);
            }
            System.out.println(myPow(1.00000, -2147483648));
        }
    }

    public static int mySqrt(int x) {
        if (x < 0) return -1;
        if (x < 2) return x;

        final int M = 46340;
        if (x >= M * M) return M;
        int a = 1, b = x / 2;
        if (b > M) b = M;
        while (a <= b) {
            int m = a + (b - a) / 2;
            int mm = m * m;
            if (mm == x) return m;
            if (mm > x) b = m - 1;
            else a = m + 1;
        }
        return b;
    }

    public static double myPow(double x, int n) {
        if (n < 0) {
            return 1.0 / (x * myPow(x, -(n + 1)));
        }
        double p = x;
        double y = 1;
        while (n != 0) {
            if ((n & 1) != 0) y *= p;
            p = p * p;
            n >>= 1;
        }
        return y;
    }

    public int getSum(int a, int b) {
        int c = a & b;
        int d = a | b;
        if (c == 0) return d;
        return getSum(c << 1, (~c) & d);
    }

static class NIt implements Iterator<Integer> {
    NestedInteger ni;
    NIt it;
    int idx;
    boolean has;
    NIt(NestedInteger ni) {
        this.ni = ni;
        has = ni.isInteger();
        idx = 0;
        init();
    }
    void init(){
        if (has) return;
        int i=0;
        it = null;
        List<NestedInteger> list = ni.getList();
        for (int n=list.size(); i<n; i++) {
            NestedInteger x = list.get(i);
            if (x==null) continue;
            NIt nit = new NIt(x);
            if (nit.hasNext()){
                it = nit;
                break;
            }
        }
        idx = i;
    }
    @Override
    public boolean hasNext() {
        if (ni.isInteger()) {
            return has;
        }
        List<NestedInteger> list = ni.getList();
        if (it!=null && it.hasNext()) return true;
        while (++idx < list.size()) {
            NestedInteger x = list.get(idx);
            if (x==null) continue;
            it = new NIt(x);
            if (it.hasNext()) return true;
        }
        return false;
    }

    @Override
    public Integer next() {
        if (ni.isInteger()){
            if (has) {
                has = false;
                return ni.getInteger();
            }
            // no value
            throw new RuntimeException("no value available");
        }
        return it.next();
    }

    @Override
    public void remove() {

    }
}
//    public class NestedIterator implements Iterator<Integer> {
//        List<NestedInteger> list;
//
//        public NestedIterator(List<NestedInteger> nestedList) {
//            this.list = nestedList;
//        }
//
//        @Override
//        public Integer next() {
//
//        }
//
//        @Override
//        public boolean hasNext() {
//
//        }
//    }

    public interface NestedInteger {
        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return null if this NestedInteger holds a single integer
        List<NestedInteger> getList();
    }
    static class  NI implements NestedInteger {
        boolean i;
        int ii;
        List<NestedInteger> l;
private NI(){}
        NI(int ii){
            this.i = true;
            this.ii = ii;
        }
        static NI I(int ii){
            return new NI(ii);
        }
        static NI L(int ...is) {
            NI x = new NI();
            x.i = false;
            x.l = new ArrayList<NestedInteger>(is.length);
            for (int k=0; k<is.length; k++) {
                x.l.add(I(is[k]));
            }
            return x;
        }
        NI(NestedInteger ... nis) {
            this.i = false;
            l = new ArrayList<NestedInteger>();
            for (NestedInteger x : nis) {
                l.add(x);
            }
        }

        @Override
        public boolean isInteger() {
            return i;
        }

        @Override
        public Integer getInteger() {
            if (i)
                return ii;
            throw new RuntimeException("not int");
        }

        @Override
        public List<NestedInteger> getList() {
            if (i)
                throw new RuntimeException("is int, not list");
            return l;
        }
    }
    public static void main1(String []args) {
        NI a = NI.L(1, 2);
        NI b = NI.L(3,4);
        NI c = new NI(a, NI.I(5), b);
        NIt nit = new NIt(c);
        int i=0;
        int n=20;
        while (i<n && nit.hasNext()) {
            System.out.println(nit.next());
            i++;
        }
    }
    public static void main(String []args) {
        int a = Integer.MAX_VALUE;
        int b = (int)Math.sqrt(a);
        int c = b*b;
        System.out.println(""+a+", "+b+", "+c);
        while (true) {
            c = b*b;
            if (c<=0) {
                break;
            }
            b++;
        }
        System.out.println(""+a+", "+b+", "+c);

        while (true){
            c = b*b;
            if (c>=0) break;
            b++;
        }
        System.out.println(""+a+", "+b+", "+c);

        Integer i1=  1;
        Integer i2 = 1;
        Integer i3 = 0x12345678;
        Integer i4 = 0x12345678;
        Integer i5 = new Integer(1);
        System.out.println(i1==i2);
        System.out.println(i3==i4);
        System.out.println(i1==i5);

    }
}
