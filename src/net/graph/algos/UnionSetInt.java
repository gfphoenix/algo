package net.graph.algos;

/**
 * Created by wuhao on 8/14/16.
 */
public class UnionSetInt {
    int[] set;
    int M;

    public UnionSetInt(int[] set) {
        this.set = check(set);
        this.M = unionSize(set);
    }

    public UnionSetInt(int n) {
        this.set = initSet(new int[n]);
        this.M = n;
    }

    public static int[] initSet(int[] set) {
        if (set != null)
            for (int i = 0, n = set.length; i < n; i++)
                set[i] = i;
        return set;
    }

    private int[] check(int[] set) {
        if (set == null) throw new NullPointerException("set must not be null");
        final int n = set.length;
        if (n == 0) throw new IllegalArgumentException("set length is 0");
        for (int i = 0; i < n; i++)
            if (set[i] < 0 || set[i] >= n)
                throw new IndexOutOfBoundsException("set[" + i + "]=" + set[i]);
        return set;
    }

    public int size() {
        return set.length;
    }

    public int unionSize() {
        return M;
    }

    public static int unionSize(int[] set) {
        int c = 0;
        for (int i = 0, n = set.length; i < n; i++)
            if (set[i] == i)
                c++;
        return c;
    }

    public int find(int i) {
        if (set[i] != i)
            set[i] = find(set[i]);
        return set[i];
    }

    public int union(int a, int b) {
        int x = find(a);
        int y = find(b);
        if (x != y) {
            set[y] = x;
            M--;
        }
        return x;
    }

    public boolean same(int a, int b) {
        return find(a) == find(b);
    }
}
