package net.graph;

import java.util.*;

/**
 * Created by wuhao on 8/13/16.
 */
public class Vertex {
    public Vertex(){
        this.edges = new ArrayList<>();
    }
    Vertex(String tag){
        this.tag = tag;
        this.edges = new ArrayList<>();
    }

    public Graph getGraph() {
        return graph;
    }
    public String getTag() {
        return tag;
    }
    public int getOrder(){
        return order;
    }

    public Iterator<Edge> getEdgeIterator() {
        return edges.iterator();
    }
    public List<Edge> getEdges() {
        return edges;
    }

    public Object getUserObject() {
        return userObject;
    }
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public String toString() {
        return "vertex: [ tag='"+tag+"', graph='"+graph.getNamespace()+"', len(to)="+ edges.size()+" ]";
    }
    String tag="";
    Graph graph;
    List<Edge> edges; // ordered by to vertex
    Object userObject;
    int order=-1;
}
