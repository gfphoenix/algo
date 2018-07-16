package net.graph;

/**
 * Created by wuhao on 8/13/16.
 */
public class Edge {
    Vertex from;
    Vertex to;
    Cost cost;
//    String tag=""; // optional

    Edge(Cost cost) {
        this.cost =cost;
    }
    Edge set(Vertex from, Vertex to){
        this.from = from;
        this.to = to;
        return this;
    }

    public Cost getCost(){
        return cost;
    }
    public Vertex getFrom() {
        return from;
    }
    public Vertex getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "edge: [ from("+from+") => to("+to+") - cost="+cost.cost()+" ]";
    }
}
