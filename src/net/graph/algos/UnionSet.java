package net.graph.algos;

import java.util.ArrayList;

/**
 * Created by wuhao on 8/14/16.
 */
public class UnionSet {
    private UnionSet parent;

    public UnionSet(){
        parent = this;
    }
    public boolean isRoot() {
        return parent==this;
    }
    public UnionSet find(){
        if (parent!=this)
            parent = parent.find();
        return parent;
    }
    public UnionSet find2() {
        if (this==parent) return this;
        ArrayList<UnionSet> stack = new ArrayList<>(8);
        UnionSet pp=this;
        for (; pp != pp.parent; pp = pp.parent)
            stack.add(pp);
        for (UnionSet p : stack)
            p.parent = pp;
        return pp;
    }
    public UnionSet union(UnionSet o) {
        UnionSet a = find();
        UnionSet b = o.find();
        if (a!=b)
            b.parent = a;
        return a;
    }
    public boolean same(UnionSet o) {
        return find() == o.find();
    }
}
