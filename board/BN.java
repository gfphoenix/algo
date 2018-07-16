package misc.board;

import java.util.*;

/**
 * Created by wuhao on 7/30/16.
 */
public class BN implements IBoard , Comparable<IBoard>{
    static int random(int n) {
        return (int) (Math.random() * n);
    }
    static int getDigits(int N) {
        int d = 0;
        while(N>0) {
            d++;
            N /= 10;
        }
        return d;
    }

    static void shuffle(int[] a) {
        for (int i = a.length; i > 0; i--) {
            int k = random(i);
            int tmp = a[k];
            a[k] = a[i - 1];
            a[i - 1] = tmp;
        }
    }

    final int N;
    int[] board;

    public BN(int size) {
        this.N = size;
        board = new int[size*size];
    }
    @Override
    public BN copy() {
    	BN cp = new BN(N);
    	System.arraycopy(board, 0, cp.board, 0, board.length);
    	return cp;
    }
    @Override
    public boolean equals(Object obj) {
    	if(obj==null || !(obj instanceof IBoard)) return false;
    	return compareTo((IBoard)obj) == 0;
    }
    @Override
	public int compareTo(IBoard o) {
    	int n = number();

    	if(n!=o.number()) return n-o.number();
    	for(int i=0; i<n; i++){
    		int rc = at(i) - o.at(i);
    		if(rc!=0) return rc;
    	}
		return 0;
	}
	@Override
    public int row() {
        return N;
    }
    @Override
    public int col() {
        return N;
    }
    @Override
    public int number() {
        return board.length;
    }
    @Override
    public int at(int idx) {
        return board[idx];
    }
    @Override
    public void swap(int idx1, int idx2) {
        int tmp = board[idx1];
        board[idx1] = board[idx2];
        board[idx2] = tmp;
    }

    public BN initBoard() {
    	int []b = board;
        for (int i = 0, n=board.length; i < n; i++)
            b[i] = i;
        return this;
    }

    void shuffle() {
        shuffle(board);
    }
    Vector2 initShuffle2(){
    	return BoardUtils.shuffle(this, 100);
    }


    class Signature implements Comparable<Signature> {
        int[] sig_;
        int dep;
        int idx; // position of zero
        Signature last;

        Signature(int[] a) {
            this.sig_ = a;
            this.dep = 0;
        }

        public int getDep() {
            return dep;
        }

        public void setDep(int dep) {
            this.dep = dep;
        }

        boolean eq(Signature signature) {
            return Arrays.equals(sig_, signature.sig_);
        }

        @Override
        public int compareTo(Signature signature) {
            final int n = N * N;
            for (int i = 0; i < n; i++) {
                int r = sig_[i] - signature.sig_[i];
                if (r != 0)
                    return r;
            }
            return 0;
        }

        int find() {
            final int n = N * N;
            for (int i = 0; i < n; i++)
                if (sig_[i] == 0)
                    return i;
            throw new RuntimeException("no zero found");
        }
    }

    Set<Signature> signatures = new TreeSet<>();


    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    void walkBFS0() {
        ArrayDeque<Signature> list = new ArrayDeque<>();
        final int len = N * N;
        int d = 0;
        long count=0;
        list.add(new Signature(Arrays.copyOf(board, board.length)));
        while (!list.isEmpty()) {
            Signature sig = list.poll();
            if (signatures.contains(sig)) {
                continue;
            }
            count++;
            if(sig.dep != d){
                d = sig.dep;
                System.out.println("depth = " + d+", count = "+count);
                count = 0;
            }
            signatures.add(sig);
            int idx = sig.idx;
            int row = toR(idx);
            int col = toC(idx);

            int xx, yy, i2;

            xx = row;
            yy = col - 1;
            if (validIndex(xx, yy)) {
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, len));
                s.dep = sig.dep + 1;
                s.idx = toI(xx, yy);
                s.last = sig;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row;
            yy = col + 1;
            if (validIndex(xx, yy)) {
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, len));
                s.dep = sig.dep + 1;
                s.idx = toI(xx, yy);
                s.last = sig;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row - 1;
            yy = col;
            if (validIndex(xx, yy)) {
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, len));
                s.dep = sig.dep + 1;
                s.idx = toI(xx, yy);
                s.last = sig;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row + 1;
            yy = col;
            if (validIndex(xx, yy)) {
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, len));
                s.dep = sig.dep + 1;
                s.idx = toI(xx, yy);
                s.last = sig;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
        }
    }

    void walkBFS() {

    }
    void walkShuffle() {
    }


    void walk(int row, int col) {
        for (Signature sig : signatures) {
            if (Arrays.equals(board, sig.sig_))
                return;
        }
        signatures.add(new Signature(Arrays.copyOf(board, board.length)));
        int idx = toI(row, col);
        int i2;
        int xx, yy;
        xx = row;
        yy = col - 1;
        if (validIndex(xx, yy)) {
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row;
        yy = col + 1;
        if (validIndex(xx, yy)) {
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row - 1;
        yy = col;
        if (validIndex(xx, yy)) {
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row + 1;
        yy = col;
        if (validIndex(xx, yy)) {
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
    }

    // | %2d | %2d |
    // ------------- n*5 +1
    void appendLine(StringBuilder sb, int w) {
        w = 1 + w * (3 + 1);
        for (int i = 0; i < w; i++) {
            sb.append('-');
        }
        sb.append('\n');
    }

    void appendNumber(StringBuilder sb, int n) {
        String fmt = "%" + 1 + "d";
        String s = String.format(Locale.US, fmt, n);
        sb.append(s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final int row = N;
        final int col = N;
        for (int r = 0; r < row; r++) {
            appendLine(sb, col);
            sb.append('|');
            for (int c = 0; c < col; c++) {
                sb.append(' ');
                appendNumber(sb, at(r, c));
                sb.append(' ').append('|');
            }
            sb.append('\n');
        }
        appendLine(sb, col);
        return sb.toString();
    }

    public static void main0(String[] args) {
        BN b = new BN(3);
        System.out.println(b);
        b.walkBFS0();
        System.out.println(b);
        System.out.println("signatures = " + b.signatures.size());
        int dep = 0;
        for (Signature sig : b.signatures)
            if (sig.dep > dep)
                dep = sig.dep;
        System.out.println("max depth = " + dep);
    }
    static void usage() {
		System.out.println("WASD to move");
	}
    public static void main2(String[] args) {
		BN b = new BN(3);
		System.out.println(b);
		Vector2 v = b.initShuffle2();
		System.out.println(b);
		Scanner scanner = new Scanner(System.in);
		usage();
		int row = v.x;
		int col = v.y;
		int xx = row, yy = col;
		while (scanner.hasNext()) {
			String s = scanner.next();
			if (s.length() != 1) {
				usage();
				System.out.println(b);
				continue;
			}
			switch (s.charAt(0)) {
			case 'w':
			case 'W':
				xx = row - 1;
				break;
			case 'a':
			case 'A':
				yy = col - 1;
				break;
			case 's':
			case 'S':
				xx = row + 1;
				break;
			case 'd':
			case 'D':
				yy = col + 1;
				break;
			default:
				usage();
				continue;
			}
			if (!b.validIndex(xx, yy)) {
				xx = row;
				yy = col;
				System.out.println("out of bounds");
				System.out.println(b);
				continue;
			}
			b.swap(row, col, xx, yy);
			row = xx;
			col = yy;
			System.out.println(b);

			if (b.isComplete()) {
				System.out.println("************************");
				System.out.println("Success !!!");
				System.out.println("************************");
				break;
			}
		}

	}
    public static void main(String []args) {
    	testB2();
    }
    static void testB2() {
        final int N = 2;
        BN b = new BN(N);
        b.initBoard();
        b.initShuffle2();
        System.out.println(b);

        Map<IBoard, Signature> map = BoardUtils.generateB(N);
        System.out.println("map size = "+map.size()	);

        IBoard bb = b;
        Signature sig = map.get(b);
        System.out.println("dep = "+(sig==null?0:sig.dep()));
        int cc = 0;
        while(sig!=null) {
            System.out.println("*****************  " + cc + "  *********************");
            System.out.println(sig);
            sig = sig.last;
            cc++;
        }
        System.out.println("depth = "+cc);

        cc = 0;
        for (Signature key :map.values()){
            cc++;
            System.out.println("**********************************");
            System.out.println("    NO. "+cc);
            System.out.println(key);
        }
    }
    static void testB3() {
    	BN b = new BN(3);
    	b.initBoard();
    	b.initShuffle2();
    	System.out.println(b);

    	Map<IBoard, Signature> map = BoardUtils.generateB3();
    	System.out.println("map size = "+map.size()	);

    	IBoard bb = b;
    	Signature sig = map.get(b);
        System.out.println("dep = "+(sig==null?0:sig.dep()));
        int cc = 0;
        while(sig!=null) {
            System.out.println("*****************  " + cc + "  *********************");
            System.out.println(sig);
            sig = sig.last;
            cc++;
        }
        System.out.println("depth = "+cc);
    }
}
