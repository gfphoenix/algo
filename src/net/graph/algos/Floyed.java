package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.List;

/**
 * Created by wuhao on 8/14/16.
 */
public class Floyed {
    // assume that all weights are non-positive
    public static int [][] floy(Graph g, Vertex [][]come_from, Vertex [][]next) {
        final int n = g.getVertexCount();
        int [][]cost = new int[n][n];
        for (int i=0; i<n; i++) {
            Vertex vertex = g.getVertexByOrder(i);
            for (int j=0; j<n; j++) {
                cost[i][j] = Integer.MAX_VALUE;
                come_from[i][j] = null; // unreachable
                next[i][j] = null;
            }
            cost[i][i] = 0;
            come_from[i][i] = vertex;
            next[i][i] = vertex;
            for (Edge edge : vertex.getEdges()) {
                Vertex to = edge.getTo();
                int to_order = to.getOrder();
                cost[i][to_order] = edge.getCost().cost();
                come_from[i][to_order] = vertex;
                next[i][to_order] = to;
            }
        }

        // 3 for iteration
        for (int k=0; k<n; k++)
        for (int i=0; i<n; i++)
        for (int j=0; j<n; j++)
            if (cost[i][k]< cost[i][j]-cost[k][j]){
            // overflow error
//              if (cost[i][k]+cost[k][j] < cost[i][j]){
                cost[i][j] = cost[i][k] + cost[k][j];
                come_from[i][j] = come_from[k][j];
                next[i][j] = next[i][k];
            }

        return cost;
    }
    static void build_path_next(Graph g, Vertex [][]next, List<Vertex> list, int u, int v) {
        if (next[u][v] == null) return;
        Vertex target = g.getVertexByOrder(v);
        list.add(g.getVertexByOrder(u));
        if (u==v) return;
        while (next[u][v] != target) {
            Vertex tmp = next[u][v];
            list.add(tmp);
            u = tmp.getOrder();
        }
        list.add(target);
    }
    static void build_path_come_from(Graph g, Vertex [][]come_from, List<Vertex> list, int u, int v) {
        if (come_from[u][v] == null) return;
        Vertex from = g.getVertexByOrder(u);
        list.add(g.getVertexByOrder(v));
        if (u==v) return;
        int n=2;
        while (come_from[u][v] != from) {
            Vertex tmp = come_from[u][v];
            list.add(tmp);
            v = tmp.getOrder();
            n++;
        }
        if (come_from[u][v] != from)
            throw new RuntimeException("bug in build path");
        list.add(from);
        // swap list
        {
            for (int r = list.size()-1, l=list.size()-n; l<r; l++,r--) {
                Vertex tmp = list.get(l);
                list.set(l, list.get(r));
                list.set(r, tmp);
            }
        }
    }
}
