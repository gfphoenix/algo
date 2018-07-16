package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wuhao on 8/14/16.
 */
public class AStar {
    // Heuristic function in A_star, implement at least one method
    interface H {
        default int cost(Graph graph, int from_order, int to_order) {
            return cost(graph, graph.getVertexByOrder(from_order), graph.getVertexByOrder(to_order));
        }
        default int cost(Graph graph, Vertex from, Vertex to) {
            return cost(graph, from.getOrder(), to.getOrder());
        }
    }
    private static void checkIndex(int n, int start_order, int to_order) {
        if (start_order<0 || start_order>=n) throw new IndexOutOfBoundsException("start = "+start_order);
        if (to_order<0 || to_order>=n) throw new IndexOutOfBoundsException("to = "+to_order);
    }
    private static final byte NON_visited = 0;
    private static final byte IN_queue = 1;
    private static final byte DONE = 2;
    // null means no way, [0] means start==to, no need to move
    // NOTE: you should make sure that the order of Graph must validate
    List<Vertex> a_star_search(final Graph g, final H h, int start_order, int to_order) {
        final int n = g.getVertexCount();
        checkIndex(n, start_order, to_order);
        if (start_order == to_order) return new ArrayList<>(); //
        /** init some variables, we need 4 arrays
         * 1. come_from : record search the search path
         * 2. cost : the cost of each visited vertex starts from s0
         * 3. flags: used to mark the state of the vertex
         * 4. positions : map vertex_order to the position in the priority-queue *QUICKLY*
         **/
        final Vertex s0 = g.getVertexByOrder(start_order);
        final Vertex target = g.getVertexByOrder(to_order);
        final Vertex []come_from = new Vertex[n]; // prio vertex of order i_order is prio[i_order];
        final int []cost = new int[n];
        final byte []flags = new byte[n]; // flags : 0 => non visited yet, 1 => visit in Q, but not done, 2 => done
        final PriorityQueue.Position []positions = new PriorityQueue.Position[n];
        /** cmp : measurement for priority queue
         * for A* algorithm, it uses an heuristic function
         * */
        final Comparator<Vertex> cmp = new Comparator<Vertex>() {
            @Override
            public int compare(Vertex a, Vertex b) {
                return (cost[a.getOrder()]+h.cost(g, a, target))  - (cost[b.getOrder()]+h.cost(g, b, target));
            }
        };
        final PriorityQueue<Vertex> q = new BHeap<Vertex>(cmp, n){
            @Override
            public Position push(Vertex x) {
                System.out.println("push vertex : "+x);
                return super.push(x);
            }
        };

        /** init 2 arrays, come_from is no need to init to an illegal value like -1 */
        for (int i=0; i<n; i++) {
            cost[i] = Integer.MAX_VALUE;
            //flags[i] = NON_visited; // default to 0
        }
        cost[start_order] = 0;
        flags[start_order] = IN_queue;
        positions[start_order] = q.push(s0);

        while (!q.isEmpty()) {
            Vertex vertex = q.deleteMin();
            int order = vertex.getOrder();
            int v_cost = cost[order];
            flags[order] = DONE;
            if (order == to_order)
                break;

            // change neighbors
            List<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                Vertex to = edge.getTo();
                int next_index = to.getOrder();
                switch (flags[next_index]) {
                    case DONE : break;
                    case NON_visited :
                        positions[next_index] = q.push(to);
                        flags[next_index] = IN_queue;
                        cost[next_index] = v_cost + edge.getCost().cost();
                        come_from[next_index] = vertex;
                        break;
                    case IN_queue:
                        int new_cost = v_cost + edge.getCost().cost();
                        if (new_cost < cost[next_index]) {
                            cost[next_index] = new_cost;
                            come_from[next_index] = vertex;
                        }
                        q.updatePriority(positions[next_index]);
                        break;
                }
            }
        }

        if (come_from[to_order] == null) return null; // no path

        /** collect vertices that form the path : s0, target, come_from */
        ArrayList<Vertex> path = new ArrayList<>();
        for (Vertex tmp=target; tmp!=s0; tmp=come_from[tmp.getOrder()])
            path.add(tmp);
        path.add(s0);
        return path;
    }
}
