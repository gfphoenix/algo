package net.graph;

import java.util.*;

/**
 * Created by wuhao on 8/13/16.
 */
public class Graph {
    private List<Vertex> vertices;
    private Map<String, Vertex> map;
    private String namespace;
    private int counter;
    public Graph(){
        this("GF_");
    }
    public Graph(String namespace) {
        this.namespace = namespace;
        this.vertices = new ArrayList<Vertex>();
        this.map = new HashMap<String, Vertex>();
    }

    public String getNamespace() {
        return namespace;
    }
    public int getCounter() {
        return counter;
    }

    private String generateName() {
        return namespace + (counter++);
    }
    public boolean try_rename(Vertex vertex, String tag) {
        if (tag==null) return false;
        Vertex v = map.get(tag);
        if (v!=null) return false;

        String old = vertex.getTag();
        map.remove(old);
        map.put(tag, v);
        vertex.tag = tag;
        return true;
    }
    private void check_and_generate_tag(Vertex v) {
        String tag = v.getTag();
        if (tag.length()!=0) return;
        while (map.get(tag) != null)
            tag = generateName();
        v.tag = tag;
    }
    public Vertex addVertex(Vertex vertex) {
        if (vertex==null) throw new NullPointerException("null vertex");
        if (vertex.getGraph()!=null) throw new IllegalStateException("vertex is added already");
        if (vertex.getTag()==null) throw new NullPointerException("null tag");
        check_and_generate_tag(vertex);
        if (map.get(vertex.getTag())!=null) throw new IllegalArgumentException("exists tag");
        vertex.graph = this;
        vertex.order = this.vertices.size();
        this.vertices.add(vertex);
        this.map.put(vertex.getTag(), vertex);
        return vertex;
    }
    public boolean addEdge(String from, String to, Cost cost) {
        return addEdge(from, to, cost, false);
    }
    public boolean addEdge(String from, String to, Cost cost, boolean both) {
        if (from==null || to==null) throw new NullPointerException("from="+from+", to="+to);
        return addEdge(map.get(from), map.get(to), cost, both);
    }
    public boolean addEdge(Vertex from, Vertex to, Cost cost) {
        return addEdge(from, to, cost, false);
    }
    public boolean addEdge(Vertex from, Vertex to, Cost cost, boolean both) {
        if (from==null || to==null) throw new NullPointerException("vertex not found: ("+from+", "+to+")");
        Edge edge = new Edge(cost).set(from, to);
        from.getEdges().add(edge);
        if (both) {
            edge = new Edge(cost).set(to, from);
            to.getEdges().add(edge);
        }
        return true;
    }

    public void revalidateOrder() {
        for (int i=0,n=vertices.size(); i<n; i++)
            vertices.get(i).order = i;
    }
    public int getVertexCount() {
        return vertices.size();
    }
    public Vertex getVertexByOrder(int order) {
        return vertices.get(order);
    }
    public Vertex getVertexByTag(String tag) {
        return map.get(tag);
    }
    public int getVertexOrderByTag(String tag) {
        Vertex vertex = map.get(tag);
        return vertex==null ? -1 : vertex.getOrder();
    }
    public int getEdgeCount() {
        int c=0;
        for (Vertex v : vertices)
            c += v.getEdges().size();
        return c;
    }
    public void swapVertexOrder(int i, int j){
        final int n = vertices.size();
        if (i<0 || j<0 || i>=n || j>=n) throw new IndexOutOfBoundsException("invalid index i & j");
        if (i==j) return;
        Vertex tmp = vertices.get(i);
        vertices.set(i, vertices.get(j));
        vertices.set(j, tmp);
        revalidateOrder();
    }
    public void setVertexOrder(Vertex vertex, int i) {
        if (vertex==null) throw new NullPointerException("null vertex");
        if (vertex.getGraph()!=this) throw new IllegalStateException("graph of this vertex is not this");
        swapVertexOrder(vertex.getOrder(), i);
    }
    public void sortVertex(Comparator<Vertex> comparator) {
        Collections.sort(vertices, comparator);
        revalidateOrder();
    }
    // after this call, all order maybe changed,
    // and you should get a fresh order from vertex
    public Vertex removeVertexByOrder(int order) {
        // remove vertex from list and map
        if (order<0 || order>=vertices.size())
            throw new IndexOutOfBoundsException("order = "+order);
        Vertex vertex = vertices.remove(order);
        map.remove(vertex.getTag());
        // remove all edges start from or move to this vertex
        for (int k=0,n=vertices.size(); k<n; k++) {
            Vertex tmp = vertices.get(k);
            tmp.order = k;
            List<Edge> edges = tmp.getEdges();
            for (int i=edges.size()-1; i>=0; i--) {
                if (edges.get(i).to == vertex)
                    edges.remove(i);
            }
        }
        vertex.order = -1;
        vertex.graph = null;
        return vertex;
    }
    public Vertex removeVertexByTag(String tag) {
        Vertex vertex = map.get(tag);
        if (vertex==null) throw new RuntimeException("no such vertex=("+tag+") found");
        return removeVertexByOrder(vertex.getOrder());
    }
    public void removeEdge(Edge edge, boolean both) {
        if (edge==null) throw new NullPointerException("null edge");
        Vertex from = edge.from;
        Vertex to = edge.to;
        if (from==null || to==null) throw new IllegalStateException("null: from="+from+", or="+to);
        if (from.getGraph()!=this || to.getGraph()!=this) throw new IllegalStateException("vertex not owned by this graph");

        List<Edge> list = from.getEdges();
        for (int i=list.size()-1; i>=0; i--) {
            if (list.get(i).to == to)
                list.remove(i);
        }
        if (both) {
            list = to.getEdges();
            for (int i=list.size()-1; i>=0; i--) {
                if (list.get(i).to == from)
                    list.remove(i);
            }
        }
    }

    public void check() {
        final int n = vertices.size();
        if (map.size() != n) {
            throw new IllegalStateException("size(vertex) = "+n+", size(map) = "+map.size());
        }
        for (int i=0; i<n; i++) {
            final Vertex vertex = vertices.get(i);
            if (vertex==null) throw new NullPointerException("null vertex at i="+i);
            if (vertex.order!=i) throw new IllegalStateException("order not equal: i=>order "+i+" => "+vertex.order);
            if (vertex.getGraph()!=this) throw new IllegalStateException("vertex not owned by this graph");
            if (vertex.getTag()==null) throw new IllegalArgumentException("vertex tag cann't be null");
            if (map.get(vertex.getTag())!=vertex) throw new IllegalStateException("vertex not found in map");

            for (Edge edge : vertex.getEdges()) {
                if (edge.from != vertex) throw new IllegalStateException("edge not start from the current vertex");
                if (edge.to == null) throw new IllegalStateException("to vertex is null");
                if (map.get(edge.to.getTag())==null) throw new IllegalStateException("to vertex not found in map");
                if (edge.getCost()==null) throw new IllegalArgumentException("cost of edge is null");
            }
        }
        System.out.println("All OK !!!");
    }

    public static Graph cloneGraphFrom(Graph g) {
        if (g==null) return null;
        Graph gg = new Graph(g.getNamespace());
        int n = g.getVertexCount();
        for (int i=0; i<n; i++) {
            Vertex vertex = g.getVertexByOrder(i);
            Vertex clone = new Vertex(vertex.getTag());
            gg.addVertex(clone);
        }
        for (int i=0; i<n; i++) {
            Vertex vertex = g.getVertexByOrder(i);
            List<Edge> edges = vertex.getEdges();
            for (Edge edge : edges)
                gg.addEdge(edge.from.getTag(), edge.to.getTag(), edge.cost);
        }
        gg.counter = g.counter;
        return gg;
    }
}
