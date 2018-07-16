package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 8/14/16.
 */
public class DFS {
    static void call(Vertex vertex, List<Integer> q, int depth) {
        Graph g = vertex.getGraph();
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        if (q.size()>0) sb.append(g.getVertexByOrder(q.get(0).intValue()).getTag());
        for (int i=1; i<q.size(); i++)
            sb.append(", ").append(g.getVertexByOrder(q.get(i).intValue()).getTag());
        sb.append(" ]");
        sb.append(" => dep=").append(depth);
        System.out.println(sb);
    }
    // print all simple paths start from start_order
    public static void dfs(Graph g, int start_order) {
        int n = g.getVertexCount();
        if (start_order<0 || start_order>=n) throw new IndexOutOfBoundsException("start order="+start_order);
        boolean []visited = new boolean[n];
        List<Integer> q = new ArrayList<>();
        dfs_helper(g, q, visited, start_order, 0);
    }
    static void dfs_helper(Graph g, List<Integer> q, boolean []visited, int start_order, int depth) {
        Vertex vertex = g.getVertexByOrder(start_order);
        int n = q.size();
        q.add(start_order);
        visited[start_order] = true;
        call(vertex, q, depth);
        List<Edge> edges = vertex.getEdges();
        for (int i=0,n_edges=edges.size(); i<n_edges; i++) {
            Edge edge = edges.get(i);
            Vertex to = edge.getTo();
            int next_order = to.getOrder();
            if (visited[next_order]) continue;
            dfs_helper(g, q, visited, next_order, depth+1);
        }
        visited[start_order] = false;
        q.remove(n);
    }
    static void dfs2(Graph g, int start, int target) {
        final int n = g.getVertexCount();
        ArrayList<Vertex> stack = new ArrayList<>();
        boolean []visited = new boolean[n];
        stack.add(g.getVertexByOrder(start));
        visited[start] = true;

        while (!stack.isEmpty()) {
            Vertex vertex = stack.remove(stack.size()-1);
            int order = vertex.getOrder();
            if (order == target) return; // found it!
            List<Edge> edges = vertex.getEdges();
            for (int i=0,n_edges=edges.size(); i<n_edges; i++) {
                Edge edge = edges.get(i);
                Vertex to = edge.getTo();
                if (!visited[to.getOrder()]) {
                    stack.add(vertex);
                    visited[to.getOrder()] = true;
                }
            }
        }
        // here not found
    }
    public static void main(String []args) {
        Graph g = new Graph();

    }
}
