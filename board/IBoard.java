package misc.board;

public interface IBoard extends Comparable<IBoard> {
    int row();
    int col();
    IBoard copy();
    // must impl one of at
    default int at(int row, int col) {
        return at(toI(row, col));
    }
    default int at(int idx) {
        return at(toR(idx), toC(idx));
    }
    // must impl one of swap
    default void swap(int r1, int c1, int r2, int c2) {
        swap(toI(r1, c1), toI(r2, c2));
    }
    default void swap(int idx1, int idx2) {
        swap(toR(idx1), toC(idx1), toR(idx2), toC(idx2));
    }

    default int number() {
        return row() * col();
    }
    default int toI(int r, int c){
        int col = col();
        return r * col + c;
    }
    default int toR(int idx) {
        return idx / col();
    }
    default int toC(int idx) {
        return idx % col();
    }
    default boolean validIndex(int idx) {
        return idx>=0 && idx<number();
    }
    default boolean validIndex(int r, int c) {
        return r>=0 && r<row() && c>=0 && c<col();
    }

    default int compareTo(IBoard iBoard) {
        int rc;
        rc = row() - iBoard.row();
        if (rc != 0) return rc;
        rc = col() - iBoard.col();
        if (rc != 0) return rc;
        int n = number();
        for (int i=0; i<n; i++) {
            rc = at(i) - iBoard.at(i);
            if (rc!=0) return rc;
        }
        return 0;
    }
    default boolean isComplete() {
        for(int i=0,n=number(); i<n; i++)
            if (at(i) != i)
                return false;
        return true;
    }

//    default boolean equals(Object o) {
//    	if(o==null || !(o instanceof IBoard)) return false;
//    	IBoard b = (IBoard)o;
//    	int row = row();
//    	int col = col();
//    	if(row!=b.row() || col!=b.col()) return false;
//    	int n = number();
//    	for(int i=0; i<n; i++){
//    		if(at(i) != b.at(i))
//    			return false;
//    	}
//    	return true;
//    }
}
