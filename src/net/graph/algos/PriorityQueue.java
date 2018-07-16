package net.graph.algos;

import java.util.Comparator;

/**
 * Created by wuhao on 8/14/16.
 */
// priority is relative, so can
public interface PriorityQueue<T> {
    public interface Position { // opaque for user
    }
    default boolean isEmpty(){
        return size()==0;
    }
    int size();
    Comparator<T> getComparator();
    Position push(T x);
    // throws :
    //      InvalidPositionException if position is invalid
    //      UnderflowException if Q is empty
    T peek(Position position);
    // throws :
    //      UnderflowException if Q is empty
    T findMin();
    T deleteMin();
    // after set new priority, call this function
    default boolean updatePriority(Position p){
        throw new UnsupportedOperationException("Q="+getClass().getName());
    }
    class InvalidPositionException extends RuntimeException {
        public InvalidPositionException(String message) {
            super(message);
        }
    }
    class UnderflowException extends RuntimeException {
        public UnderflowException(String message) {
            super(message);
        }
    }
}
