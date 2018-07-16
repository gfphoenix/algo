package net.graph.algos;

import net.graph.Edge;
import net.graph.Graph;
import net.graph.Vertex;

import java.util.List;

/**
 * Created by wuhao on 8/14/16.
 */
public class MiscAlgos {

    public static int cc(Graph g) {
        final int n = g.getVertexCount();
        UnionSetInt set = new UnionSetInt(n);

        for (int i=0; i<n; i++) {
            Vertex vertex = g.getVertexByOrder(i);
            int order = vertex.getOrder();
            for (Edge edge : vertex.getEdges()) {
                set.union(order, edge.getTo().getOrder());
            }
        }

        return set.unionSize();
    }

    public static void main(String []args) {
        Graph g = new Graph("g2");
        for (int i=1; i<=6; i++){
            Vertex v = new Vertex();
            g.addVertex(v);
            g.try_rename(v, ""+i);
        }
        int []edges = {
                1,5, 29,
                1,6, 10,
                5,2, 19,
                5,6, 30,
                2,3, 31,
                3,5, 8,
                4,3, 37,
                6,4, 11,
        };
        for (int i=0; i<edges.length; i+=3) {
            g.addEdge(""+edges[i], ""+edges[i+1], new Dijkstra.IntCost(edges[i+2]));
        }
        g.check();
        final int n = g.getVertexCount();

        System.out.println(g);
        StringBuilder sb = new StringBuilder();
        Dijkstra dj = new Dijkstra();
        int []c1 = dj.dijkstra_cost(g, "1");
        sb.append("start from vertex 1: ");
        appendArray(sb, c1);
        System.out.println(sb);

        AStar as = new AStar();
        AStar.H h = new AStar.H() {
            @Override
            public int cost(Graph graph, int from_order, int to_order) {
                return 0;
            }
        };
        List<Vertex> path = as.a_star_search(g, h, 0, 2);
        int i=path.size();
        while (i-->0) {
            System.out.println(path.get(i).getTag());
        }

        System.out.println("test dfs ...");
        DFS.dfs(g, 0);

        Vertex [][]come_from = new Vertex[n][n];
        Vertex [][]next = new Vertex[n][n];
        int [][]f = Floyed.floy(g, come_from, next);
        for (int k=0; k<f.length; k++) {
            for (int m=0; m<f[k].length; m++) {
                System.out.printf("%d  ", f[k][m]);
            }
            System.out.println();
        }
        path.clear();
        Floyed.build_path_come_from(g, come_from, path, 2,2);
        System.out.println("print build path = ");
        for (Vertex vertex : path) {
            System.out.println(vertex.getTag());
        }
        path.clear();
        Floyed.build_path_next(g, next, path, 1, 3);
        System.out.println("print build path = ");
        for (Vertex vertex : path) {
            System.out.println(vertex.getTag());
        }
        System.out.println("cc size = " + MiscAlgos.cc(g));
    }
    static <T> StringBuilder appendArray(StringBuilder sb, T []a) {
        if (a==null) return sb.append("<nil>");
        sb.append("[ ");
        if (a.length>0)
            sb.append(a[0]);
        for (int i=1,n=a.length; i<n; i++)
            sb.append(", ").append(a[i]);
        sb.append(" ]");
        return sb;
    }
    static void appendArray(StringBuilder sb, int []a){
        if (a==null) {
            sb.append("<nil>");
            return;
        }
        sb.append("[ ");
        if (a.length>0)
            sb.append(a[0]);
        for (int i=1,n=a.length; i<n; i++)
            sb.append(", ").append(a[i]);
        sb.append(" ]");
    }
}
