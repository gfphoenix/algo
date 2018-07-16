package misc.board;

import java.util.*;

class BoardUtils {
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
    static final Vector2[] dir = {
        new Vector2(0, 1), new Vector2(-1, 0),
        new Vector2(0, -1), new Vector2(1, 0),
};
    
    public static Vector2 shuffle(IBoard board, int depth) {
    	Vector2 current = new Vector2();
    	return shuffle(board, current, depth);
    }
    
    static Vector2 shuffle(IBoard board, Vector2 current, int dep) {

        int x = current.x, y = current.y;
        int xx, yy;
        while (true) {
        	Vector2 v = dir[random(4)];
            xx = x + v.x;
            yy = y + v.y;
            if (board.validIndex(xx, yy))
            break;
        }
        
        board.swap(x, y, xx, yy);
        dep--;
        current.set(xx, yy);
        if (dep<=0) return current;
        return shuffle(board, current, dep);
    }
    public static Map<IBoard, Signature> generateB3() {
        return generateB(3);
    }
    public static Map<IBoard, Signature> generateB2() {
        return generateB(2);
    }
    public static Map<IBoard, Signature> generateB(int N) {
    	Map<IBoard, Signature> set = new TreeMap<IBoard, Signature>();
    	BN b = new BN(N).initBoard();
    	ArrayDeque<Signature> q = new ArrayDeque<>();
    	q.add(new Signature(b));
    	Vector2 []dir = BoardUtils.dir;
    	int dir_n = dir.length;
        int maxDep=0;
    	while(!q.isEmpty()) {
    		Signature sig = q.poll();
    		if(set.get(sig.board)!=null) continue;
    		
    		set.put(sig.board, sig);
    		int idx = sig.idx();
    		int row = b.toR(idx);
    		int col = b.toC(idx);
    		
    		for(int i=0; i<dir_n; i++) {
    			int xx = row + dir[i].x;
    			int yy = col + dir[i].y;
    			if(b.validIndex(xx, yy)) {
    				Signature s = new Signature(sig.board.copy());
    				s.idx = b.toI(xx, yy);
    				s.last = sig;
                    s.dep = maxDep = sig.dep + 1;
    				s.board.swap(row, col, xx, yy);
    				
    				q.add(s);
    			}
    		}
    	}
        System.out.println("max dep  = "+maxDep);
    	return set;
    }
    public static Map<IBoard, Signature> generateBoard(IBoard board) {
        Map<IBoard, Signature> set = new TreeMap<IBoard, Signature>();
        IBoard b = board;
        ArrayDeque<Signature> q = new ArrayDeque<>();
        q.add(new Signature(b));
        Vector2 []dir = BoardUtils.dir;
        int dir_n = dir.length;
        int maxDep=0;
        while(!q.isEmpty()) {
            Signature sig = q.poll();
            if(set.get(sig.board)!=null) continue;

            set.put(sig.board, sig);
            int idx = sig.idx();
            int row = b.toR(idx);
            int col = b.toC(idx);

            for(int i=0; i<dir_n; i++) {
                int xx = row + dir[i].x;
                int yy = col + dir[i].y;
                if(b.validIndex(xx, yy)) {
                    Signature s = new Signature(sig.board.copy());
                    s.idx = b.toI(xx, yy);
                    s.last = sig;
                    s.dep = maxDep = sig.dep + 1;
                    s.board.swap(row, col, xx, yy);

                    q.add(s);
                }
            }
        }
        System.out.println("max dep  = "+maxDep);
        return set;
    }

    private static int digits(int n){
        int d = 0;
        while (n>0){
            d++;
            n /= 10;
        }
        return d;
    }
    // | %2d | %2d |
    // ------------- n*5 +1
    private static void appendLine(IBoard board, StringBuilder sb, int w) {
        w = 1 + w * (3 + digits(board.number()-1));
        for (int i = 0; i < w; i++) {
            sb.append('-');
        }
        sb.append('\n');
    }

    private static void appendNumber(IBoard board, StringBuilder sb, int n) {
        String fmt = "%" + digits(board.number()-1) + "d";
        String s = String.format(Locale.US, fmt, n);
        sb.append(s);
    }

    public static StringBuilder toString(IBoard board, StringBuilder sb) {
        final int row = board.row();
        final int col = board.col();
        int tmp;
        for (int r = 0; r < row; r++) {
            appendLine(board, sb, col);
            sb.append('|');
            for (int c = 0; c < col; c++) {
                sb.append(' ');
                appendNumber(board, sb, board.at(r, c));
                sb.append(' ').append('|');
            }
            sb.append('\n');
        }
        appendLine(board, sb, col);
        return sb;
    }

    private static void usage(){
        System.out.println("WASD to move");
    }
    public static void play(IBoard b) {

        System.out.println(b);
        Scanner scanner = new Scanner(System.in);
        usage();
        int row=-1, col=-1;
        for (int i=0,n=b.number(); i<n; i++)
            if (b.at(i) == 0){
                row = b.toR(i);
                col = b.toC(i);
                break;
            }
        if (row<0 || col<0)
            throw new IllegalArgumentException("bad board");

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
}
