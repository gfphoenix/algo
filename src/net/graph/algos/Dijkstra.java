package net.graph.algos;

import net.graph.Cost;
import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.Comparator;
import java.util.List;

/**
 * Created by wuhao on 8/14/16.
 */
public class Dijkstra {
    static class Entry{
        Vertex vertex;
        PriorityQueue.Position position;
        Entry(Vertex vertex) {
            this.vertex =vertex;
        }
    }

    int []dijkstra_cost(Graph G, String start_tag) {
        return dijkstra_cost(G, G.getVertexOrderByTag(start_tag));
    }

    private static final byte NON_visited = 0;
    private static final byte PENDING = 1;
    private static final byte DONE = 2;
    // return cost by order
    static int []dijkstra_cost(Graph G, int start_order) {
        final int n = G.getVertexCount();
//        if (start<0 || start>=n) throw new IndexOutOfBoundsException("start = "+start);
        Vertex s0 = G.getVertexByOrder(start_order);
        final int []cost = new int[n];
        final byte []flags = new byte[n];
        // flags : 0 => non visited yet, 1 => visit in Q, but not done, 2 => done
        PriorityQueue.Position []positions = new PriorityQueue.Position[n];
        PriorityQueue<Vertex> q = new BHeap<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex a, Vertex b) {
                return cost[a.getOrder()] - cost[b.getOrder()];
            }
        }, n);

        for (int i=0; i<n; i++) {
            cost[i] = Integer.MAX_VALUE;
            flags[i] = NON_visited;
        }
        cost[start_order] = 0;
        flags[start_order] = PENDING;
        positions[start_order] = q.push(s0);

        while (!q.isEmpty()) {
            Vertex vertex = q.deleteMin();
            int order = vertex.getOrder();
            int v_cost = cost[order];
            flags[order] = DONE;

            // change neighbors
            List<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                Vertex to = edge.getTo();
                int next_idx = to.getOrder();
                if (flags[next_idx] == DONE) continue;
                if (flags[next_idx] == NON_visited) {
                    positions[next_idx] = q.push(to);
                    flags[next_idx] = PENDING;
                }
//                FIXME : overflow error should take care
                int new_cost = v_cost + edge.getCost().cost();
                if (new_cost < cost[next_idx]) {
                    cost[next_idx] = new_cost;
                    // update queue by decreased cost
                    q.updatePriority(positions[next_idx]);
                }
            }
        }
        return cost;
    }
    static int []dijkstra_cost2(Graph G, int start_order) {
        final int n = G.getVertexCount();
        Vertex s0 = G.getVertexByOrder(start_order);
        final int []cost = new int[n];
        final byte []flags = new byte[n];
        // flags : 0 => non visited yet, 1 => visit in Q, but not done, 2 => done
        PriorityQueue.Position []positions = new PriorityQueue.Position[n];
        PriorityQueue<Vertex> q = new BHeap<Vertex>(new Comparator<Vertex>() {
            @Override
            public int compare(Vertex a, Vertex b) {
                return cost[a.getOrder()] - cost[b.getOrder()];
            }
        }, n);

        for (int i=0; i<n; i++) {
            cost[i] = Integer.MAX_VALUE;
            flags[i] = NON_visited;
        }
        cost[start_order] = 0;
        flags[start_order] = PENDING;
        positions[start_order] = q.push(s0);

        while (!q.isEmpty()) {
            Vertex vertex = q.deleteMin();
            int order = vertex.getOrder();
            int v_cost = cost[order];
            if (flags[order]!= PENDING) throw new IllegalStateException("vertex flag should be in-queue");
            flags[order] = DONE;

            // change neighbors
            List<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                Vertex to = edge.getTo();
                int next_idx = to.getOrder();
                if (flags[next_idx] == DONE) continue;
                if (flags[next_idx] == NON_visited) {
                    positions[next_idx] = q.push(to);
                    flags[next_idx] = PENDING;
                }

                // FIXME : overflow error should take care
                int new_cost = v_cost + edge.getCost().cost();
                if (new_cost < cost[next_idx]) {
                    cost[next_idx] = new_cost;
                    // update queue by decreased cost
                    q.updatePriority(positions[next_idx]);
                }
            }
        }
        return cost;
    }

    static class IntCost implements Cost{
        int i;
        IntCost(int i){
            this.i = i;
        }
        void setI(int i) {
            this.i = i;
        }
        @Override
        public int cost() {
            return i;
        }
    }

}
