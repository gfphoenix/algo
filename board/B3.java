package misc.board;

import java.util.*;

/**
 * Created by wuhao on 7/30/16.
 */
public class B3 {
    static int random(int n) {
        return (int)(Math.random()*n);
    }
    static void shuffle(int []a){
        for (int i=a.length; i>0; i--) {
            int k = random(i);
            int tmp = a[k];
            a[k] = a[i-1];
            a[i-1] = tmp;
        }
    }
    static final int ROW = 3;
    static final int COL = 3;
    int []board;
    public B3(){
        board = initBoard();
    }
    int []initBoard() {
        int []b = new int[9];
        for (int i=0; i<9; i++)
            b[i] = i;
        return b;
    }
void shuffle(){
    shuffle(board);
}


    static class Signature implements Comparable<Signature>{
        int []sig_;
        int dep;
        Signature(int []a){
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
            for(int i=0; i<9; i++){
                int r = sig_[i] - signature.sig_[i];
                if (r!=0)
                    return r;
            }
            return 0;
        }

        int find(){
            for (int i=0; i<9; i++)
                if (sig_[i] == 0)
                    return i;
            throw new RuntimeException("no zero found");
        }
    }
    Set<Signature> signatures = new TreeSet<>();
    boolean validIndex(int row, int col) {
        return row>=0 && row<ROW && col>=0 && col<COL;
    }
    int toI(int r, int c){
        return r*COL + c;
    }
    int toR(int idx) {
        return idx/3;
    }
    int toC(int idx) {
        return idx%3;
    }
    void swap(int i1, int i2) {
        int tmp = board[i1];
        board[i1] = board[i2];
        board[i2] = tmp;
    }
    void swap(int []a, int i, int j){
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    void walkBFS0(){
//        ArrayList<Signature> list = new ArrayList<>();
        ArrayDeque<Signature> list = new ArrayDeque<>();
        list.add(new Signature(Arrays.copyOf(board, board.length)));
        while (!list.isEmpty()) {
            Signature sig = list.poll();
            if (signatures.contains(sig)){
                continue;
            }
            signatures.add(sig);
            int idx = sig.find();
            int row= toR(idx);
            int col= toC(idx);
            int xx, yy, i2;
            xx = row; yy=col-1;
            if (validIndex(xx, yy)){
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, 9));
                s.dep = sig.dep + 1;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row; yy=col+1;
            if (validIndex(xx, yy)){
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, 9));
                s.dep = sig.dep + 1;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row-1; yy=col;
            if (validIndex(xx, yy)){
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, 9));
                s.dep = sig.dep + 1;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
            xx = row+1; yy=col;
            if (validIndex(xx, yy)){
                i2 = toI(xx, yy);
                Signature s = new Signature(Arrays.copyOf(sig.sig_, 9));
                s.dep = sig.dep + 1;
                swap(s.sig_, idx, i2);
                list.add(s);
            }
        }
    }
    void walkBFS(){

    }
    void walk(int row, int col){
        for (Signature sig : signatures) {
            if (Arrays.equals(board, sig.sig_))
                return;
        }
        signatures.add(new Signature(Arrays.copyOf(board, board.length)));
        int idx = toI(row, col);
        int i2;
        int xx,yy;
        xx = row; yy=col-1;
        if (validIndex(xx, yy)){
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row; yy=col+1;
        if (validIndex(xx, yy)){
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row-1; yy=col;
        if (validIndex(xx, yy)){
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
        xx = row+1; yy=col;
        if (validIndex(xx, yy)){
            i2 = toI(xx, yy);
            swap(idx, i2);
            walk(xx, yy);
            swap(idx, i2);
        }
    }
    // | %2d | %2d |
    // ------------- n*5 +1
    void appendLine(StringBuilder sb, int w) {
        w = 1 + w * (3+1);
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
    int at(int r, int c){
        return board[toI(r, c)];
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        final int row = ROW;
        final int col = COL;
        for (int r = 0; r < row; r++) {
            appendLine(sb, col);
            sb.append('|');
            for (int c = 0; c < col; c++) {
                sb.append(' ');
                appendNumber(sb, at(r,c));
                sb.append(' ').append('|');
            }
            sb.append('\n');
        }
        appendLine(sb, col);
        return sb.toString();
    }

    public void testWalk(){
        B3 b = new B3();
        System.out.println(b);
        b.walkBFS0();
        System.out.println(b);
        System.out.println("signatures = "+b.signatures.size());
        int dep = 0;
        for (Signature sig : signatures)
            if (sig.dep>dep)
                dep = sig.dep;
        System.out.println("max depth = "+dep);
    }
}
