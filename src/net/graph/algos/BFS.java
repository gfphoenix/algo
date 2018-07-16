package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by wuhao on 8/14/16.
 */
public class BFS {
    interface Visitor {
        boolean visit(Vertex vertex);
    }

    private static final byte NON_visited = 0;
    private static final byte IN_queue = 1;
    private static final byte DONE = 2;
    public static void bfs(Graph g, int start_order, Visitor fn) {
        final int n = g.getVertexCount();
        if (start_order<0 || start_order>=n) throw new IndexOutOfBoundsException("start order="+start_order);
        byte []visited = new byte[n];
        Queue<Vertex> q = new LinkedList<>();
        q.add(g.getVertexByOrder(start_order));
        visited[start_order] = IN_queue;

        while (!q.isEmpty()) {
            Vertex vertex = q.poll();
            int order = vertex.getOrder();
            if (visited[order] != IN_queue) throw new RuntimeException("bug in visited state, expected in-queue");
            visited[order] = DONE; // visited
            if (fn.visit(vertex)) return; // early return

            List<Edge> edges = vertex.getEdges();
            for (int i=0,n_edges=edges.size(); i<n_edges; i++) {
                Edge edge = edges.get(i);
                Vertex next = edge.getTo();
                int next_order = next.getOrder();
                switch (visited[next_order]) {
                    case NON_visited:
                        q.add(next);
                        visited[next_order] = IN_queue;
                        break;
                    case IN_queue:
                    case DONE:
                        continue;
                    default:
                        throw new RuntimeException("bug in visit state");
                }
            }
        }
    }

    public static void bfs2(Graph g, int start_order, Visitor fn) {
        final int n = g.getVertexCount();
        if (start_order<0 || start_order>=n) throw new IndexOutOfBoundsException("start order="+start_order);
        boolean []visited = new boolean[n]; // init all false

        Queue<Vertex> q = new LinkedList<>();
        q.add(g.getVertexByOrder(start_order));
        visited[start_order] = true;

        while (!q.isEmpty()) {
            Vertex vertex = q.poll();
            // logic visit come here
            if (fn.visit(vertex)) return; // early return

            List<Edge> edges = vertex.getEdges();
            for (int i=0,n_edges=edges.size(); i<n_edges; i++) {
                Edge edge = edges.get(i);
                Vertex next = edge.getTo();
                int next_order = next.getOrder();
                if (!visited[next_order]) {
                    q.add(next);
                    visited[next_order] = true;
                }
            }
        }
    }
}
