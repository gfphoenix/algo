package net.graph.algos;

/**
 * Created by wuhao on 8/14/16.
 */
// each item has the absolute priority value
public interface AbsolutePriorityQueue<T> {
    interface Position {
        Comparable getValue();
    }
    Comparable getPriority(T x);
    default boolean isEmpty(){
        return size()==0;
    }
    int size();
    Position push(T x);
    T findMin();
    T deleteMin();
    default Position setPriority(Position p, Comparable new_Val){
        throw new UnsupportedOperationException("Q="+getClass().getName());
    }

    class UnderflowException extends RuntimeException {
        public UnderflowException(String message) {
            super(message);
        }
    }
}
