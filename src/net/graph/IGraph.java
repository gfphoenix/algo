package net.graph;

import java.util.Iterator;

public interface IGraph {
    Iterator<Vertex> iterVertices();
    Iterator<Edge> iterEdges();
    Iterator<Edge> neighboor(Vertex vertex);
    int vertexSize();

    int edgeSize();
}
