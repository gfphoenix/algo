package misc.board;

public class Signature {
	IBoard board; // maybe snapshot better
	int dep; // moves depth
	int idx; // position of zero
	Signature last;
	public Signature(IBoard board) {
		this.board = board;
	}
	public int dep(){
		int d = 0;
		Signature sig = last;
		while(sig!=null){
			d++;
			sig = sig.last;
		}
		return d;
	}
	public int idx(){
		return idx;
	}
	public IBoard board(){
		return this.board;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Signature)
			return board.equals(((Signature)obj).board);
		return false;
	}

	@Override
	public String toString() {
		return board.toString() + "\n dep="+dep;
	}
}
