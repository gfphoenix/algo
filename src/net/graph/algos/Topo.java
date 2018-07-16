package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 9/6/16.
 */
public class Topo {
    public boolean topo(Graph g, List<Vertex> result) {
        final int n = g.getVertexCount();
        final ArrayList<Vertex> q = new ArrayList<>(n/2);
        final int []in_degree = new int[n];
        for (int i=0; i<n; i++) {
            Vertex v = g.getVertexByOrder(i);
            for (Edge e : v.getEdges()) {
                int to = e.getTo().getOrder();
                in_degree[to]++;
            }
        }
        for (int i=0; i<n; i++)
            if (in_degree[i] == 0)
                q.add(g.getVertexByOrder(i));
        final int list_size = result.size();
        while (!q.isEmpty()) {
            int size = q.size();
            int i = (int)(Math.random() * size);
            Vertex v = q.get(i);
            q.set(i, q.get(size-1));
            q.remove(size-1);
            result.add(v);
            for (Edge e : v.getEdges()) {
                Vertex to = e.getTo();
                if (--in_degree[to.getOrder()] == 0)
                    q.add(to);
            }
        }
        return result.size() - list_size == n;
    }
}
