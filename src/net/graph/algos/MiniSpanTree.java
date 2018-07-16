package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuhao on 9/7/16.
 */
public class MiniSpanTree {
    public static Graph mini(Graph g) {
        return g;
    }
    // greedy by edge, add minimal cost edge for each
    public static List<Edge> mini_span_tree(Graph g) {
        ArrayList<Edge> list = new ArrayList<>();
        final int n = g.getVertexCount();
        for (int i=0; i<n; i++) {
            Vertex v = g.getVertexByOrder(i);
            list.addAll(v.getEdges());
        }
        list.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return e2.getCost().cost() - e1.getCost().cost();
            }
        });
        UnionSetInt us = new UnionSetInt(n);
        ArrayList<Edge> result = new ArrayList<>();
        for (int i=n; i-->0; ) {
            Edge e = list.get(i);
            Vertex from = e.getFrom();
            Vertex to = e.getTo();
            int i_from = from.getOrder();
            int i_to = to.getOrder();
            if (us.same(i_from, i_to)) continue;
            us.union(i_from, i_to);
            result.add(e);
        }
        if (us.unionSize()!=1)
            throw new IllegalStateException("graph can not have a span tree, not connected");
        return result;
    }
    public static List<Edge> mini_span_tree2(Graph g) {
        final int n = g.getVertexCount();
        final ArrayList<Edge> result = new ArrayList<>();
        final ArrayList<Edge> tmp = new ArrayList<>();
        UnionSetInt us = new UnionSetInt(n);
        final Comparator<Edge> cmp = ((e1, e2) -> e1.getCost().cost() - e2.getCost().cost());
        for (int i=0; i<n; i++) {
            Vertex v = g.getVertexByOrder(i);
            tmp.clear();
            tmp.addAll(v.getEdges());
            tmp.sort(cmp);
            for (int k=0; k<tmp.size(); k++) {
                Edge e = tmp.get(k);
                int i1 = e.getFrom().getOrder();
                int i2 = e.getTo().getOrder();
                if (us.same(i1, i2)) continue;
                us.union(i1, i2);
                result.add(e);
            }
        }
        if (us.unionSize()!=1)
            throw new IllegalStateException("graph can not have a span tree, not connected");
        return result;
    }
}
